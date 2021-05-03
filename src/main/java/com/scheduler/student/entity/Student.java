package com.scheduler.student.entity;

import com.scheduler.auth.Credential;
import com.scheduler.common.entity.BaseEntity;
import com.scheduler.group.subgroup.SubGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode( callSuper = true )
@Table( name = "student" )
public class Student extends BaseEntity
{
    @Id
    @NotNull
    @Column( name = "st_id" )
    private Long id;

    @NotNull
    @Column( name = "st_name" )
    private String name;

    @NotNull
    @Column( name = "st_sex" )
    private String sex;

    @NotNull
    @Column( name = "st_is_capitan" )
    private boolean isCapitan;

    @NotNull
    @Column( name = "st_date_of_birth" )
    private LocalDateTime dateOfBirth;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "sgr_id" )
    private SubGroup subGroup;

    @NotNull
    @OneToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "cr_id" )
    private Credential credential;

    @NotNull
    @Column( name = "un_id" )
    private Long universityId;

    @Column( name = "st_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "st_modified" )
    private LocalDateTime modified;
}
