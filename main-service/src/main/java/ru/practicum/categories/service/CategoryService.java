package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.dto.CategoryDTO;
import ru.practicum.categories.dto.NewCategoryDTO;
import ru.practicum.categories.entity.Category;
import ru.practicum.categories.mapper.CategoryMapper;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.ObjectNotFoundException;
import ru.practicum.exceptions.RequestConflictException;
import ru.practicum.exceptions.SQLConstraintViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryDTO add(NewCategoryDTO newCategoryDto) {

        Category category = categoryMapper.newCategoryDtoToCategory(newCategoryDto);

        try {
            category = categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new SQLConstraintViolationException("Category with name = " + newCategoryDto.getName() + " already exists.");
        }

        return categoryMapper.categoryToCategoryDto(category);
    }

    @Transactional
    public CategoryDTO update(Long catId, NewCategoryDTO categoryDto) {

        Category category = categoryRepository.findById(catId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Category with id = " + catId + " doesn't exist.");
        });

        category.setName(categoryDto.getName());

        try {
            category = categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new SQLConstraintViolationException("Category with name = " + categoryDto.getName() + " already exists.");
        }

        return categoryMapper.categoryToCategoryDto(category);
    }

    @Transactional(readOnly = true)
    public CategoryDTO get(Long catId) {

        Category category = categoryRepository.findById(catId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Category with id = " + catId + " doesn't exist.");
        });

        return categoryMapper.categoryToCategoryDto(category);
    }

    @Transactional
    public void delete(Long catId) {

        if (eventRepository.existsByCategoryId(catId)) {
            throw new RequestConflictException("Failed to delete category. It must not be assigned to any event.");
        }

        try {
            categoryRepository.deleteById(catId);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Category with id = " + catId + " doesn't exist.");
        }
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> getAll(Integer from, Integer size) {

        Sort sort = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(from / size, size, sort);

        List<CategoryDTO> categories = categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::categoryToCategoryDto)
                .collect(Collectors.toList());

        return (!categories.isEmpty()) ? categories : new ArrayList<>();
    }
}
