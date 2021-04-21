package com.scheduler.teacher.mapper;

import com.scheduler.common.mapper.EntityToResponseVOMapper;
import com.scheduler.lesson.TeacherLesson;
import com.scheduler.lesson.mapper.LessonMapper;
import com.scheduler.teacher.Teacher;
import com.scheduler.teacher.vo.TeacherResponseVO;
import liquibase.util.BooleanUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TeacherMapper implements EntityToResponseVOMapper<Teacher, TeacherResponseVO>
{
    private final ModelMapper modelMapper = new ModelMapper();
    private final LessonMapper lessonMapper;

    public TeacherMapper( LessonMapper lessonMapper )
    {
        this.lessonMapper = lessonMapper;

        modelMapper.addMappings( new PropertyMap<Teacher, TeacherResponseVO>()
        {
            @Override
            protected void configure()
            {
                skip().setLessons( null );
            }
        } );
    }

    @Override
    public TeacherResponseVO toResponseVO( Teacher teacher, Object... args )
    {
        final Boolean isForStudentView = args.length == 0;

        TeacherResponseVO teacherResponseVO = modelMapper.map( teacher, TeacherResponseVO.class );//todo
        if( !BooleanUtils.isTrue( isForStudentView ) )
        {
            teacherResponseVO.setLessons( lessonMapper.toResponseVOs( teacher.getLessons()
                                                                             .stream()
                                                                             .map( TeacherLesson::getLesson )
                                                                             .collect( Collectors.toList() ) ) );
        }

        return teacherResponseVO;
    }
}
