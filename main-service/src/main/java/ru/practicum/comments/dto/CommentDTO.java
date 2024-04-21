package ru.practicum.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.events.dto.EventShortDTO;
import ru.practicum.users.dto.UserShortDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    @NotBlank
    @Size(min = 5, max = 5000)
    private String text;

    private LocalDateTime createdOn;

    private UserShortDTO user;

    private EventShortDTO event;

}
