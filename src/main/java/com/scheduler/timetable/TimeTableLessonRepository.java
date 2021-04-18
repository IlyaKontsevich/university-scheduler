package com.scheduler.timetable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeTableLessonRepository extends JpaRepository<TimeTableLesson, Long>
{
    TimeTableLesson findByStartTime( String startTime );
}
