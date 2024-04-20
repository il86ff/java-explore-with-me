package ru.practicum.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationDTO;
import ru.practicum.compilations.dto.NewCompilationDTO;
import ru.practicum.compilations.dto.UpdateCompilationRequest;
import ru.practicum.compilations.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDTO addEventCompilation(@RequestBody @Valid NewCompilationDTO compilationDto) {

        log.info("Calling addEventCompilation: /admin/compilations with 'compilationDto': {}", compilationDto);
        return compilationService.add(compilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDTO updateEventCompilation(@PathVariable Long compId,
                                                 @RequestBody @Valid UpdateCompilationRequest compRequest) {

        log.info("Calling updateEventCompilation: /admin/compilations/{compId} with 'compId': {}", compId);
        return compilationService.update(compId, compRequest);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEventCompilation(@PathVariable Long compId) {

        log.info("Calling deleteEventCompilation: /admin/compilations/{compId} with 'compId': {}", compId);
        compilationService.delete(compId);
    }
}
