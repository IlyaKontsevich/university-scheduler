package com.scheduler.parser.temporary;

import com.scheduler.lesson.LessonVO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.scheduler.parser.ParserPredicates.IS_CELL_START_WITH_ADDITIONAL_TIME;
import static com.scheduler.parser.ParserPredicates.IS_CELL_START_WITH_INITIALS;
import static com.scheduler.parser.ParserStringUtils.SIZE_OF_AND_CLASSROOM_NAME_REGEXES;

@Data
@NoArgsConstructor
public class DataPerLessonTime
{
    @NonNull
    private String lessonTime;
    @NotNull
    private List<String> row1;
    @NotNull
    private List<String> row2;
    @NotNull
    private List<String> row3;
    @NotNull
    private List<String> row4;
    private boolean isParsed;
    private List<LessonVO> lessons;

    @Override
    public String toString()
    {
        return "\nLessonTime= " + lessonTime +
               "\n row1=" + row1 +
               "\n row2=" + row2 +
               "\n row3=" + row3 +
               "\n row4=" + row4 +
               "\n isParsed=" + isParsed;
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
        List<String> classroomNames = new ArrayList<>();
        List<String> teachers = new ArrayList<>();
        List<String> lessons = new ArrayList<>();
        LessonVO lessonVO = new LessonVO();

        for( int i = 0; i < row1.size(); i++ )
        {
            String cell = row1.get( i );
            if( !cell.isBlank() )
            {
                cell = setLessonTime( lessonVO, cell );
                cell = addClassroomNameIfNeeded( classroomNames, cell );
                cell = addTeacherIfNeeded( teachers, cell );
                cell = addTeacherIfNeeded( classroomNames, cell );
            }
        }

    }

    private String addTeacherIfNeeded( List<String> teachers, String cell )
    {
        cell = checkForPlussAndTrim( cell );
        if( !cell.isBlank() )
        {
            String teacher;
            if( cell.contains( " " ) )
            {
                if( cell.contains( ", " ) )
                {
                    teacher = cell.substring( 0, cell.indexOf( "," ) );
                    cell = cell.substring( cell.indexOf( "," ) + 1 );
                    addTeacherIfNeeded( teachers, cell );
                }
                else
                {
                    teacher = cell.substring( 0, cell.indexOf( " " ) );
                    cell = cell.substring( cell.indexOf( " " ) );
                }
                teacher = addInitialIfNeeded( teacher, cell );
            }
            else
            {
                teacher = cell;
                cell = "";
            }
            teachers.add( teacher );
            return cell;
        }
        return cell;
    }

    private String checkForPlussAndTrim( String cell ) //todo temporery
    {
        cell = cell.trim();
        if( cell.startsWith( ".+" ) )
        {
            return cell.contains( " " )
                   ? cell.substring( cell.indexOf( " " ) )
                   : "";
        }
        return cell;
    }

    private String addInitialIfNeeded( String teacher, String cell )
    {
        cell = checkForPlussAndTrim( cell );
        if( IS_CELL_START_WITH_INITIALS.test( cell ) )
        {
            return teacher + cell;
        }
        return cell;
    }

    private void addLessonIfNeeded( String teacher, String cell )
    {
        checkForPlussAndTrim( cell );
        if( IS_CELL_START_WITH_INITIALS.test( cell ) )
        {
            teacher = teacher + cell;
        }
    }

    private String addClassroomNameIfNeeded( List<String> classroomNames, String cell )
    {
        cell = checkForPlussAndTrim( cell );
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
                return cell.substring( cell.indexOf( " " ) );
            }
            lessonVO.setLessonTime( cell );
            return "";
        }
        else
        {
            lessonVO.setLessonTime( lessonTime );
            return cell;
        }
    }

    public boolean addIfAndCut( List<String> list, String value, Predicate<String> predicate, Function<String, String> cutOperation )
    {
        if( predicate.test( value ) )
        {
            value = cutOperation.apply( value );
            list.add( value );
            return true;
        }
        return false;
    }

}
