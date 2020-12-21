package com.scheduler.parser.temporary;

import com.scheduler.timetable.WeekDay;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
public class DataPerWeekDay
{
    @NonNull
    private WeekDay weekDay;
    @NonNull
    private List<DataPerLessonTime> dataPerLessonList;

    @Override
    public String toString()
    {
        return "\nWeek Day = " + weekDay + "\n" + dataPerLessonList;
    }
}
