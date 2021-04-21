package com.scheduler.common.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface EntityToVOCrudMapper<ENTITY, CREATE_VO, UPDATE_VO, RESPONSE_VO>
{
    RESPONSE_VO toResponseVO( ENTITY entity, Object... args );

    ENTITY toNewEntity( CREATE_VO vo, Object... args );

    void updateEntity( ENTITY entity, UPDATE_VO vo, Object... args );

    default List<RESPONSE_VO> toResponseVOs( List<ENTITY> entities, Object... args )
    {
        return Optional.ofNullable( entities )
                       .orElse( Collections.emptyList() )
                       .stream()
                       .map( entity -> toResponseVO( entity, args ) )
                       .collect( Collectors.toList() );
    }

    default List<ENTITY> toEntities( List<CREATE_VO> vos, Object... args )
    {
        return Optional.ofNullable( vos )
                       .orElse( Collections.emptyList() )
                       .stream()
                       .map( vo -> toNewEntity( vo, args ) )
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
