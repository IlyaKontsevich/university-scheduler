package com.scheduler.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

import java.util.Locale;

@JsonSerialize( as = ResponseError.class )
public class NotFoundResponseError implements ResponseError
{
    private static final long serialVersionUID = -9114087446030176377L;

    @JsonIgnore
    private String entityName;
    @JsonIgnore
    private Object search;

    public NotFoundResponseError()
    {
    }

    public NotFoundResponseError( Class entity )
    {
        this( entity, null );
    }

    public NotFoundResponseError( Class entity, Object search )
    {
        this.entityName = entity.getSimpleName().toLowerCase( Locale.US );
        this.search = search;
    }

    public NotFoundResponseError( String entityName, Object search )
    {
        this.entityName = entityName;
        this.search = search;
    }

    @Override
    public HttpStatus getHttpStatus()
    {
        return HttpStatus.NOT_FOUND;
    }

    public String getEntityName()
    {
        return entityName;
    }

    @Override
    public String toString()
    {
        return getCode();
    }

    @Override
    public String getCode()
    {
        return getEntityName() + ".not.found";
    }

    @Override
    public String getMessageKey()
    {
        return "response.error.entity.not.found";
    }

    @Override
    public Object[] getArguments()
    {
        return new Object[] {
            getEntityName(),
            search
        };
    }
}
