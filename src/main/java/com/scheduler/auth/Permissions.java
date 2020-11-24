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
@Table( name = "permissions" )
public class Permissions extends BaseEntity
{
    @Id
    @NotNull
    @Column( name = "per_id" )
    private Long id;

    @NotNull
    @Column( name = "per_name" )
    private String name;

    @NotNull
    @Column( name = "un_id" )
    private Long universityId;

    @Column( name = "per_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "per_modified" )
    private LocalDateTime modified;
}
