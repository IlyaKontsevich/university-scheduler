package com.scheduler.lesson;

import com.scheduler.common.entity.BaseEntity;
import com.scheduler.group.subgroup.SubGroup;
import com.scheduler.teacher.Teacher;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode( callSuper = true )
@Table( name = "teacher_lesson" )
public class TeacherLesson extends BaseEntity
{
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator( name = "native", strategy = "native")
    @Column( name = "tl_id" )
    private Long id;

    @NotNull
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "teach_id" )
    private Teacher teacher;

    @NotNull
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "les_id" )
    private Lesson lesson;

    @NotNull
    @Column( name = "un_id" )
    private Long universityId;

    @Column( name = "tl_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "tl_modified" )
    private LocalDateTime modified;
}