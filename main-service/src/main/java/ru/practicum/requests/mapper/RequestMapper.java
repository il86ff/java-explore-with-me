package ru.practicum.requests.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.requests.dto.ParticipationRequestDTO;
import ru.practicum.requests.entity.ParticipationRequest;

public interface RequestMapper {
    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationRequestDTO requestToDto(ParticipationRequest participationRequest);
}
