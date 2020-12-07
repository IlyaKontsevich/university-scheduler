package com.scheduler.timetable;

import com.scheduler.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@EqualsAndHashCode( callSuper = true )
@Table( name = "time_table_lesson" )
public class TimeTableLesson extends BaseEntity
{
    @Id
    @NotNull
    @Column( name = "ttl_id" )
    private Long id;

    @NotNull
    @Column( name = "ttl_number" )
    private Integer number;

    @NotNull
    @Column( name = "ttl_start" )
    private LocalTime startTime;

    @NotNull
    @Column( name = "ttl_end" )
    private LocalTime endTime;

    @NotNull
    @Column( name = "un_id" )
    private Long universityId;

    @Column( name = "ttl_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "ttl_modified" )
    private LocalDateTime modified;
}
