package com.scheduler.timetable.mapper;

import com.scheduler.common.mapper.EntityToResponseVOMapper;
import com.scheduler.timetable.TimeTableLesson;
import com.scheduler.timetable.vo.TimeTableLessonResponseVO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TimeTableLessonMapper implements EntityToResponseVOMapper<TimeTableLesson, TimeTableLessonResponseVO>
{
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public TimeTableLessonResponseVO toResponseVO( TimeTableLesson timeTableLesson, Object... args )
    {
        return modelMapper.map( timeTableLesson, TimeTableLessonResponseVO.class );
    }
}
