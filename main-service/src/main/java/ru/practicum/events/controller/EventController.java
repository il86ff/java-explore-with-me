package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDTO;
import ru.practicum.events.dto.EventShortDTO;
import ru.practicum.events.entity.enums.EventSort;
import ru.practicum.events.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j

public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDTO> getAll(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false", required = false) Boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "EVENT_DATE") EventSort sort,
            @RequestParam(defaultValue = "0", required = false) Integer from,
            @RequestParam(defaultValue = "10", required = false) Integer size,
            HttpServletRequest request) {

        log.info("Calling getAll: /events with 'text': {}, 'categories': {}, 'paid': {}, 'rangeStart': {}," +
                        " 'rangeEnd': {}, 'onlyAvailable': {}, 'sort': {}, 'from': {}, 'size': {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        return eventService.getAll(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, from, size, sort, request);
    }

    @GetMapping("/{id}")
    public EventFullDTO getById(@PathVariable Long id, HttpServletRequest request) {

        log.info("Calling getById: /events/{id} with 'id': {}",  id);
        return eventService.get(id, request);
    }
}
