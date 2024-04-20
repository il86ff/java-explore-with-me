package ru.practicum.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDTO;
import ru.practicum.categories.dto.NewCategoryDTO;
import ru.practicum.categories.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO addCategory(@RequestBody @Valid NewCategoryDTO newCategoryDto) {

        log.info("Calling addCategory: /admin/categories with 'newCategoryDto':{}", newCategoryDto.toString());
        return categoryService.add(newCategoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDTO updateCategory(@PathVariable Long catId,
                                      @RequestBody @Valid NewCategoryDTO newCategoryDto) {

        log.info("Calling updateCategory: /admin/categories with 'categoryDto': {}", newCategoryDto.toString());
        return categoryService.update(catId, newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {

        log.info("Calling deleteCategory: /admin/categories/{catId} with 'categoryId': {}", catId);
        categoryService.delete(catId);
    }
}