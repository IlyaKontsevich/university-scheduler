package com.scheduler.group.subgroup;

import com.scheduler.common.entity.BaseEntity;
import com.scheduler.group.Group;
import com.scheduler.lesson.SubGroupLesson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode( callSuper = true )
@Table( name = "subgroup" )
public class SubGroup extends BaseEntity
{
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator( name = "native", strategy = "native")
    @Column( name = "sgr_id" )
    private Long id;

    @NotNull
    @Column( name = "sgr_number" )
    private Long number;

    @NotNull
    @Column( name = "un_id" )
    private Long universityId;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "gr_id" )
    private Group group;

    @OneToMany( fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "subGroup" )
    private List<SubGroupLesson> lessons = new ArrayList<>();

    @Column( name = "sgr_created", updatable = false )
    private LocalDateTime created;

    @Column( name = "sgr_modified" )
    private LocalDateTime modified;
}
