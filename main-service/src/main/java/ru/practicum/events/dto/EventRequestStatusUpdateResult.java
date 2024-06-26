package ru.practicum.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.requests.dto.ParticipationRequestDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDTO> confirmedRequests;
    private List<ParticipationRequestDTO> rejectedRequests;
}
