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
@Table( name = "sub_group_lesson" )
public class SubGroupLesson extends BaseEntity
{
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator( name = "native", strategy = "native")
    @Column( name = "sgl_id" )
    private Long id;

    @NotNull
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "teach_id" )
    private Teacher teacher;

    @NotNull
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "les_id" )
    private Lesson lesson;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "sgr_id" )
    private SubGroup subGroup;

    @NotNull
    @Column( name = "un_id" )
    private Long universityId;

    @Column( name = "sgl_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "sgl_modified" )
    private LocalDateTime modified;
}