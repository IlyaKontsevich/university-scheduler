package com.scheduler.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@JsonPropertyOrder( { "code", "message" } )
@NoArgsConstructor
public class ResponseErrorImpl implements ResponseError
{
    private static final long serialVersionUID = 4285218945822868530L;

    @JsonView( JsonViews.Summary.class )
    private String code;
    @JsonView( JsonViews.Summary.class )
    private String message;
    @JsonView( JsonViews.Summary.class )
    private String token;
    @JsonIgnore
    private String messageKey;
    @JsonIgnore
    private HttpStatus httpStatus;

    public ResponseErrorImpl( ResponseError error, String message )
    {
        this.code = error.getCode();
        this.message = message;
        this.httpStatus = error.getHttpStatus();
    }

    @Override
    public String toString()
    {
        return getCode();
    }
}
