package com.scheduler.parser.temporary;

import com.scheduler.lesson.LessonVO;
import com.scheduler.timetable.WeekType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.*;

import static com.scheduler.parser.ParserPredicates.*;
import static com.scheduler.parser.ParserStringUtils.*;

@Data
@NoArgsConstructor
public class DataPerLessonTime
{
    @NotNull
    private String lessonTime;
    private String additionalLessonTime;
    @NotNull
    private List<String> row1 = new ArrayList<>();
    @NotNull
    private List<String> row2 = new ArrayList<>();
    @NotNull
    private List<String> row3 = new ArrayList<>();
    @NotNull
    private List<String> row4 = new ArrayList<>();
    private boolean isParsed;
    private List<LessonVO> lessons = new ArrayList<>();

    @Override
    public String toString()
    {
        return "Lessons= " + lessons;
    }

    public void setRow( int number, List<String> splitRow )
    {
        if( number == 0 )
        {
            this.row1 = splitRow;
        }
        if( number == 1 )
        {
            this.row2 = splitRow;
        }
        if( number == 2 )
        {
            this.row3 = splitRow;
        }
        if( number == 3 )
        {
            this.row4 = splitRow;
        }
    }

    public void parse()
    {
        List<LessonVO> lessonsFromFirstRow = parseRow( row1, WeekType.UPPER );
        List<LessonVO> lessonsFromSecondRow = parseRow( row2, WeekType.UPPER );
        List<LessonVO> lessonsFromThirdRow = parseRow( row3, WeekType.LOWER );
        List<LessonVO> lessonsFromFourRow = parseRow( row4, WeekType.LOWER );

        List<LessonVO> upperWeekLessons = merge( lessonsFromFirstRow, lessonsFromSecondRow );
        List<LessonVO> lowerWeekLessons = merge( lessonsFromThirdRow, lessonsFromFourRow );

        if( upperWeekLessons.size() + lowerWeekLessons.size() <= 2 )
        {
            LessonVO upperWeekLesson = upperWeekLessons.get( 0 );
            LessonVO lowerWeekLesson = lowerWeekLessons.get( 0 );
            if( Optional.ofNullable( upperWeekLesson )
                        .map( LessonVO::getName )
                        .filter( name -> name.equals( Optional.ofNullable( lowerWeekLesson ).map( LessonVO::getName ).orElse( name ) ) )
                        .isPresent() ||
                Optional.ofNullable( lowerWeekLesson )
                        .map( LessonVO::getName )
                        .filter( name -> name.equals( Optional.ofNullable( upperWeekLesson ).map( LessonVO::getName ).orElse( name ) ) )
                        .isPresent() )
            {
                upperWeekLessons.addAll( lowerWeekLessons );
                LessonVO lesson = new LessonVO( upperWeekLessons );
                setWeekType( lesson, upperWeekLesson, lowerWeekLesson );
                lessons.add( lesson );
                handleNoneWeek();
                setParsed( true );
                return;
            }
        }
        lessons.addAll( upperWeekLessons );
        lessons.addAll( lowerWeekLessons );
        handleNoneWeek();
        setParsed( true );
    }

    private void handleNoneWeek()
    {
        lessons.forEach( lessonVO -> {
            if( lessonVO.getName() != null && lessonVO.getName().matches( STRING_WITH_NUMBERS_REGEX ) )
            {
                String lessonName = lessonVO.getName();
                int indexOfNumber = getMinIndexOfNumber( lessonName );
                String dates = lessonName.substring( indexOfNumber );
                lessonVO.setName( lessonName.substring( 0, indexOfNumber ) );
                lessonVO.setDates( dates );
                lessonVO.setWeekType( WeekType.NONE );
            }
        } );
    }

    private int getMinIndexOfNumber( String lessonName )
    {
        Set<Integer> listOfIndexes = new HashSet<>();
        listOfIndexes.add( lessonName.indexOf( "0" ) );
        listOfIndexes.add( lessonName.indexOf( "1" ) );
        listOfIndexes.add( lessonName.indexOf( "2" ) );
        listOfIndexes.add( lessonName.indexOf( "3" ) );
        listOfIndexes.add( lessonName.indexOf( "4" ) );
        listOfIndexes.add( lessonName.indexOf( "5" ) );
        listOfIndexes.add( lessonName.indexOf( "6" ) );
        listOfIndexes.add( lessonName.indexOf( "7" ) );
        listOfIndexes.add( lessonName.indexOf( "8" ) );
        listOfIndexes.add( lessonName.indexOf( "9" ) );
        listOfIndexes.remove( -1 );
        return Collections.min( listOfIndexes );
    }

