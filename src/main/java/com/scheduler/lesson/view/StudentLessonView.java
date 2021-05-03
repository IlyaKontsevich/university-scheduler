package com.scheduler.lesson.view;

import com.scheduler.lesson.vo.LessonResponseVO;
import com.scheduler.teacher.vo.TeacherResponseVO;
import lombok.Data;

@Data
public class StudentLessonView
{
    private TeacherResponseVO teacher;
    private LessonResponseVO lesson;
}
