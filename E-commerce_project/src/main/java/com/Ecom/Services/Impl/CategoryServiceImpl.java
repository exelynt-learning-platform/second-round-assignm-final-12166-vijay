package com.Ecom.Services.Impl;

import com.Ecom.Dtos.CategoryDto;
import com.Ecom.Entities.Category;
import com.Ecom.Exceptions.ResourceNotFoundException;
import com.Ecom.Repositories.CategoryRepository;
import com.Ecom.Services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        Category categoryEntity = modelMapper.map(categoryDto, Category.class);
        var savedCategoryEntity = categoryRepository.save(categoryEntity);
        return modelMapper.map(savedCategoryEntity, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories
                .stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .toList();

    }

    @Override
    public CategoryDto get(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with this " + categoryId));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto update(Long categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with this " + categoryId));
        category.setTitle(categoryDto.getTitle());

        category.setTitle(categoryDto.getTitle());

        Category updatedCategory = categoryRepository.save(category);
        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void delete(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with this " + categoryId));
        categoryRepository.delete(category);
    }
}