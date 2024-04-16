package ru.practicum.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.*;
import ru.practicum.events.service.EventService;
import ru.practicum.requests.dto.ParticipationRequestDTO;
import ru.practicum.requests.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final RequestService requestService;
    private final EventService eventService;

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDTO addUserEvent(@PathVariable Long userId,
                                     @Valid @RequestBody NewEventDTO event) {

        log.info("Calling addUserEvent: /users/{userId}/events with 'userId': {}, 'event': {}", userId, event);
        return eventService.addUserEvent(userId, event);
    }

    @GetMapping("/events")
    public List<EventShortDTO> getUserEvents(@PathVariable Long userId,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {

        log.info("Calling getUserEvents: /users/{userId}/events with 'userId': {}, 'from': {}, 'size': {}", userId, from, size);
        return eventService.getUserEvents(userId, from, size);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDTO getUserEventById(@PathVariable Long userId,
                                         @PathVariable Long eventId) {

        log.info("Calling getUserEventById: /users/{userId}/events/{eventId} with 'userId': {}, 'eventId': {}", userId, eventId);
        return eventService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDTO updateUserEventById(@PathVariable @NotNull Long userId,
                                            @PathVariable @NotNull Long eventId,
                                            @RequestBody @Valid EventUpdateDTO eventDto) {

        log.info("Calling updateUserEventById: /users/{userId}/events/{eventId} with 'userId': {}, 'eventId': {}", userId, eventId);
        return eventService.updateUserEventById(userId, eventId, eventDto);
    }

    @GetMapping("/requests")
    public List<ParticipationRequestDTO> getUserRequests(@PathVariable Long userId) {

        log.info("Calling getUserRequests: /users/{userId}/requests with 'userId': {}", userId);
        return requestService.getUserRequests(userId);
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDTO addUserRequest(@PathVariable Long userId,
                                                  @RequestParam(name = "eventId") Long eventId) {

        log.info("Calling addUserRequest: /users/{userId}/requests with 'userId': {}, 'eventId': {}", userId, eventId);
        return requestService.addParticipationRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDTO updateUserRequest(@PathVariable Long userId,
                                                     @PathVariable Long requestId) {

        log.info("Calling updateUserRequest: /users/{userId}/requests/{requestId}/cancel with 'userId': {}, 'requestId': {}", userId, requestId);
        return requestService.cancelParticipationRequest(userId, requestId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDTO> getUserEventsRequests(@PathVariable Long userId,
                                                               @PathVariable Long eventId) {

        log.info("Calling getUserEventsRequests: /users/{userId}/events/{eventId}/requests with 'userId': {}, 'eventId': {}", userId, eventId);
        return requestService.getUserEventRequests(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateUserEventRequests(@PathVariable Long userId,
                                                                  @PathVariable Long eventId,
                                                                  @RequestBody EventRequestStatusUpdateRequest requestsUpdate) {

        log.info("Calling updateUserEventRequests: /users/{userId}/events/{eventId}/requests with 'userId': {}, " +
                "'eventId': {}, 'requestsUpdate': {}", userId, eventId, requestsUpdate);
        return requestService.updateEventRequests(userId, eventId, requestsUpdate);
    }
}
