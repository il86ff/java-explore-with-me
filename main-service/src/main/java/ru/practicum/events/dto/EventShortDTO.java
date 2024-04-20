package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.categories.dto.CategoryDTO;
import ru.practicum.users.dto.UserShortDTO;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDTO {

    @Size(min = 20, max = 2000)
    private String annotation;

    private Long id;

    private CategoryDTO category;

    private Long confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShortDTO initiator;

    private Boolean paid;

    @Size(min = 3, max = 120)
    private String title;

    private Long views;
}
