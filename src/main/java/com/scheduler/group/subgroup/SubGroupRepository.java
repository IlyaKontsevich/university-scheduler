package com.scheduler.group.subgroup;

import com.scheduler.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubGroupRepository extends JpaRepository<SubGroup, Long>
{
    SubGroup findByGroupAndNumber( Group group, Long number );
}
