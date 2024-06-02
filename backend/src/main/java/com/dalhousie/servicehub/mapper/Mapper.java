package com.dalhousie.servicehub.mapper;

public interface Mapper<Entity,Dto> {

    Dto toDto(Entity entity);

    Entity toEntity(Dto dto);

}
