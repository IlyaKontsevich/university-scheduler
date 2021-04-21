package com.scheduler.common.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface EntityToResponseVOMapper <ENTITY, RESPONSE_VO>
{
    RESPONSE_VO toResponseVO( ENTITY entity, Object... args );

    default List<RESPONSE_VO> toResponseVOs( List<ENTITY> entities, Object... args )
    {
        return Optional.ofNullable( entities )
                       .orElse( Collections.emptyList() )
                       .stream()
                       .map( entity -> toResponseVO( entity, args ) )
                       .collect( Collectors.toList() );
    }

    default <T> T get( Object[] args, int index, Class<T> clazz )
    {
        if( !( clazz.isAssignableFrom( args[index].getClass() ) ) )
        {
            throw new IllegalArgumentException( "Expected " + clazz.getName() + ", but found " + args[index].getClass().getName() );
        }
        return clazz.cast( args[index] );
    }
}
