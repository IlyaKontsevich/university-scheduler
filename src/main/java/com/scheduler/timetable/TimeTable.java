package com.scheduler.timetable;

import com.scheduler.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode( callSuper = true )
@Table( name = "time_table" )
public class TimeTable extends BaseEntity
{
    @Id
    @NotNull
    @Column( name = "tt_id" )
    private Long id;

    @NotNull
    @Column( name = "tt_name" )
    private String name;

    @NotNull
    @Column( name = "tt_date" )
    private LocalDate date;

    @NotNull
    @Column( name = "tt_week" )
    private String week; //TODO ENUM

    @Column( name = "tt_dates" ) //for week == NONE
    private String dates;

    @NotNull
    @Column( name = "un_id" )
    private Long universityId;

    @Column( name = "tt_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "tt_modified" )
    private LocalDateTime modified;
}