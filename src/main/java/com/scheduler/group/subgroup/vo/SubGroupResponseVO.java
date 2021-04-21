package com.scheduler.group.subgroup.vo;

import com.scheduler.group.Group;
import com.scheduler.lesson.vo.SubGroupLessonResponseVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubGroupResponseVO
{
    private Long id;
    private Long number;
    private Long universityId;
    private Group group;
    private List<SubGroupLessonResponseVO> lessons = new ArrayList<>();
}
