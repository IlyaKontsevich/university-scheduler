package com.scheduler.lesson;

import com.scheduler.classroom.Classroom;
import com.scheduler.common.entity.BaseEntity;
import com.scheduler.timetable.TimeTable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode( callSuper = true )
@Table( name = "lesson" )
public class Lesson extends BaseEntity
{
    @Id
    @NotNull
    @Column( name = "les_id" )
    private Long id;

    @NotNull
    @Column( name = "les_name" )
    private String name;

    @NotNull
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "tt_id" )
    private TimeTable timeTable;

    @NotNull
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "class_id" )
    private Classroom classroom;

    @NotNull
    @Column( name = "un_id" )
    private Long universityId;

    @Column( name = "les_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "les_modified" )
    private LocalDateTime modified;
}