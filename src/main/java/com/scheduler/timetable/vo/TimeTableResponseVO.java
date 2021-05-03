package com.scheduler.timetable.vo;

import com.scheduler.timetable.WeekDay;
import com.scheduler.timetable.WeekType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TimeTableResponseVO
{
    private Long id;
    private TimeTableLessonResponseVO timeTableLesson;
    private WeekType week;
    private WeekDay weekDay;
    private String dates;
    private Long universityId;
}
