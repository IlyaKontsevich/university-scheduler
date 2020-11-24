package com.scheduler.cathedra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CathedraRepository extends JpaRepository<Cathedra, Long>
{
}
