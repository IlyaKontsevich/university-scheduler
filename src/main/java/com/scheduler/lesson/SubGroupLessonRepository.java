package com.scheduler.lesson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubGroupLessonRepository extends JpaRepository<SubGroupLesson, Long>
{
    @Query( "find SubGroupLesson where subGroup.id =:subGroupId" )
    List<SubGroupLesson> findBySubGroupId( @Param( "subGroupId" ) Long subGroupId );
}
