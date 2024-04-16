package ru.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.dto.CompilationDTO;
import ru.practicum.compilations.dto.NewCompilationDTO;
import ru.practicum.compilations.dto.UpdateCompilationRequest;
import ru.practicum.compilations.entity.Compilation;
import ru.practicum.compilations.mapper.CompilationMapper;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.events.entity.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.ObjectNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    public CompilationDTO add(NewCompilationDTO compilationDto) {
        Compilation compilation = compilationMapper.newCompilationDtoToCompilation(compilationDto);

        if (compilationDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllByIdIn(compilationDto.getEvents());
            compilation.setEvents(events);
        }

        compilation = compilationRepository.save(compilation);

        return compilationMapper.compilationToCompilationDto(compilation);
    }

    public CompilationDTO update(Long compId, UpdateCompilationRequest compRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Compilation with id = " + compId + " doesn't exist.");
        });

        updateComp(compilation, compRequest);

        compilation = compilationRepository.save(compilation);

        return compilationMapper.compilationToCompilationDto(compilation);
    }

    public CompilationDTO get(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Compilation with id = " + compId + " doesn't exist.");
        });

        return compilationMapper.compilationToCompilationDto(compilation);
    }

    public List<CompilationDTO> getAll(Boolean pinned, Integer from, Integer size) {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(from / size, size, sort);

        if (pinned != null) {
            List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageable);
            return compilations.stream().map(compilationMapper::compilationToCompilationDto).collect(Collectors.toList());
        } else {
            Page<Compilation> compilations = compilationRepository.findAll(pageable);
            return compilations.stream().map(compilationMapper::compilationToCompilationDto).collect(Collectors.toList());
        }
    }

    public void delete(Long compId) {
        try {
            compilationRepository.deleteById(compId);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Compilation with id = " + compId + " doesn't exist.");
        }
    }

    private void updateComp(Compilation compilation, UpdateCompilationRequest compRequest) {
        if (compRequest.getEvents() != null) {
            List<Event> events = eventRepository.findAllByIdIn(compRequest.getEvents());
            compilation.setEvents(events);
        }

        if (compRequest.getTitle() != null) {
            compilation.setTitle(compRequest.getTitle());
        }

        if (compRequest.getPinned() != null) {
            compilation.setPinned(compRequest.getPinned());
        }
    }
}
