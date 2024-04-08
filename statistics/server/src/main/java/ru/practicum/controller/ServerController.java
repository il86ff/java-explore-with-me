package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.RequestDTO;
import ru.practicum.RequestOutDTO;
import ru.practicum.service.StatService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ServerController {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addRequest(@Valid @RequestBody RequestDTO requestDto) {
        statService.addRequest(requestDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<RequestOutDTO>> getStats(@RequestParam String start,
                                                        @RequestParam String end,
                                                        @RequestParam(required = false) List<String> uris,
                                                        @RequestParam(defaultValue = "false") Boolean unique) {

        LocalDateTime startDT;
        LocalDateTime endDT;
        try {
            startDT = LocalDateTime.parse(start, DTF);
            endDT = LocalDateTime.parse(end, DTF);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        }

        List<RequestOutDTO> results = statService.getRequestsWithViews(startDT, endDT, uris, unique);
        return ResponseEntity.ok().body(results);
    }
}