    private void setWeekType( LessonVO resultLesson, LessonVO upperWeekLesson, LessonVO lowerWeekLesson )
    {
        boolean upperWeekLessonIsEmpty = Optional.ofNullable( upperWeekLesson )
                                                 .filter( lesson -> lesson.getTeacher() != null
                                                                    || lesson.getName() != null
                                                                    || lesson.getClassroomName() != null )
                                                 .isEmpty();
        boolean lowerWeekLessonIsEmpty = Optional.ofNullable( lowerWeekLesson )
                                                 .filter( lesson -> lesson.getTeacher() != null
                                                                    || lesson.getName() != null
                                                                    || lesson.getClassroomName() != null )
                                                 .isEmpty();
        if( !upperWeekLessonIsEmpty && !lowerWeekLessonIsEmpty )
        {
            resultLesson.setWeekType( WeekType.ALL );
            return;
        }
        if( upperWeekLessonIsEmpty )
        {
            resultLesson.setWeekType( WeekType.LOWER );
        }
        if( lowerWeekLessonIsEmpty )
        {
            resultLesson.setWeekType( WeekType.UPPER );
        }
    }

    private List<LessonVO> merge( List<LessonVO> lessons1, List<LessonVO> lessons2 )
    {
        List<LessonVO> mergeResult = new ArrayList<>();
        if( lessons1.isEmpty() && lessons2.isEmpty() )
        {
            return mergeResult;
        }
        if( lessons1.size() + lessons2.size() <= 2 )
        {
            lessons1.addAll( lessons2 );
            LessonVO lesson = new LessonVO( lessons1 );
            lesson.setSubGroupNumber( "all" );
            mergeResult.add( lesson );
            return mergeResult;
        }
        List<LessonVO> firstGroupLessons = new ArrayList<>();
        List<LessonVO> secondGroupLessons = new ArrayList<>();

        if( lessons1.size() >= 1 )
        {
            firstGroupLessons.add( lessons1.get( 0 ) );
        }
        if( lessons2.size() >= 1 )
        {
            firstGroupLessons.add( lessons2.get( 0 ) );
        }
        LessonVO firstGroupLesson = new LessonVO( firstGroupLessons );
        firstGroupLesson.setSubGroupNumber( "0" );

        if( lessons1.size() > 1 )
        {
            secondGroupLessons.add( lessons1.get( 1 ) );
        }
        if( lessons2.size() > 1 )
        {
            secondGroupLessons.add( lessons2.get( 1 ) );
        }
        LessonVO secondGroupLesson = new LessonVO( secondGroupLessons );
        secondGroupLesson.setSubGroupNumber( "1" );

        mergeResult.add( firstGroupLesson );
        mergeResult.add( secondGroupLesson );
        return mergeResult;
    }

    private List<LessonVO> parseRow( List<String> row, WeekType weekType )
    {
        List<LessonVO> lessonVOList = new ArrayList<>();
        List<String> classroomNames = new ArrayList<>();
        List<String> teachers = new ArrayList<>();
        List<String> lessonNames = new ArrayList<>();
        LessonVO lessonVO = new LessonVO();

        for( String cell : row )
        {
            if( !cell.isBlank() )
            {
                if( cell.contains( " " ) || IS_CELL_CONTAINS_TEACHER_TITLE.test( cell )
                    || cell.matches( STRING_WITH_NUMBERS_REGEX ) ) //need to handle Схемотехника
                {
                    cell = setLessonTime( lessonVO, cell );
                    cell = addClassroomNameIfNeeded( classroomNames, cell );
                    cell = addTeacherIfNeeded( teachers, cell );
                }
                addLessonIfNeeded( lessonNames, cell );
            }
        }
        lessonVOList.add( lessonVO );

        Map<Integer, String> teacherNamePerGroupNumber = new HashMap<>();
        Map<Integer, String> classroomNameWithGroupNumber = new HashMap<>();
        Map<Integer, String> lessonNameWithGroupNumber = new HashMap<>();
        if( !teachers.isEmpty() )
        {
            teacherNamePerGroupNumber = groupBySubGroupNumber( teachers );
            addNewLessonIfNecessary( lessonVOList, teacherNamePerGroupNumber.size() > 1 );
        }
        if( !classroomNames.isEmpty() )
        {
            classroomNameWithGroupNumber = groupBySubGroupNumber( classroomNames );
            addNewLessonIfNecessary( lessonVOList, classroomNameWithGroupNumber.size() > 1 );
        }
        if( !lessonNames.isEmpty() )
        {
            lessonNameWithGroupNumber = groupBySubGroupNumber( lessonNames );
            addNewLessonIfNecessary( lessonVOList, lessonNameWithGroupNumber.size() > 1 );
        }
        for( int i = 0, lessonVOListSize = lessonVOList.size(); i < lessonVOListSize; i++ )
        {
            LessonVO lesson = lessonVOList.get( i );
            lesson.setLessonTime( Optional.ofNullable( additionalLessonTime ).orElse( lessonTime ) );
            lesson.setName( lessonNameWithGroupNumber.get( i ) );
            lesson.setTeacher( teacherNamePerGroupNumber.get( i ) );
            lesson.setClassroomName( classroomNameWithGroupNumber.get( i ) );
            lesson.setSubGroupNumber( String.valueOf( i ) );
            lesson.setWeekType( weekType );
        }
        return lessonVOList;
    }

