package com.project.shopapp.service;

import com.project.shopapp.domain.dtos.CategoryDTO;
import com.project.shopapp.domain.entity.Category;
import com.project.shopapp.domain.response.ResultPaginationDTO;
import com.project.shopapp.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ResultPaginationDTO handleGetAllCategory(Pageable pageable) {
        Page<Category> page = this.categoryRepository.findAll(pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(page.getNumber() + 1);
        meta.setPageSize(page.getSize());

        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());

        result.setMeta(meta);
        result.setResult(page.getContent());
        return result;
    }

    public Category handleGetCategoryById(long id) {
        return this.categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category handleCreateCategory(CategoryDTO categoryDTO) {
        Category newCategory = new Category();
        newCategory.setName(categoryDTO.getName());
        return this.categoryRepository.save(newCategory);
    }

    public Category handleUpdateCategory(CategoryDTO categoryDTO, long id) {
        Category checkCategory = handleGetCategoryById(id);
        checkCategory.setName(categoryDTO.getName());
        return this.categoryRepository.save(checkCategory);
    }

    public void handleDeleteCategory(long id) {
        this.categoryRepository.deleteById(id);
    }

    public boolean handleCheckCategory(long id) {
        return this.categoryRepository.existsById(id);
    }
}
