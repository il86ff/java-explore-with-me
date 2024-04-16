package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.requests.entity.ParticipationRequest;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    Boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    List<ParticipationRequest> findAllByIdInAndAndEventId(Iterable<Long> ids, Long eventId);

    List<ParticipationRequest> findAllByRequesterId(Long userId);

    List<ParticipationRequest> findAllByEventIdAndEventInitiatorId(Long eventId, Long userId);

    Optional<ParticipationRequest> findByIdAndRequesterId(Long requestId, Long userId);

}
