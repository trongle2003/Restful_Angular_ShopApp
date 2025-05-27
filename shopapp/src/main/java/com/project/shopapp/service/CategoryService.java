package com.project.shopapp.service;

import com.project.shopapp.entity.Category;
import com.project.shopapp.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> handleGetAllCategory() {
        return this.categoryRepository.findAll();
    }

    public Category handleCreateCategory(Category category) {
        return this.categoryRepository.save(category);
    }

    public Category handleUpdateCategory(Category category) {
        return this.categoryRepository.save(category);
    }

    public void handleDeleteCategory(long id) {
        this.categoryRepository.deleteById(id);
    }

    public boolean handleCheckCategory(long id) {
        return this.categoryRepository.existsById(id);
    }
}
