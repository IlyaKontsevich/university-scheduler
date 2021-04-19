package com.scheduler.common.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.scheduler.common.domain.JsonViews;
import com.scheduler.common.domain.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.*;
import java.util.stream.Collectors;

@JsonIgnoreProperties( { "rootCause", "mostSpecificCause", "stackTrace", "rawStatusCode", "statusText", "message", "localizedMessage" } )
public class ErrorResponse extends HttpStatusCodeException
{
    private static final long serialVersionUID = -572349368962701456L;

    @JsonView( JsonViews.Summary.class )
    private final List<ResponseError> errors;

    public ErrorResponse( ResponseError... errors )
    {
        super( getHttpStatus( Arrays.asList( errors ) ), getErrorCode( Arrays.asList( errors ) ) );
        this.errors = new ArrayList<>( Arrays.asList( errors ) );
    }

    @JsonCreator( mode = JsonCreator.Mode.PROPERTIES )
    public ErrorResponse( @JsonProperty( "errors" ) Collection<ResponseError> errors )
    {
        super( getHttpStatus( new ArrayList<>( errors ) ), getErrorCode( new ArrayList<>( errors ) ) );
        this.errors = new ArrayList<>( errors );
    }

    public ErrorResponse( HttpStatus status, ErrorResponse response )
    {
        super( status, getErrorCode( response.getErrors() ) );
        this.errors = new ArrayList<>( response.getErrors() );
    }

    public ErrorResponse( HttpStatus status, List<ResponseError> errors )
    {
        super( status, getErrorCode( errors ) );
        this.errors = errors;
    }

    @Override
    public String toString()
    {
        return "Message: " + getMessage() + "\nErrors: " + getErrors().stream().map( ResponseError::getCode ).collect( Collectors.joining( "," ) );
    }

    public List<ResponseError> getErrors()
    {
        return errors;
    }

    private static String getErrorCode( List<ResponseError> errors )
    {
        return errors.stream()
                     .findFirst()
                     .map( ResponseError::getCode )
                     .orElse( "Error response without errors" );
    }

    private static HttpStatus getHttpStatus( List<ResponseError> errors )
    {
        return errors.stream()
                     .findFirst()
                     .map( ResponseError::getHttpStatus )
                     .orElse( HttpStatus.I_AM_A_TEAPOT );
    }

    private static class ErrorComparator implements Comparator<ResponseError>
    {
        private static final List<HttpStatus> HTTP_STATUS_PRIORITY = Arrays.asList( HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.UNAUTHORIZED, HttpStatus.FORBIDDEN,
            HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND, HttpStatus.CONFLICT );

        private static ErrorComparator instance = new ErrorComparator();

        static ErrorComparator getInstance()
        {
            return instance;
        }

        @Override
        public int compare( ResponseError a, ResponseError b )
        {
            int result = Integer.compare( HTTP_STATUS_PRIORITY.indexOf( a.getHttpStatus() ), HTTP_STATUS_PRIORITY.indexOf( b.getHttpStatus() ) );
            if( result == 0 )
            {
                result = a.getCode().compareTo( b.getCode() );
            }
            return result;
        }
    }
}
