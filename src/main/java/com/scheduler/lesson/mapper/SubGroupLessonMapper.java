package com.scheduler.lesson.mapper;

import com.scheduler.common.mapper.EntityToResponseVOMapper;
import com.scheduler.lesson.SubGroupLesson;
import com.scheduler.lesson.vo.SubGroupLessonResponseVO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SubGroupLessonMapper implements EntityToResponseVOMapper<SubGroupLesson, SubGroupLessonResponseVO>
{
    private final ModelMapper modelMapper = new ModelMapper();
    private final LessonMapper lessonMapper;

    @Override
    public SubGroupLessonResponseVO toResponseVO( SubGroupLesson subGroupLesson, Object... args )
    {
        SubGroupLessonResponseVO responseVO = modelMapper.map( subGroupLesson, SubGroupLessonResponseVO.class );

        responseVO.setLesson(  );
        responseVO.setTeacher(  );

        return responseVO;
    }
}
