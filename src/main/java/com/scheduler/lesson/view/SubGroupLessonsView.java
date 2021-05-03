package com.scheduler.lesson.view;

import com.scheduler.group.subgroup.vo.SubGroupResponseVO;
import lombok.Data;

import java.util.List;

@Data
public class SubGroupLessonsView
{
    private SubGroupResponseVO subGroup;
    private List<StudentLessonView> lessons;
}
