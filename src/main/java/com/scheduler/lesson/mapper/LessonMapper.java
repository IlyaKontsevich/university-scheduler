package com.scheduler.lesson.mapper;

import com.scheduler.common.mapper.EntityToResponseVOMapper;
import com.scheduler.lesson.Lesson;
import com.scheduler.lesson.vo.LessonResponseVO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper implements EntityToResponseVOMapper<Lesson, LessonResponseVO>
{
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public LessonResponseVO toResponseVO( Lesson lesson, Object... args )
    {
        LessonResponseVO responseVO = modelMapper.map( lesson, LessonResponseVO.class );
        return responseVO; //todo
    }
}
