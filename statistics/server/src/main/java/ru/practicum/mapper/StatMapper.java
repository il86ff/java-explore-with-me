package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.practicum.RequestDTO;
import ru.practicum.model.Request;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StatMapper {
    StatMapper INSTANCE = Mappers.getMapper(StatMapper.class);

    @Mapping(source = "app.name", target = "app")
    RequestDTO toRequestDto(Request request);

    @Mapping(source = "app", target = "app.name")
    Request toRequest(RequestDTO requestDto);
}
