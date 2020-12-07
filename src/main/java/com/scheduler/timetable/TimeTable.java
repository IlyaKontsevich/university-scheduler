package com.scheduler.timetable;

import com.scheduler.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
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
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "ttl_id" )
    private TimeTableLesson timeTableLesson;

    @NotNull
    @Column( name = "tt_week_type" )
    private WeekType week;

    @NotNull
    @Column( name = "tt_week_day" )
    private WeekDay weekDay;

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