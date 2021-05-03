package com.scheduler.parser.temporary;

import com.scheduler.parser.TeacherTitle;
import com.scheduler.timetable.WeekType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static com.scheduler.parser.ParserPredicates.*;
import static com.scheduler.parser.ParserStringUtils.*;
import static java.util.Optional.ofNullable;

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
    private List<LessonTmp> lessons = new ArrayList<>();

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
        LessonPerGroup firstRowLesson = parseRow( row1, WeekType.UPPER );
        LessonPerGroup secondRowLesson = parseRow( row2, WeekType.UPPER );
        LessonPerGroup thirdRowLesson = parseRow( row3, WeekType.LOWER );
        LessonPerGroup fourRowLesson = parseRow( row4, WeekType.LOWER );

        List<LessonTmp> upperWeekLessons = merge( firstRowLesson, secondRowLesson );
        List<LessonTmp> lowerWeekLessons = merge( thirdRowLesson, fourRowLesson );

        if( upperWeekLessons.size() + lowerWeekLessons.size() <= 2 )
        {
            LessonTmp upperWeekLesson = upperWeekLessons.size() != 0 ? upperWeekLessons.get( 0 ) : null;
            LessonTmp lowerWeekLesson = lowerWeekLessons.size() != 0 ? lowerWeekLessons.get( 0 ) : null;
            if( weekTypeLessonsDescribeOneLesson( upperWeekLesson, lowerWeekLesson ) )
            {
                LessonTmp lesson = new LessonTmp( new ArrayList<>( Arrays.asList( upperWeekLesson, lowerWeekLesson ) ) );
                setWeekType( lesson, upperWeekLesson, lowerWeekLesson );
                lesson.setSubGroupNumber( "all" );
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

    private void setWeekType( LessonTmp resultLesson, LessonTmp upperWeekLesson, LessonTmp lowerWeekLesson )
    {
        boolean upperWeekLessonIsEmpty = ofNullable( upperWeekLesson ).filter( LessonTmp::isNotEmpty ).isEmpty();
        boolean lowerWeekLessonIsEmpty = ofNullable( lowerWeekLesson ).filter( LessonTmp::isNotEmpty ).isEmpty();
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

    private List<LessonTmp> merge( LessonPerGroup upper, LessonPerGroup lower )
    {
        List<LessonTmp> mergeResult = new ArrayList<>();
        if( upper.isEmpty() && lower.isEmpty() )
        {
            return mergeResult;
        }
        if( upper.isForAllSubgroup() || lower.isForAllSubgroup() )
        {
            mergeResult.add( upper.getSubGroup1() );
            mergeResult.add( upper.getSubGroup2() );
            mergeResult.add( lower.getSubGroup1() );
            mergeResult.add( lower.getSubGroup2() );
            LessonTmp lesson = new LessonTmp( mergeResult );
            lesson.setSubGroupNumber( "all" );
            return new ArrayList<>( List.of( lesson ) );
        }

        List<LessonTmp> firstGroupLessons = new ArrayList<>();
        List<LessonTmp> secondGroupLessons = new ArrayList<>();

        firstGroupLessons.add( upper.getSubGroup1() );
        firstGroupLessons.add( lower.getSubGroup1() );
        LessonTmp firstGroupLesson = new LessonTmp( firstGroupLessons );
        firstGroupLesson.setSubGroupNumber( "0" );

        secondGroupLessons.add( upper.getSubGroup2() );
        secondGroupLessons.add( lower.getSubGroup2() );
        LessonTmp secondGroupLesson = new LessonTmp( secondGroupLessons );
        secondGroupLesson.setSubGroupNumber( "1" );

        if( firstGroupLesson.isNotEmpty() )
        {
            mergeResult.add( firstGroupLesson );
        }
        if( secondGroupLesson.isNotEmpty() )
        {
            mergeResult.add( secondGroupLesson );
        }
        return mergeResult;
    }

    private LessonPerGroup parseRow( List<String> row, WeekType weekType )
    {
        List<String> classroomNames = new ArrayList<>();
        List<String> teachers = new ArrayList<>();
        List<String> lessonNames = new ArrayList<>();

        for( String cell : row )
        {
            if( !cell.isBlank() )
            {
                if( !IS_CELL_CONTAINS_ONLY_SUBJECT_NAME.test( cell ) ) //need to handle Схемотехника
                {
                    cell = setLessonTimeWithAdditionalCheck( cell );
                    cell = addClassroomName( classroomNames, cell );
                    cell = addTeacherIfNeeded( teachers, cell );
                }
                addLessonIfNeeded( lessonNames, cell );
            }
            else
            {
                classroomNames.add( "UNKNOWN" );
                teachers.add( "UNKNOWN" );
                lessonNames.add( "UNKNOWN" );
            }
        }
        Map<Integer, String> teacherNamePerGroupNumber = groupBySubGroupNumber( teachers );
        Map<Integer, String> classroomNameWithGroupNumber = groupBySubGroupNumber( classroomNames );
        Map<Integer, String> lessonNameWithGroupNumber = groupBySubGroupNumber( removeLastDotIfNeed( lessonNames ) );

        List<LessonTmp> lessonVOList = new ArrayList<>();
        for( int i = 0; i < 2; i++ )
        {
            LessonTmp lesson = new LessonTmp();
            lesson.setLessonTime( ofNullable( additionalLessonTime ).orElse( lessonTime ) );
            lesson.setName( lessonNameWithGroupNumber.get( i ) );
            lesson.setTeacher( teacherNamePerGroupNumber.get( i ) );
            lesson.setClassroomName( classroomNameWithGroupNumber.get( i ) );
            lesson.setSubGroupNumber( String.valueOf( i ) );
            lesson.setWeekType( weekType );
            lessonVOList.add( lesson );
        }

        LessonTmp subGroup1Lesson = ofNullable( lessonVOList.get( 0 ) )
            .filter( LessonTmp::isNotEmpty )
            .orElse( null );
        LessonTmp subGroup2Lesson = ofNullable( lessonVOList.get( 1 ) )
            .filter( LessonTmp::isNotEmpty )
            .orElse( null );

        return new LessonPerGroup( subGroup1Lesson, subGroup2Lesson, subGroupLessonsDescribeOneLesson( subGroup1Lesson, subGroup2Lesson ) );
    }

    private List<String> removeLastDotIfNeed( List<String> lessonNames )
    {
        return lessonNames.stream().map( name -> {
            if( ".".equals( name.substring( name.length() - 1 ) ) )
            {
                return name.substring( 0, name.length() - 1 );
            }
            return name;
        } ).collect( Collectors.toList() );
    }

    private boolean subGroupLessonsDescribeOneLesson( LessonTmp subGroup1Lesson, LessonTmp subGroup2Lesson )
    {
        return subGroup1Lesson != null && subGroup2Lesson != null
               && ( ofNullable( subGroup1Lesson.getTeacher() ).filter( s -> s.equals( subGroup2Lesson.getTeacher() ) ).isPresent()
                    || ofNullable( subGroup1Lesson.getName() ).filter( s -> s.equals( subGroup2Lesson.getName() ) ).isPresent() );
    }

    private boolean weekTypeLessonsDescribeOneLesson( LessonTmp upperWeekLesson, LessonTmp lowerWeekLesson )
    {
        return ofNullable( upperWeekLesson )
                   .map( LessonTmp::getName )
                   .filter( name -> name.equals( ofNullable( lowerWeekLesson ).map( LessonTmp::getName ).orElse( name ) ) )
                   .isPresent() ||
               ofNullable( lowerWeekLesson )
                   .map( LessonTmp::getName )
                   .filter( name -> name.equals( ofNullable( upperWeekLesson ).map( LessonTmp::getName ).orElse( name ) ) )
                   .isPresent();
    }

    private String addTeacherIfNeeded( List<String> teachers, String cell )
    {
        cell = checkForPlusAndTrim( cell );
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
                else if( IS_CELL_CONTAINS_TEACHER_TITLE.test( cell ) )//need to handle cell like this "Методы и средства защиты информации"
                {
                    String finalCell = cell;
                    int cutSize = Arrays.stream( TeacherTitle.values() )
                                        .filter( title -> finalCell.contains( title.getRussianValue() ) )
                                        .map( TeacherTitle::getSize )
                                        .findFirst()
                                        .orElse( 0 );
                    teacher = cell.trim().substring( 0, cutSize );
                    cell = cell.trim().substring( cutSize );
                    cutSize = cell.contains( " " ) ? cell.indexOf( " " ) : cell.length() - 1;
                    teacher = teacher + cell.trim().substring( 0, cutSize );
                    cell = cell.trim().substring( cutSize );
                }
                else if( IS_CELL_START_WITH_SURNAME.test( cell ) )
                {
                    teacher = cell.trim().substring( 0, cell.indexOf( " " ) );
                    cell = cell.trim().substring( cell.indexOf( " " ) );
                }
                if( IS_CELL_START_WITH_INITIALS.test( cell.trim() ) ) // initials can be only like 
                {
                    teacher = teacher + cell.substring( 0, 4 ); //4 cause М.П. - 4 symbols Лапич СВ
                    cell = cell.trim().substring( 4 );
                }
                else if( cell.trim().matches( START_WITH_INITIALS_WITH_ONE_LETTER_REGEX ) )
                {
                    int cutSize = !cell.contains( "." ) ? cell.indexOf( "." ) : cell.length() - 1;
                    teacher = teacher + cell.trim().substring( 0, cutSize );
                    cell = cell.trim().substring( cutSize );
                }
            }
            else if( IS_CELL_CONTAINS_TEACHER_TITLE.test( cell ) || IS_CELL_START_WITH_SURNAME.test( cell ) )
            {
                teacher = cell; //cause if do not have space than contains only teacher name
                if( teacher.contains( "." ) && !IS_CELL_CONTAINS_TEACHER_TITLE.test( cell ) )//handle Арх.ЭВМ
                {
                    return cell;
                }
                cell = "";
            }
            teachers.add( Objects.requireNonNullElse( teacher, "UNKNOWN" ) );
            return cell;
        }
        return cell;
    }

    private String checkForPlusAndTrim( String cell ) //handle case with ".+MC-4"
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

    private void addLessonIfNeeded( List<String> lessons, String cell ) //lesson was added in last period cause of this just add cell to collection
    {
        if( !StringUtils.isEmpty( cell ) )
        {
            lessons.add( StringUtils.trim( cell ) );
        }
        else
        {
            lessons.add( "UNKNOWN" );
        }
    }

    private String addClassroomName( List<String> classroomNames, String cell )
    {
        cell = checkForPlusAndTrim( cell );
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
        classroomNames.add( "UNKNOWN" );
        return cell;
    }

    private String setLessonTimeWithAdditionalCheck( String cell )
    {
        cell = checkForPlusAndTrim( cell );
        if( IS_CELL_START_WITH_ADDITIONAL_TIME.test( cell ) )
        {
            if( cell.contains( " " ) )
            {
                additionalLessonTime = cell.substring( 0, cell.indexOf( " " ) );
                return cell.substring( cell.indexOf( " " ) );
            }
            additionalLessonTime = cell.substring( 0, ADDITIONAL_LESSON_TIME_SIZE );
            return cell.substring( ADDITIONAL_LESSON_TIME_SIZE );
        }
        else
        {
            return cell;
        }
    }

    public static <T> Map<Integer, T> groupBySubGroupNumber( List<T> list )
    {
        Map<Integer, T> valueByGroupNumber = new HashMap<>();
        for( int i = 0, listSize = list.size(); i < listSize; i++ )
        {
            T value = list.get( i );
            if( value != "UNKNOWN" )
            {
                valueByGroupNumber.put( i == 0 || i == 1 ? 0 : 1, value );
            }
        }
        return valueByGroupNumber;
    }

    private void handleNoneWeek()
    {
        lessons.forEach( lessonVO -> {
            if( lessonVO.getName() != null && lessonVO.getName().matches( STRING_WITH_NUMBERS_REGEX ) )
            {
                String lessonName = lessonVO.getName();
                int indexOfNumber = getMinIndexOfNumber( lessonName );
                String dates = lessonName.substring( indexOfNumber );
                lessonName = lessonName.substring( 0, indexOfNumber ).replace( ",", "" ).trim();
                if( lessonName.endsWith( "по" ) )
                {
                    lessonVO.setName( lessonName.substring( 0, lessonName.indexOf( "по" ) ) );
                    lessonVO.setDates( "по " + dates );
                }
                else
                {
                    lessonVO.setName( lessonName );
                    lessonVO.setDates( dates );
                }
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
}
