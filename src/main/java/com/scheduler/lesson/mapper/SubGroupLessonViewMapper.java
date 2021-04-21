package com.scheduler.lesson.mapper;

import com.scheduler.group.subgroup.mapper.SubGroupMapper;
import com.scheduler.lesson.SubGroupLesson;
import com.scheduler.lesson.view.SubGroupLessonsView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SubGroupLessonViewMapper
{
    private final StudentLessonViewMapper studentLessonViewMapper;
    private final SubGroupMapper subGroupMapper;

    public SubGroupLessonsView toResponseVO( List<SubGroupLesson> subGroupLessons )
    {
        SubGroupLessonsView subGroupLessonsView = new SubGroupLessonsView();
        subGroupLessonsView.setLessons( studentLessonViewMapper.toResponseVOs( subGroupLessons ) );
        subGroupLessonsView.setSubGroup( subGroupMapper.toResponseVO( subGroupLessons.stream()
                                                                                     .map( SubGroupLesson::getSubGroup )
                                                                                     .findFirst()
                                                                                     .get() ) );
        return subGroupLessonsView;
    }
}
