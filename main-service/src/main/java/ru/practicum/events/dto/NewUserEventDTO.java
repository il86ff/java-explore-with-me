package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.categories.dto.CategoryDTO;
import ru.practicum.events.entity.Location;
import ru.practicum.events.entity.enums.StateAction;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserEventDTO {

    @Size(min = 20, max = 2000)
    private String annotation;

    private CategoryDTO category;

    @Size(min = 20, max = 7000)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Long participantLimit;

    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private StateAction stateAction;

    @Size(min = 3, max = 120)
    private String title;
}
