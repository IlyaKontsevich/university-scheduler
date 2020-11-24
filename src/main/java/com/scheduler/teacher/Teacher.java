package com.scheduler.teacher;

import com.scheduler.common.entity.BaseEntity;
import com.scheduler.employee.Employee;
import com.scheduler.lesson.TeacherLesson;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode( callSuper = true )
@Table( name = "teacher" )
public class Teacher extends BaseEntity
{
    @Id
    @NotNull
    @Column( name = "teach_id" )
    private Long id;

    @NotNull
    @Column( name = "teach_name" )
    private String name;

    @NotNull
    @Column( name = "un_id" )
    private Long universityId;

    @NotNull
    @OneToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "em_id" )
    private Employee employee;

    @OneToMany( fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "teacher" )
    private List<TeacherLesson> lessons = new ArrayList<>();

    @Column( name = "teach_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "teach_modified" )
    private LocalDateTime modified;
}
