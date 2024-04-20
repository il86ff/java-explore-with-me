package ru.practicum.compilations.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.practicum.compilations.dto.CompilationDTO;
import ru.practicum.compilations.dto.NewCompilationDTO;
import ru.practicum.compilations.entity.Compilation;
import ru.practicum.events.mapper.EventMapper;

@Mapper(uses = EventMapper.class, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompilationMapper {

    CompilationMapper INSTANCE = Mappers.getMapper(CompilationMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    Compilation newCompilationDtoToCompilation(NewCompilationDTO compilationDto);

    CompilationDTO compilationToCompilationDto(Compilation compilation);
}
