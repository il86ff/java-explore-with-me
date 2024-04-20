package ru.practicum.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationDTO;
import ru.practicum.compilations.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDTO> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "10") Integer size) {

        log.info("Calling getCompilations: /compilations with 'pinned': {}, 'from': {}, 'size': {}", pinned, from, size);
        return compilationService.getAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDTO getCompilationById(@PathVariable Long compId) {

        log.info("Calling getCompilationById: /compilations/{compId} with 'compId': {}", compId);
        return compilationService.get(compId);
    }
}
