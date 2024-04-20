package ru.practicum.events.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.practicum.events.dto.EventFullDTO;
import ru.practicum.events.dto.EventShortDTO;
import ru.practicum.events.dto.NewEventDTO;
import ru.practicum.events.entity.Event;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    EventFullDTO eventToEventFullDto(Event event);

    EventShortDTO eventToShortDto(Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "compilations", ignore = true)
    Event newEventDtoToEvent(NewEventDTO eventDto);

    Set<EventShortDTO> listEventToSetEventShortDto(List<Event> events);
}
