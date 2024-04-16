package ru.practicum.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.events.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
