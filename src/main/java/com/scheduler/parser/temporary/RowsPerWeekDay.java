package com.scheduler.parser.temporary;

import com.scheduler.timetable.WeekDay;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class RowsPerWeekDay
{
    @NonNull
    private WeekDay weekDay;
    @NonNull
    private List<RowsPerLesson> dataPerLessonList;
}
