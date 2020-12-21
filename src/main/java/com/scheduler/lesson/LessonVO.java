package com.scheduler.lesson;

import com.scheduler.timetable.WeekType;
import lombok.Data;

@Data
public class LessonVO
{
    private String name;
    private String classroomName;
    private String lessonTime;
    private WeekType weekType;
    private String dates; //for week type = NONE
    private String teacher;
    private String subGroupNumber;


}
