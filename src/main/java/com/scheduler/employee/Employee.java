package com.scheduler.employee;

import com.scheduler.auth.Credential;
import com.scheduler.cathedra.Cathedra;
import com.scheduler.common.entity.BaseEntity;
import com.scheduler.faculty.Faculty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode( callSuper = true )
@Table( name = "employee" )
public class Employee extends BaseEntity
{
    @Id
    @NotNull
    @Column( name = "em_id" )
    private Long id;

    @NotNull
    @Column( name = "em_name" )
    private String name;

    @Column( name = "em_sex" )
    private String sex;

    @Column( name = "em_position" )
    private Long position;

    @NotNull
    @Column( name = "un_id" )
    private Long universityId;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "ca_id" )
    private Cathedra cathedra;

    @NotNull
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "fa_id" )
    private Faculty faculty;

    @NotNull
    @OneToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "cr_id" )
    private Credential credential;

    @Column( name = "em_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "em_modified" )
    private LocalDateTime modified;
}