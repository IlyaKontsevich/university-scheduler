package com.scheduler.auth;

import com.scheduler.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode( callSuper = true )
@Table( name = "credential" )
public class Credential extends BaseEntity
{
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator( name = "native", strategy = "native")
    @Column( name = "cr_id" )
    private Long id;

    @NotNull
    @Column( name = "cr_name" )
    private String name;

    @NotNull
    @Column( name = "cr_login" )
    private String login;

    @NotNull
    @Column( name = "cr_password" )
    private String password;

    @NotNull
    @Column( name = "un_id" )
    private Long universityId;

    @NotNull
    @Column( name = "cr_is_pass_temp" )
    private boolean isPasswordTemp;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "rol_id" )
    private Role role;

    @Column( name = "cr_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "cr_modified" )
    private LocalDateTime modified;
}

