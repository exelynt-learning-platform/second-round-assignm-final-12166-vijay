package com.Ecom.Services;



import com.Ecom.Dtos.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto create(CategoryDto categoryDto);

    List<CategoryDto> getAll();

    CategoryDto get(Long categoryId);

    CategoryDto update(Long categoryId, CategoryDto categoryDto);

    void delete(Long categoryId);
}