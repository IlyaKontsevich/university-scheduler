package com.scheduler.cathedra;

import com.scheduler.common.entity.BaseEntity;
import com.scheduler.faculty.Faculty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode( callSuper = true )
@Table( name = "cathedra" )
public class Cathedra extends BaseEntity
{
    @Id
    @NotNull
    @Column( name = "ca_id" )
    private Long id;

    @NotNull
    @Column( name = "ca_name" )
    private String name;

    @NotNull
    @Column( name = "un_id" )
    private Long universityId;

    @Column( name = "ca_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "ca_modified" )
    private LocalDateTime modified;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "fac_id", updatable = false, insertable = false )
    private Faculty faculty;
}
