package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDTO;
import ru.practicum.events.dto.EventUpdateDTO;
import ru.practicum.events.entity.enums.EventState;
import ru.practicum.events.service.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDTO> getAdminEvents(@RequestParam(name = "users", required = false) List<Long> users,
                                             @RequestParam(name = "states", required = false) List<EventState> states,
                                             @RequestParam(name = "categories", required = false) List<Long> categories,
                                             @RequestParam(name = "rangeStart", required = false)
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                             @RequestParam(name = "rangeEnd", required = false)
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                             @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("Calling getAdminEvents: /admin/events with 'users': {}, 'states': {}, 'categories': {}, 'rangeStart': {}, " +
                "'rangeEnd': {}, 'from': {}, 'size': {}", users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getAdminEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping(path = "/{eventId}")
    public EventFullDTO updateAdminEvent(@PathVariable("eventId") Long eventId,
                                         @RequestBody @Valid EventUpdateDTO eventUpdateDto) {

        log.info("Calling updateAdminEvent: /admin/events/{eventId} with 'eventId': {}, 'eventAdminPatchDto': {}", eventId, eventUpdateDto);
        return eventService.updateAdminEvent(eventId, eventUpdateDto);
    }
}
