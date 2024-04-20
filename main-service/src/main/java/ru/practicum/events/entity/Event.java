package ru.practicum.events.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.categories.entity.Category;
import ru.practicum.compilations.entity.Compilation;
import ru.practicum.events.entity.enums.EventState;
import ru.practicum.users.entity.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 20, max = 2000)
    private String annotation;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cat_id")
    private Category category;

    @Column(name = "confirmed_requests")
    private Long confirmedRequests = 0L;

    @Column(name = "created_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @Size(min = 20, max = 7000)
    private String description;

    @Column(name = "event_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "initiator")
    private User initiator;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location")
    private Location location;

    private Boolean paid;

    @Column(name = "participant_limit")
    private Long participantLimit = 0L;

    @Column(name = "published_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @Size(min = 3, max = 120)
    private String title;

    private Long views = 0L;

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "comp_events",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "compilation_id"))
    private List<Compilation> compilations = new ArrayList<>();
}
