package com.scheduler.hull;

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
@Table( name = "hull" )
public class Hull extends BaseEntity
{
    @Id
    @NotNull
    @Column( name = "hu_id" )
    private Long id;

    @NotNull
    @Column( name = "hu_number" )
    private Long number;

    @NotNull
    @Column( name = "hu_address" )
    private String address;

    @NotNull
    @Column( name = "un_id" )
    private Long universityId;

    @Column( name = "hu_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "hu_modified" )
    private LocalDateTime modified;
}

