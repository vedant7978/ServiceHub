package com.dalhousie.servicehub.mapper;

/**
 * Mapper class that maps classes from entity to dto and vice versa
 * @param <Entity> Entity class reference
 * @param <Dto> Dto class reference
 */
public interface Mapper<Entity,Dto> {

    /**
     * Converts the Entity class to Dto class
     * @param entity Entity to convert
     * @return Dto instance
     */
    Dto toDto(Entity entity);

    /**
     * Converts the Dto class to Entity class
     * @param dto Dto to convert
     * @return Entity instance
     */
    Entity toEntity(Dto dto);
}
