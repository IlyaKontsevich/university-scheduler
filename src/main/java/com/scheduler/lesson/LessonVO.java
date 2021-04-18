package com.scheduler.lesson;

import com.scheduler.timetable.WeekType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LessonVO
{
    private String name;
    private String classroomName;
    private String lessonTime;
    private WeekType weekType;
    private String dates; //for week type = NONE
    private String teacher;
    private String subGroupNumber;

    public LessonVO( List<LessonVO> lessonVO )
    {
        lessonVO.forEach( lesson -> {
            if( name == null )
            {
                name = lesson.getName();
            }
            if( classroomName == null )
            {
                classroomName = lesson.getClassroomName();
            }
            if( teacher == null )
            {
                teacher = lesson.getTeacher();
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
}
