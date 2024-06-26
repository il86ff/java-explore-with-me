package ru.practicum.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDTO;
import ru.practicum.categories.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDTO> get(@RequestParam(defaultValue = "0") Integer from,
                                 @RequestParam(defaultValue = "10") Integer size) {

        log.info("Calling get: /categories with 'from': {}, 'size': {}", from, size);
        return categoryService.getAll(from, size);
    }

    @GetMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO getById(@PathVariable Long categoryId)  {

        log.info("Calling getById: /categories/{categoryId} with 'categoryId': {}", categoryId);
        return categoryService.get(categoryId);
    }
}
