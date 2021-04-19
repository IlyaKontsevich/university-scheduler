package com.scheduler.parser.temporary;

import com.scheduler.timetable.WeekType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
@NoArgsConstructor
public class LessonTmp
{
    private String name;
    private String classroomName;
    private String lessonTime;
    private WeekType weekType;
    private String dates; //for week type = NONE
    private String teacher;
    private String subGroupNumber;

    public LessonTmp( List<LessonTmp> lessonVO )
    {
        lessonVO.forEach( lesson -> {
            if( lesson == null )
            {
                return;
            }
            if( name == null && !StringUtils.isBlank( lesson.getName() ) )
            {
                name = lesson.getName();
            }
            else if( !StringUtils.isBlank( lesson.getName() ) && !lesson.getName().contains( name ) )
            {
                name = name + ", " + lesson.getName();
            }

            if( classroomName == null )
            {
                classroomName = lesson.getClassroomName();
            }

            if( teacher == null && !StringUtils.isBlank( lesson.getTeacher() ) )
            {
                teacher = lesson.getTeacher();
            }
            else if( !StringUtils.isBlank( lesson.getTeacher() ) 
                     && !StringUtils.contains( teacher, lesson.getTeacher() ) )
            {
                teacher = teacher + ", " + lesson.getTeacher();
            }

            if( weekType == null )
            {
                weekType = lesson.getWeekType();
            }
            if( lessonTime == null )
            {
                lessonTime = lesson.getLessonTime();
            }
        } );
    }

    public boolean isNotEmpty()
    {
        return !( name == null && classroomName == null && teacher == null );
    }
}
