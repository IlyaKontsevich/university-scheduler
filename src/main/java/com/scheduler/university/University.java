package com.scheduler.university;

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
@Table( name = "university" )
public class University extends BaseEntity
{
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator( name = "native", strategy = "native")
    @Column( name = "un_id" )
    private Long id;

    @NotNull
    @Column( name = "un_name" )
    private String name;

    @NotNull
    @Column( name = "un_address" )
    private String address;

    @Column( name = "un_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "un_modified" )
    private LocalDateTime modified;
}
