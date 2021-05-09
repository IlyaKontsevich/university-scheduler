package com.scheduler.auth;

import com.scheduler.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode( callSuper = true )
@Table( name = "role" )
public class Role extends BaseEntity
{
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "native" )
    @GenericGenerator( name = "native", strategy = "native" )
    @Column( name = "rol_id" )
    private Long id;

    @NotNull
    @Column( name = "rol_name" )
    private String name;

    @NotEmpty
    @ManyToMany( cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY )
    @JoinTable( name = "role_permissions",
                joinColumns = @JoinColumn( name = "rol_id" ),
                inverseJoinColumns = @JoinColumn( name = "per_id" ) )
    private Set<Permissions> permissions;

    @NotNull
    @Column( name = "un_id" )
    private Long universityId;

    @Column( name = "rol_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "rol_modified" )
    private LocalDateTime modified;
}
