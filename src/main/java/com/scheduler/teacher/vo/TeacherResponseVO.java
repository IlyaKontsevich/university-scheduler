package com.scheduler.teacher.vo;

import com.scheduler.lesson.vo.TeacherLessonResponseVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TeacherResponseVO
{
    private Long id;
    private String name;
    private Long universityId;
    private List<TeacherLessonResponseVO> lessons = new ArrayList<>();
}
