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
@Table( name = "credential" )
public class Credential extends BaseEntity
{
    @Id
    @NotNull
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

    @Column( name = "cr_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "cr_modified" )
    private LocalDateTime modified;
}

