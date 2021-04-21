package com.scheduler.lesson.service;

import com.scheduler.lesson.SubGroupLesson;
import com.scheduler.lesson.SubGroupLessonRepository;
import com.scheduler.lesson.mapper.SubGroupLessonMapper;
import com.scheduler.lesson.vo.SubGroupLessonResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubGroupLessonService
{
    private final SubGroupLessonRepository subGroupLessonRepository;
    private final SubGroupLessonMapper mapper;

    public List<SubGroupLessonResponseVO> getLessonsBySubGroupId( Long subGroupId )
    {
        List<SubGroupLesson> subGroupLessons = subGroupLessonRepository.findBySubGroupId( subGroupId );
        return mapper.toResponseVOs(subGroupLessons);

    }
}
