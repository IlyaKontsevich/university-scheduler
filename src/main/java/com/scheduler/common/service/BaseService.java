package com.scheduler.common.service;

import com.scheduler.common.exception.ErrorResponse;
import com.scheduler.common.domain.NotFoundResponseError;
import com.scheduler.common.mapper.EntityToVOCrudMapper;
import com.scheduler.common.repository.SpecificationPagingAndSortingRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
public abstract class BaseService<ENTITY, CREATE_VO, UPDATE_VO, RESPONSE_VO>
{
    @Getter
    @SuppressWarnings( "unchecked" )
    private final Class<ENTITY> entityClass = (Class<ENTITY>) GenericTypeResolver.resolveTypeArgument( getClass(), BaseService.class );

    protected abstract EntityToVOCrudMapper<ENTITY, CREATE_VO, UPDATE_VO, RESPONSE_VO> getEntityVOMapper();

    //@Override
    protected abstract SpecificationPagingAndSortingRepository<ENTITY, UUID> getEntityRepository();

    protected RESPONSE_VO toResponseVO( ENTITY entity )
    {
        return getEntityVOMapper().toResponseVO( entity );
    }

    /**
     * Abstract method should create entity from createVO with specified id and persist it
     *
     * @param createVO - create VO
     * @param id       - id of entity
     * @param args     - additional arguments
     * @return created and persisted entity
     */
    protected abstract ENTITY createEntity( CREATE_VO createVO, UUID id, Object... args );

    /**
     * Abstract method should update existing entity from updateVO.
     *
     * @param entity   - entity to be updated
     * @param updateVO - update VO
     * @param args     - additional arguments
     * @return updated entity
     */
    protected abstract ENTITY updateEntity( ENTITY entity, UPDATE_VO updateVO, Object... args );

    /**
     * Abstract method should find existing entity by id.
     *
     * @param id   - id of entity
     * @param args - additional arguments
     * @return entity
     */
    protected ENTITY getEntity( UUID id, Object... args )
    {
        return getEntityRepository().findById( id )
                                    .orElseThrow( () -> new ErrorResponse( new NotFoundResponseError( getEntityClass(), id ) ) );
    }

    /**
     * Abstract method should find all existing entities.
     *
     * @param pageable - pageable info
     * @param args     - additional arguments
     * @return page of entities
     */
    protected Page<ENTITY> getEntities( Pageable pageable, Object... args )
    {
        return getEntityRepository().findAll( pageable );
    }

    /**
     * Creates entity, based on value object.
     *
     * @param vo   input data
     * @param args optional arguments
     * @return value object, representing created entity
     */
    @Transactional( propagation = Propagation.REQUIRED )
    public RESPONSE_VO create( CREATE_VO vo, UUID id, Object... args )
    {
        return getEntityVOMapper().toResponseVO( createReturnEntity( vo, id, args ), args );
    }

    /**
     * Creates entity, based on value object.
     *
     * @param vo   input data
     * @param args optional arguments
     * @return created entity
     */
    @Transactional( propagation = Propagation.REQUIRED )
    public ENTITY createReturnEntity( CREATE_VO vo, UUID id, Object... args )
    {
        log.debug( "Create {} = {}", getEntityClass().getSimpleName(), id );
        return createEntity( vo, id, args );
    }

    /**
     * Gets entity by UUID id.
     *
     * @param id   UUID id of entity
     * @param args optional arguments
     * @return value object, representing entity
     */
    @Transactional( propagation = Propagation.REQUIRED, readOnly = true )
    public RESPONSE_VO get( UUID id, Object... args )
    {
        return getEntityVOMapper().toResponseVO( getEntity( id, args ), args );
    }

    /**
     * Updates entity with value object data.
     *
     * @param vo   input data
     * @param args optional arguments
     * @return value object, representing upserted entity
     */
    @Transactional( propagation = Propagation.REQUIRED )
    public RESPONSE_VO update( UPDATE_VO vo, UUID id, Object... args )
    {
        return getEntityVOMapper().toResponseVO( updateReturnEntity( vo, id, args ), args );
    }

    /**
     * Updates entity with value object data.
     *
     * @param vo   input data
     * @param args optional arguments
     * @return created entity
     */
    @Transactional( propagation = Propagation.REQUIRED )
    public ENTITY updateReturnEntity( UPDATE_VO vo, UUID id, Object... args )
    {
        log.debug( "Update {} = {}", getEntityClass().getSimpleName(), id );
        ENTITY entity = getEntity( id, args );
        return updateEntity( entity, vo, args );
    }

    /**
     * Deletes entity by id.
     *
     * @param id   UUID id of entity
     * @param args optional arguments
     * @return null
     */
    @Transactional( propagation = Propagation.REQUIRED )
    public Void delete( UUID id, Object... args )
    {
        log.debug( "Delete {} = {}", getEntityClass().getSimpleName(), id );

        ENTITY entity = getEntity( id, args );
        getEntityRepository().delete( entity );

        return null;
    }
}
