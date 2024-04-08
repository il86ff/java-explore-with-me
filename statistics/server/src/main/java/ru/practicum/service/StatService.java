package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.RequestDTO;
import ru.practicum.RequestOutDTO;
import ru.practicum.mapper.StatMapper;
import ru.practicum.model.Application;
import ru.practicum.model.Request;
import ru.practicum.repository.ApplicationRepository;
import ru.practicum.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatService {
    private final RequestRepository requestRepository;
    private final ApplicationRepository appRepository;
    private final StatMapper mapper;

    public void addRequest(RequestDTO requestDto) {
        Optional<Application> optionalApp = appRepository.findByName(requestDto.getApp());

        Application app = optionalApp.orElseGet(() -> appRepository.save(new Application(requestDto.getApp())));

        Request request = mapper.toRequest(requestDto);
        request.setApp(app);
        requestRepository.save(request);
    }

    public List<RequestOutDTO> getRequestsWithViews(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        if (unique) {
            if (uris == null || uris.isEmpty()) {
                return requestRepository.getUniqueIpRequestsWithoutUri(start, end);
            }
            return requestRepository.getUniqueIpRequestsWithUri(start, end, uris);
        } else {
            if (uris == null || uris.isEmpty()) {
                return requestRepository.getAllRequestsWithoutUri(start, end);
            }
            return requestRepository.getAllRequestsWithUri(start, end, uris);
        }
    }
}
