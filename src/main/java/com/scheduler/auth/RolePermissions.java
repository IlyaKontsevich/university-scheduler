package com.scheduler.auth;

import com.scheduler.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode( callSuper = true )
@Table( name = "role_permissions" )
public class RolePermissions extends BaseEntity
{
    @Id
    @NotNull
    @Column( name = "rp_id" )
    private Long id;

    @NotNull
    @Column( name = "rp_name" )
    private String name;

    @NotNull
    @Column( name = "un_id" )
    private Long universityId;

    @Column( name = "rp_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "rp_modified" )
    private LocalDateTime modified;

    @NotNull
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "rol_id" )
    private Role role;

    @NotNull
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "per_id" )
    private Permissions permissions;
}
