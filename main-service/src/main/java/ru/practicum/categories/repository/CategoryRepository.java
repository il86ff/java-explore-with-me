package ru.practicum.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.categories.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}