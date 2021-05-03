package com.scheduler.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@JsonPropertyOrder( { "code", "message" } )
@JsonDeserialize( as = ResponseErrorImpl.class )
public interface ResponseError extends Serializable
{
    String getCode();

    default String getMessage()
    {
        return getMessageKey();
    }

    @JsonIgnore
    default String getMessageKey()
    {
        return getCode();
    }

    @JsonIgnore
    HttpStatus getHttpStatus();

    @JsonIgnore
    default Object[] getArguments()
    {
        return new Object[] {};
    }
}