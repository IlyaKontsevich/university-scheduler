package com.scheduler.classroom;

import com.scheduler.common.entity.BaseEntity;
import com.scheduler.faculty.Faculty;
import com.scheduler.hull.Hull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode( callSuper = true )
@Table( name = "classroom" )
public class Classroom extends BaseEntity
{
    @Id
    @NotNull
    @Column( name = "class_id" )
    private Long id;

    @NotNull
    @Column( name = "class_name" )
    private String name;

    @NotNull
    @Column( name = "un_id" )
    private Long universityId;

    @NotNull
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "hu_id" )
    private Hull hull;

    @Column( name = "class_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "class_modified" )
    private LocalDateTime modified;

    @NotNull
    @Column( name = "class_capacity" )
    private Long capacity;
}
