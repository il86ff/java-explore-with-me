package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Application;

import java.util.Optional;
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    Optional<Application> findByName(String app);
}
