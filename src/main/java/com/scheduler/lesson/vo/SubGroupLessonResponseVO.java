package com.scheduler.lesson.vo;

import com.scheduler.group.subgroup.vo.SubGroupResponseVO;
import com.scheduler.teacher.vo.TeacherResponseVO;
import lombok.Data;

@Data
public class SubGroupLessonResponseVO
{
    private Long id;
    private TeacherResponseVO teacher;
    private LessonResponseVO lesson;
    private SubGroupResponseVO subGroup;
    private Long universityId;
}
