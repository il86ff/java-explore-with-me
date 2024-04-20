package ru.practicum.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.events.dto.EventShortDTO;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDTO {

    private Integer id;

    private Set<EventShortDTO> events;

    private Boolean pinned;

    @NotBlank
    @Length(min = 1, max = 50)
    private String title;
}
