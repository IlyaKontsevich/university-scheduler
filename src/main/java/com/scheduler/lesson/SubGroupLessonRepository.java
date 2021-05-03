package com.scheduler.lesson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubGroupLessonRepository extends JpaRepository<SubGroupLesson, Long>
{
    @Query( "from SubGroupLesson where subGroup.group.name =:name and subGroup.number =:number" )
    List<SubGroupLesson> findByGroupNameAndSubGroupNumber( @Param( "name" ) String name, @Param( "number" ) Long number);
}
