package ru.practicum.categories.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.practicum.categories.dto.CategoryDTO;
import ru.practicum.categories.dto.NewCategoryDTO;
import ru.practicum.categories.entity.Category;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO categoryToCategoryDto(Category category);

    @Mapping(target = "id", ignore = true)
    Category newCategoryDtoToCategory(NewCategoryDTO newCategoryDto);

    Category categoryDtoToCategory(CategoryDTO categoryDto);
}
