package com.scheduler.lesson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherLessonRepository extends JpaRepository<TeacherLesson, Long>
{
}
