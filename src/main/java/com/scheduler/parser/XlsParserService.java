package com.scheduler.parser;

import com.scheduler.parser.temporary.*;
import com.scheduler.timetable.WeekDay;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.*;

import static com.beust.jcommander.internal.Lists.newArrayList;
import static com.scheduler.parser.ParserPredicates.*;
import static com.scheduler.parser.ParserStringUtils.BLANK_ROW_REGEX;
import static com.scheduler.parser.ParserStringUtils.splitNotEmpty;

@Service
@RequiredArgsConstructor
public class XlsParserService
{
    private final XlsStoreService storeService;

    public String parse( InputStream inputStream ) throws IOException, InvalidFormatException, URISyntaxException
    {
        List<String> allRows = readNotEmptyLinesFromFile( inputStream );
        List<String> groupNames = getGroupNames( allRows );
        Map<WeekDay, List<String>> weekDayListMap = getDataPerWeeks( allRows );

        List<RowsPerWeekDay> dataPerWeekDays = new ArrayList<>();
        weekDayListMap.forEach( ( weekDay, value ) -> dataPerWeekDays.add( new RowsPerWeekDay( weekDay, getDataPerLesson( value ) ) ) );
        Collection<DataPerGroup> dataPerGroup = getGroupScheduler( groupNames, dataPerWeekDays );

        StringBuilder stringBuilder = new StringBuilder();
        dataPerGroup.forEach( dataPerGroup1 -> {
            dataPerGroup1.getDataPerWeekDays().forEach( dataPerWeekDay -> dataPerWeekDay.getDataPerLessonList().forEach( DataPerLessonTime::parse ) );
            stringBuilder.append( "\n" );
            stringBuilder.append( dataPerGroup1 );
        } );
        storeService.storeData( dataPerGroup );
        return stringBuilder.toString();
    }

    private Collection<DataPerGroup> getGroupScheduler( List<String> groupNames, List<RowsPerWeekDay> rowsPerWeekDays )
    {
        Map<Integer, DataPerGroup> dataPerGroups = new HashMap<>();
        rowsPerWeekDays.forEach( row ->
            updateOrCreate( dataPerGroups, groupNames, row.getWeekDay(), getLessonDataPerGroupNumber( row.getDataPerLessonList() ) ) );

        return dataPerGroups.values();
    }

    private Map<Integer, List<DataPerLessonTime>> getLessonDataPerGroupNumber( List<RowsPerLesson> rowsPerLessons )
    {
        Map<Integer, List<DataPerLessonTime>> lessonDataPerGroup = new HashMap<>();
        rowsPerLessons.forEach( rowsPerLesson -> {
            for( int rowCounter = 0; rowCounter < rowsPerLesson.getRows().size(); rowCounter++ )
            {
                String row = rowsPerLesson.getRows().get( rowCounter ).substring( 1 ); //get without first |
                String[] splitRow = row.split( "\\|" );

                Map<Integer, List<String>> groupNumberWithCellData = groupByFourElements( splitRow ); //4 cause for every group we have 4 cells

                int finalRowCounter = rowCounter;
                groupNumberWithCellData.forEach( ( groupNumber, columnData ) ->
                    putOrUpdate( lessonDataPerGroup, groupNumber, rowsPerLesson.getLessonTime(), columnData, finalRowCounter ) );
            }
        } );
        return lessonDataPerGroup;
    }

    private void updateOrCreate( Map<Integer, DataPerGroup> dataPerGroups, List<String> groupNames, WeekDay weekDay, Map<Integer, List<DataPerLessonTime>> groupNumberWithCellData )
    {
        groupNumberWithCellData.forEach( ( groupNumber, dataPerLesson ) -> {
            DataPerWeekDay dataPerWeekDay = new DataPerWeekDay();
            dataPerWeekDay.setWeekDay( weekDay );
            dataPerWeekDay.setDataPerLessonList( dataPerLesson );

            if( dataPerGroups.get( groupNumber ) == null )
            {
                DataPerGroup dataPerGroup = new DataPerGroup();
                dataPerGroup.setGroupName( groupNames.get( groupNumber ) );
                dataPerGroup.setDataPerWeekDays( newArrayList( dataPerWeekDay ) );
                dataPerGroups.put( groupNumber, dataPerGroup );
            }
            else
            {
                dataPerGroups.get( groupNumber ).getDataPerWeekDays().add( dataPerWeekDay );
            }
        } );
    }

    private void putOrUpdate( Map<Integer, List<DataPerLessonTime>> lessonDataPerGroup, Integer key, String lessonTime, List<String> columnData, int rowNumber )
    {
        if( columnData.isEmpty() || columnData.stream().allMatch( String::isEmpty ) )
        {
            return;
        }
        DataPerLessonTime data = new DataPerLessonTime();
        data.setLessonTime( lessonTime );
        data.setRow( rowNumber, columnData );
        if( lessonDataPerGroup.get( key ) == null )
        {
            lessonDataPerGroup.put( key, newArrayList( data ) );
        }
        else
        {
            lessonDataPerGroup.get( key )
                              .stream()
                              .filter( dataPerLesson -> dataPerLesson.getLessonTime().equals( lessonTime ) )
                              .findFirst()
                              .ifPresentOrElse(
                                  dataPerLesson -> dataPerLesson.setRow( rowNumber, columnData ),
                                  () -> lessonDataPerGroup.get( key ).add( data ) );

        }
    }

