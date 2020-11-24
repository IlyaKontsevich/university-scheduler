package com.scheduler.faculty;

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
@Table( name = "faculty" )
public class Faculty extends BaseEntity
{
    @Id
    @NotNull
    @Column( name = "fa_id" )
    private Long id;

    @NotNull
    @Column( name = "fa_name" )
    private String name;

    @NotNull
    @Column( name = "un_id" )
    private Long universityId;

    @Column( name = "fa_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "fa_modified" )
    private LocalDateTime modified;
}
