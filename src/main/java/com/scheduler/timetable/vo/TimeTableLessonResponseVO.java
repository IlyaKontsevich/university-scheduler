package com.scheduler.timetable.vo;

import lombok.Data;

@Data
public class TimeTableLessonResponseVO
{
    private Long id;
    private Integer number;
    private String startTime;
    private String endTime;
    private Long universityId;
}
