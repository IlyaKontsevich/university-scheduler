package com.scheduler.teacher.vo;

import com.scheduler.lesson.vo.LessonResponseVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TeacherResponseVO
{
    private Long id;
    private String name;
    private Long universityId;
    private List<LessonResponseVO> lessons = new ArrayList<>();
}
