package com.scheduler.lesson.mapper;

import com.scheduler.lesson.SubGroupLesson;
import com.scheduler.lesson.view.StudentLessonView;
import com.scheduler.teacher.mapper.TeacherMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StudentLessonViewMapper
{
    private final TeacherMapper teacherMapper;
    private final LessonMapper lessonMapper;

    public StudentLessonView toResponseVO( SubGroupLesson subGroupLesson )
    {
        StudentLessonView studentLessonView = new StudentLessonView();
        studentLessonView.setLesson( lessonMapper.toResponseVO( subGroupLesson.getLesson() ) );
        studentLessonView.setTeacher( teacherMapper.toResponseVO( subGroupLesson.getTeacher() ) );
        return studentLessonView;
    }

    public List<StudentLessonView> toResponseVOs( List<SubGroupLesson> subGroupLessons, Object... args )
    {
        return subGroupLessons.stream()
                              .map( this::toResponseVO )
                              .collect( Collectors.toList());
    }
}
