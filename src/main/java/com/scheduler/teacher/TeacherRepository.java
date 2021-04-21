package com.scheduler.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long>
{
    Teacher findByName(String name);

    List<Teacher> findByNameContaining(String name);
}
