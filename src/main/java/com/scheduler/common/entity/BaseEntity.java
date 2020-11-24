package com.scheduler.common.entity;

import lombok.Data;
import org.springframework.data.domain.Persistable;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class BaseEntity implements Persistable
{
    private transient boolean isNew = false;

    @Override
    public boolean isNew()
    {
        return isNew;
    }
}