    private Map<Integer, List<String>> groupByFourElements( String[] splitRow )
    {
        int numberCount = 0;
        Map<Integer, List<String>> numberWithForElements = new HashMap<>();
        List<String> rows = new ArrayList<>();
        for( int i = 0; i < splitRow.length; i++ )
        {
            rows.add( splitRow[i] );
            if( i != 0 && ( i + 1 ) % 4 == 0 ||
                ( i + 1 == splitRow.length && !rows.isEmpty() ) ) //handle last cells (it can be not multiple 4)
            {
                numberWithForElements.put( numberCount++, rows );
                rows = new ArrayList<>();
            }
        }
        return numberWithForElements;
    }

    private List<String> readNotEmptyLinesFromFile( InputStream inputStream ) throws IOException, InvalidFormatException, URISyntaxException
    {
        Workbook workbook = WorkbookFactory.create( inputStream );
        Sheet sheet = workbook.getSheetAt( 0 );
        List<String> allRows = new ArrayList<>();

        sheet.forEach( row -> {
            StringBuilder rowString = new StringBuilder();
            row.forEach( cell -> {
                if( cell != null )
                {
                    rowString.append( cell.toString() );
                    rowString.append( "|" );
                }
            } );
            if( !rowString.toString().isEmpty() && !rowString.toString().matches( BLANK_ROW_REGEX ) )
            {
                rowString.deleteCharAt( rowString.length() - 1 ); //remove last |
                allRows.add( rowString.toString() );
            }
        } );
        return allRows;
    }

    private List<String> getGroupNames( List<String> allRows )
    {
        List<String> groupNames = new ArrayList<>();
        allRows.stream()
               .filter( IS_GROUP_NAME_ROW::test )
               .findFirst()
               .ifPresent( groupNamesRow -> {
                   List<String> groupNamesWithSpaces = splitNotEmpty( groupNamesRow, "\\|" );
                   groupNamesWithSpaces.forEach( name -> {
                       if( name.contains( " " ) ) //need cut after space, cause in group name we have "МС-3 17"
                       {
                           groupNames.add( name.substring( 0, name.indexOf( " " ) ) );
                       }
                       else
                       {
                           groupNames.add( name );
                       }
                   } );
               } );
        return groupNames;
    }

    private Map<WeekDay, List<String>> getDataPerWeeks( List<String> allRows )
    {
        Map<WeekDay, List<String>> dataPerWeek = new HashMap<>();
        for( int rowNumber = 0; rowNumber < allRows.size(); )
        {
            String currentRow = allRows.get( rowNumber++ );//get current row and increased counter
            if( IS_ROW_START_WITH_WEEK_DAY.test( currentRow ) )
            {
                List<String> data = new ArrayList<>();
                WeekDay weekDay = WeekDay.getByRussianName( currentRow.substring( 0, currentRow.indexOf( "|" ) ) );
                currentRow = currentRow.substring( currentRow.indexOf( "|" ) ); //cut weekDay name from string
                data.add( currentRow );

                while( rowNumber < allRows.size() - 1 && !IS_ROW_START_WITH_WEEK_DAY.test( allRows.get( rowNumber ) ) )//save data before we find next weekDay
                {
                    data.add( allRows.get( rowNumber++ ) );//get current row and increased counter
                }
                dataPerWeek.put( weekDay, data );
            }
        }
        return dataPerWeek;
    }

    private List<RowsPerLesson> getDataPerLesson( List<String> weekDayRows )
    {
        List<RowsPerLesson> dataPerLessonList = new ArrayList<>();
        for( int rowNumber = 0; rowNumber < weekDayRows.size(); )
        {
            String currentRow = weekDayRows.get( rowNumber++ ); //get current row and increased counter
            if( IS_ROW_START_WITH_LESSON_TIME.test( currentRow ) )
            {
                List<String> data = new ArrayList<>();
                String lessonTime = currentRow.substring( 1, currentRow.indexOf( "|", 1 ) );
                currentRow = currentRow.substring( currentRow.indexOf( "|", 1 ) ); //cut lesson time from string
                currentRow = currentRow.substring( currentRow.indexOf( "|", 1 ) ); //cut lesson number from string
                data.add( currentRow );

                while( rowNumber < weekDayRows.size() && !IS_ROW_START_WITH_LESSON_TIME.test( weekDayRows.get( rowNumber ) ) )//save data before we find next lesson time
                {
                    String rowToSave = weekDayRows.get( rowNumber++ );//get current row and increased counter
                    data.add( rowToSave.substring( 2 ) );//should remove first 3 symbols, cause it just |||
                }
                dataPerLessonList.add( new RowsPerLesson( lessonTime, data ) );
            }
        }
        return dataPerLessonList;
    }
}
