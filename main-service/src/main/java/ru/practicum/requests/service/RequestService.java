package ru.practicum.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.dto.EventRequestStatusUpdateRequest;
import ru.practicum.events.dto.EventRequestStatusUpdateResult;
import ru.practicum.events.entity.*;
import ru.practicum.events.entity.enums.EventState;
import ru.practicum.events.entity.enums.EventStatus;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.ObjectNotFoundException;
import ru.practicum.exceptions.RequestConflictException;
import ru.practicum.requests.dto.ParticipationRequestDTO;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.requests.entity.ParticipationRequest;
import ru.practicum.requests.entity.ParticipationStatus;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.users.entity.User;
import ru.practicum.users.repository.UserRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;

    @Transactional
    public ParticipationRequestDTO addParticipationRequest(Long userId, Long eventId) {
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            log.error("Calling addParticipationRequest data: with id {}, event id {}", userId, eventId);
            throw new RequestConflictException("Participation request with userId = " + userId
                    + " eventId = " + eventId + " already exists.");
        }

        User requester = userRepository.findById(userId).orElseThrow(() -> {
            log.error("Calling addParticipationRequest data: with id {}, event id {}", userId, eventId);
            throw new ObjectNotFoundException("User with id = " + userId + " was not found.");
        });

        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Calling addParticipationRequest data: with id {}, event id {}", userId, eventId);
            throw new ObjectNotFoundException("Event with id = " + eventId + " doesn't exist.");
        });

        if (!event.getState().equals(EventState.PUBLISHED)) {
            log.error("Calling addParticipationRequest data: with id {}, event id {}", userId, eventId);
            throw new RequestConflictException("Users are not allowed to register for unpublished events.");
        }

        if (Objects.equals(userId, event.getInitiator().getId())) {
            log.error("Calling addParticipationRequest data: with id {}, event id {}", userId, eventId);
            throw new RequestConflictException("Event organizers are not allowed to request participation in their own events.");
        }

        if ((event.getParticipantLimit() != 0L) && (event.getConfirmedRequests() >= event.getParticipantLimit())) {
            log.error("Calling addParticipationRequest data: with id {}, event id {}", userId, eventId);
            throw new RequestConflictException("Participant limit reached.");
        }

        log.info("Calling addParticipationRequest data: with id {}, event id {}", userId, eventId);

        ParticipationRequest requestToSave = new ParticipationRequest(requester, event,
                !event.getRequestModeration() || event.getParticipantLimit() == 0L ?
                        ParticipationStatus.CONFIRMED : ParticipationStatus.PENDING, LocalDateTime.now());

        ParticipationRequest participationRequest = requestRepository.save(requestToSave);

        if (participationRequest.getStatus() == ParticipationStatus.CONFIRMED) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        return requestMapper.requestToDto(participationRequest);
    }

    @Transactional
    public ParticipationRequestDTO cancelParticipationRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId).orElseThrow(() -> {
            log.error("Calling cancelParticipationRequest data: with id {}, request id {}", userId, requestId);
            throw new ObjectNotFoundException("Participation request with id = " + requestId + " doesn't exist.");
        });

        if (request.getStatus() == ParticipationStatus.CONFIRMED) {
            log.error("Calling cancelParticipationRequest data: with id {}, request id {}", userId, requestId);
            throw new RequestConflictException("Participation request with id = " + requestId + " is already confirmed.");
        }

        request.setStatus(ParticipationStatus.CANCELED);

        Long eventId = request.getEvent().getId();
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Calling cancelParticipationRequest data: with id {}, request id {}", userId, requestId);
            throw new ObjectNotFoundException("Event with id = " + eventId + " doesn't exist.");
        });

        log.info("Calling cancelParticipationRequest data: with id {}, request id {}", userId, requestId);

        event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        eventRepository.save(event);

        request = requestRepository.save(request);
        return requestMapper.requestToDto(request);
    }

    @Transactional(readOnly = true)
    public List<ParticipationRequestDTO> getUserRequests(Long userId) {

        log.info("Calling getUserRequests data: with id {}", userId);

        List<ParticipationRequest> requests = requestRepository.findAllByRequesterId(userId);

        if (!requests.isEmpty()) {
            return requests.stream()
                    .map(requestMapper::requestToDto)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<ParticipationRequestDTO> getUserEventRequests(Long userId, Long eventId) {

        log.info("Calling getUserEventRequests data: with id {}, event id {}", userId, eventId);

        List<ParticipationRequest> requests = requestRepository.findAllByEventIdAndEventInitiatorId(eventId, userId);

        if (!requests.isEmpty()) {
            return requests.stream()
                    .map(requestMapper::requestToDto)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional
    public EventRequestStatusUpdateResult updateEventRequests(Long userId, Long eventId,
                                                              @Valid EventRequestStatusUpdateRequest requestsUpdate) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> {
            log.error("Calling updateEventRequests data: with id {}, event id {}", userId, eventId);
            throw new ObjectNotFoundException("Event with id = " + eventId + " and user id = " + userId + " doesn't exist.");
        });

        if (!event.getInitiator().getId().equals(userId)) {
            log.error("Calling updateEventRequests data: with id {}, event id {}", userId, eventId);
            throw new RequestConflictException("Access denied. User with id = " + userId + " is not an event initiator.");
        }

        List<ParticipationRequest> participationRequests = requestRepository.findAllByIdInAndAndEventId(requestsUpdate.getRequestIds(), eventId);

        if (participationRequests.size() != requestsUpdate.getRequestIds().size()) {
            log.error("Calling updateEventRequests data: with id {}, event id {}", userId, eventId);
            throw new ObjectNotFoundException("Incorrect request id(s) received in the request body.");
        }

        for (ParticipationRequest request : participationRequests) {
            if (!request.getStatus().equals(ParticipationStatus.PENDING)) {
                log.error("Calling updateEventRequests data: with id {}, event id {}", userId, eventId);
                throw new RequestConflictException("Only requests with status 'Pending' can be accepted or rejected.");
            }
        }

        List<ParticipationRequestDTO> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDTO> rejectedRequests = new ArrayList<>();

        if (requestsUpdate.getStatus() == EventStatus.REJECTED) {
            participationRequests.forEach(participationRequest -> {
                participationRequest.setStatus(ParticipationStatus.REJECTED);
                requestRepository.save(participationRequest);
                rejectedRequests.add(requestMapper.requestToDto(participationRequest));
            });
            return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
        }

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            return new EventRequestStatusUpdateResult(
                    participationRequests.stream().map(requestMapper::requestToDto).collect(Collectors.toList()),
                    new ArrayList<>()
            );
        }

        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            log.error("Calling updateEventRequests data: with id {}, event id {}", userId, eventId);
            throw new RequestConflictException("Failed to accept request. Reached max participant limit for event id = " + eventId + ".");
        }

        participationRequests.forEach(participationRequest -> {
            if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                participationRequest.setStatus(ParticipationStatus.CONFIRMED);
                requestRepository.save(participationRequest);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                confirmedRequests.add(requestMapper.requestToDto(participationRequest));
            } else {
                participationRequest.setStatus(ParticipationStatus.REJECTED);
                requestRepository.save(participationRequest);
                rejectedRequests.add(requestMapper.requestToDto(participationRequest));
            }
        });

        if (!confirmedRequests.isEmpty()) {
            eventRepository.save(event);
        }

        log.info("Calling updateEventRequests data: with id {}, event id {}", userId, eventId);
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }
}
