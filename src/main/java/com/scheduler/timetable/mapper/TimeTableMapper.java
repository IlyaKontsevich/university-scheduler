package com.scheduler.timetable.mapper;

import com.scheduler.common.mapper.EntityToResponseVOMapper;
import com.scheduler.timetable.TimeTable;
import com.scheduler.timetable.vo.TimeTableResponseVO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TimeTableMapper implements EntityToResponseVOMapper<TimeTable, TimeTableResponseVO>
{
    private final ModelMapper modelMapper = new ModelMapper();

    private TimeTableLessonMapper timeTableLessonMapper;

    public TimeTableMapper(TimeTableLessonMapper timeTableLessonMapper){
        this.timeTableLessonMapper = timeTableLessonMapper;
    }

    @Override
    public TimeTableResponseVO toResponseVO( TimeTable timeTable, Object... args )
    {
        TimeTableResponseVO timeTableResponseVO = modelMapper.map( timeTable, TimeTableResponseVO.class );
        timeTableResponseVO.setTimeTableLesson( timeTableLessonMapper.toResponseVO( timeTable.getTimeTableLesson() ) );
        return timeTableResponseVO;
    }
}
