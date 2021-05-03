package com.scheduler.lesson.mapper;

import com.scheduler.classroom.mapper.ClassroomMapper;
import com.scheduler.common.mapper.EntityToResponseVOMapper;
import com.scheduler.lesson.Lesson;
import com.scheduler.lesson.vo.LessonResponseVO;
import com.scheduler.timetable.mapper.TimeTableMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper implements EntityToResponseVOMapper<Lesson, LessonResponseVO>
{
    private final ModelMapper modelMapper = new ModelMapper();
    private ClassroomMapper classRoomMapper;
    private TimeTableMapper timeTableMapper;

    public LessonMapper( ClassroomMapper classRoomMapper, TimeTableMapper timeTableMapper )
    {
        this.classRoomMapper = classRoomMapper;
        this.timeTableMapper = timeTableMapper;
    }

    @Override
    public LessonResponseVO toResponseVO( Lesson lesson, Object... args )
    {
        LessonResponseVO responseVO = modelMapper.map( lesson, LessonResponseVO.class );
        responseVO.setClassroom( classRoomMapper.toResponseVO( lesson.getClassroom() ) );
        responseVO.setTimeTable( timeTableMapper.toResponseVO( lesson.getTimeTable() ) );
        return responseVO;
    }
}