    private String addTeacherIfNeeded( List<String> teachers, String cell )
    {
        cell = checkForPlussAndTrim( cell );
        cell = cell.trim();
        if( !cell.isBlank() )
        {
            String teacher = null;
            if( cell.contains( " " ) ) //check row like this "ст.пр.Ник.-Ртищева М.С." or "доц.Дереченник, проф.Поляков " or "Лапич МЭиМСхТ "
            {
                if( cell.contains( ", " ) ) //check row like this "доц.Дереченник, проф.Поляков"
                {
                    teacher = cell.substring( 0, cell.indexOf( "," ) );
                    cell = cell.substring( cell.indexOf( "," ) );
                    teacher = teacher + cell.substring( cell.indexOf( " " ) - 1 );
                    cell = "";
                }
                else if( IS_CELL_CONTAINS_TEACHER_TITLE.test( cell )
                         || cell.matches( REGEX_TWO_FIRST_WORD_WITH_CAPITAL_LETTERS ) )//need to handle cell like this "Методы и средства защиты информации"
                {
                    teacher = cell.substring( 0, cell.indexOf( " " ) );
                    if( !IS_CELL_CONTAINS_TEACHER_TITLE.test( teacher ) && !IS_CELL_CONTAINS_SURNAME.test( teacher ) ) // can be like "Прикл.система обраб.данных"
                    {
                        return cell;
                    }
                    cell = cell.substring( cell.indexOf( " " ) );
                }
                if( IS_CELL_START_WITH_INITIALS.test( cell.trim() ) )
                {
                    teacher = teacher + cell;
                    cell = cell.trim().substring( 4 ); //4 cause М.П. - 4 symbols 
                }
            }
            else if( IS_CELL_CONTAINS_TEACHER_TITLE.test( cell ) || IS_CELL_CONTAINS_SURNAME.test( cell ) )
            {
                teacher = cell;
                if( teacher.contains( "." ) && !IS_CELL_CONTAINS_TEACHER_TITLE.test( cell ) ) //handle Арх.ЭВМ
                {
                    return cell;
                }
                cell = cell.substring( teacher.length() );
            }
            if( teacher != null )
            {
                teachers.add( teacher );
            }
            return cell;
        }
        return cell;
    }

    private String checkForPlussAndTrim( String cell )
    {
        cell = cell.trim();
        if( cell.startsWith( ".+" ) )
        {
            if( cell.startsWith( ".+ " ) ) //handle case with ".+ MC-4"
            {
                cell = cell.substring( cell.indexOf( " " ) + 1 );
            }
            return cell.contains( " " )
                   ? cell.substring( cell.indexOf( " " ) )
                   : "";
        }
        return cell;
    }

    private void addLessonIfNeeded( List<String> lessons, String cell )
    {
        cell = checkForPlussAndTrim( cell );
        if( !StringUtils.isEmpty( cell ) )
        {
            lessons.add( StringUtils.trim( cell ) );
        }
    }

    private String addClassroomNameIfNeeded( List<String> classroomNames, String cell )
    {
        cell = checkForPlussAndTrim( cell );
        for( Map.Entry<Integer, String> entry : SIZE_OF_AND_CLASSROOM_NAME_REGEXES_WITH_LETTERS.entrySet() )
        {
            Integer size = entry.getKey();
            String regex = entry.getValue();
            if( cell.matches( regex ) )
            {
                classroomNames.add( cell.substring( 0, size ) );
                return cell.substring( size );
            }
        }
        for( Map.Entry<Integer, String> entry : SIZE_OF_AND_CLASSROOM_NAME_REGEXES.entrySet() )
        {
            Integer size = entry.getKey();
            String regex = entry.getValue();
            if( cell.matches( regex ) )
            {
                classroomNames.add( cell.substring( 0, size ) );
                return cell.substring( size );
            }
        }
        return cell;
    }

    private String setLessonTime( LessonVO lessonVO, String cell )
    {
        cell = checkForPlussAndTrim( cell );
        if( IS_CELL_START_WITH_ADDITIONAL_TIME.test( cell ) )
        {
            if( cell.contains( " " ) )
            {
                lessonVO.setLessonTime( cell.substring( 0, cell.indexOf( " " ) ) );
                additionalLessonTime = lessonVO.getLessonTime();
                return cell.substring( cell.indexOf( " " ) );
            }
            lessonVO.setLessonTime( cell );
            additionalLessonTime = cell;
            return "";
        }
        else
        {
            lessonVO.setLessonTime( lessonTime );
            return cell;
        }
    }

    private void addNewLessonIfNecessary( List<LessonVO> lessons, boolean subGroupMoreThenOne )
    {
        if( lessons.size() == 1 && subGroupMoreThenOne )
        {
            lessons.add( new LessonVO() );
        }
    }

    public static <T> Map<Integer, T> groupBySubGroupNumber( List<T> list )
    {
        Map<Integer, T> valueByGroupNumber = new HashMap<>();
        for( int i = 0, listSize = list.size(); i < listSize; i++ )
        {
            T value = list.get( i );
            if( !valueByGroupNumber.containsValue( value ) )
            {
                valueByGroupNumber.put( i == 0 || i == 1 ? 0 : 1, value );
            }
        }
        return valueByGroupNumber;
    }

}
