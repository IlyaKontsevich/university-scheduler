package com.scheduler.group;

import com.scheduler.cathedra.Cathedra;
import com.scheduler.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@EqualsAndHashCode( callSuper = true )
@Table( name = "groupp" )
public class Group extends BaseEntity
{
    @Id
    @NotNull
    @Column( name = "gr_id" )
    private Long id;

    @NotNull
    @Column( name = "gr_name" )
    private String name;

    @NotNull
    @Column( name = "un_id" )
    private Long universityId;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "ca_id" )
    private Cathedra cathedra;

    @Column( name = "gr_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "gr_modified" )
    private LocalDateTime modified;
}

