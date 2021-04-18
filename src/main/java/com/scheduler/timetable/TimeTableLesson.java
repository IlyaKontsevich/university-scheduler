package com.scheduler.timetable;

import com.scheduler.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator( name = "native", strategy = "native")
    @Column( name = "ttl_id" )
    private Long id;
    
    @Column( name = "ttl_number" )
    private Integer number;

    @NotNull
    @Column( name = "ttl_start" )
    private String startTime;

    @NotNull
    @Column( name = "ttl_end" )
    private String endTime;

    @NotNull
    @Column( name = "un_id" )
    private Long universityId;

    @Column( name = "ttl_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "ttl_modified" )
    private LocalDateTime modified;
}
