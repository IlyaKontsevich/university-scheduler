package com.scheduler.lesson.vo;

import com.scheduler.classroom.vo.ClassroomResponseVO;
import com.scheduler.timetable.vo.TimeTableResponseVO;
import lombok.Data;

@Data
public class LessonResponseVO
{
    private Long id;
    private String name;
    private TimeTableResponseVO timeTable;
    private ClassroomResponseVO classroom;
    private Long universityId;
}
