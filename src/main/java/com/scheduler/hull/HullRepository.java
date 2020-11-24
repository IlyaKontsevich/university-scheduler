package com.scheduler.hull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HullRepository extends JpaRepository<Hull, Long>
{
}
