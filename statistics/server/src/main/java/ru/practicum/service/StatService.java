package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.RequestDTO;
import ru.practicum.RequestOutDTO;
import ru.practicum.mapper.StatMapper;
import ru.practicum.model.Application;
import ru.practicum.model.Request;
import ru.practicum.repository.ApplicationRepository;
import ru.practicum.repository.RequestRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatService {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final RequestRepository requestRepository;
    private final ApplicationRepository appRepository;
    private final StatMapper mapper;

    @Transactional
    public void addRequest(RequestDTO requestDto) {
        Optional<Application> optionalApp = appRepository.findByName(requestDto.getApp());

        Application app = optionalApp.orElseGet(() -> appRepository.save(new Application(requestDto.getApp())));

        Request request = mapper.toRequest(requestDto);
        request.setApp(app);
        requestRepository.save(request);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<RequestOutDTO>> getRequestsWithViews(String start, String end, List<String> uris, Boolean unique) {

        LocalDateTime startDT;
        LocalDateTime endDT;
        try {
            startDT = LocalDateTime.parse(start, DTF);
            endDT = LocalDateTime.parse(end, DTF);
            if (startDT.isEqual(endDT)) ResponseEntity.badRequest().build();
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        }

        if (unique) {
            if (uris == null || uris.isEmpty()) {
                return ResponseEntity.ok().body(requestRepository.getUniqueIpRequestsWithoutUri(startDT, endDT));
            }
            return ResponseEntity.ok().body(requestRepository.getUniqueIpRequestsWithUri(startDT, endDT, uris));
        } else {
            if (uris == null || uris.isEmpty()) {
                return ResponseEntity.ok().body(requestRepository.getAllRequestsWithoutUri(startDT, endDT));
            }
            return ResponseEntity.ok().body(requestRepository.getAllRequestsWithUri(startDT, endDT, uris));
        }
    }

    public ResponseEntity<List<RequestOutDTO>> getRequestsWithViewsByIp(String start, String end, List<String> uris, Boolean unique, String ip) {

        LocalDateTime startDT;
        LocalDateTime endDT;
        try {
            startDT = LocalDateTime.parse(start, DTF);
            endDT = LocalDateTime.parse(end, DTF);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        }

        if (unique) {
            if (uris == null || uris.isEmpty()) {
                return ResponseEntity.ok().body(requestRepository.getUniqueIpRequestsWithoutUriByIp(startDT, endDT, ip));
            }
            return ResponseEntity.ok().body(requestRepository.getUniqueIpRequestsWithUriByIp(startDT, endDT, uris, ip));
        } else {
            if (uris == null || uris.isEmpty()) {
                return ResponseEntity.ok().body(requestRepository.getAllRequestsWithoutUriByIp(startDT, endDT, ip));
            }
            return ResponseEntity.ok().body(requestRepository.getAllRequestsWithUriByIp(startDT, endDT, uris, ip));
        }
    }
}
