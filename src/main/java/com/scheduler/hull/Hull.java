package com.scheduler.hull;

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
@Table( name = "hull" )
public class Hull extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator( name = "native", strategy = "native")
    @Column( name = "hu_id" )
    private Long id;

    @NotNull
    @Column( name = "hu_number" )
    private Long number;

    @Column( name = "hu_address" )
    private String address;

    @Column( name = "un_id" )
    private Long universityId;

    @Column( name = "hu_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "hu_modified" )
    private LocalDateTime modified;
}

