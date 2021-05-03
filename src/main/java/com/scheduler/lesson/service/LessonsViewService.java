package com.scheduler.lesson.service;

import com.scheduler.common.domain.NotFoundResponseError;
import com.scheduler.common.exception.ErrorResponse;
import com.scheduler.lesson.SubGroupLesson;
import com.scheduler.lesson.SubGroupLessonRepository;
import com.scheduler.lesson.mapper.SubGroupLessonViewMapper;
import com.scheduler.lesson.view.SubGroupLessonsView;
import com.scheduler.teacher.Teacher;
import com.scheduler.teacher.TeacherRepository;
import com.scheduler.teacher.mapper.TeacherMapper;
import com.scheduler.teacher.vo.TeacherResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonsViewService
{
    private final SubGroupLessonRepository subGroupLessonRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final SubGroupLessonViewMapper mapper;

    @Transactional
    public SubGroupLessonsView getSubGroupLessonsViewBySubGroupId( String name, Long number )
    {
        List<SubGroupLesson> subGroupLessons = subGroupLessonRepository.findByGroupNameAndSubGroupNumber( name, number );
        return mapper.toResponseVO( subGroupLessons );
    }

    @Transactional
    public TeacherResponseVO getTeacherLessonsByTeacherName( String name )
    {
        Optional
            .ofNullable( teacherRepository.findByName( name ) )
            .orElseThrow( () -> new ErrorResponse( new NotFoundResponseError( Teacher.class, name ) ) );

        List<TeacherResponseVO> responses = teacherMapper.toResponseVOs( teacherRepository.findByNameContaining( name ), true );
        return responses.stream()
                        .reduce( ( teacherR1, teacherR2 ) -> {
                            teacherR1.getLessons().addAll( teacherR2.getLessons() );
                            return teacherR1;
                        } )
                        .get();
    }
}
