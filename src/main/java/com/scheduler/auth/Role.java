package com.scheduler.auth;

import com.scheduler.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode( callSuper = true )
@Table( name = "role" )
public class Role extends BaseEntity
{
    @Id
    @NotNull
    @Column( name = "rol_id" )
    private Long id;

    @NotNull
    @Column( name = "rol_name" )
    private String name;

    @NotNull
    @Column( name = "un_id" )
    private Long universityId;

    @Column( name = "rol_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "rol_modified" )
    private LocalDateTime modified;
}
