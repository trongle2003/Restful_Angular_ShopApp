package com.project.shopapp.controller;

import com.project.shopapp.entity.Category;
import com.project.shopapp.service.CategoryService;
import com.project.shopapp.ultil.anotation.ApiMessage;
import com.project.shopapp.ultil.error.InvalidException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    @ApiMessage("Get All Categories")
    public ResponseEntity<List<Category>> getAllCategory(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        List<Category> category = this.categoryService.handleGetAllCategory();
        return ResponseEntity.ok().body(category);
    }

    @PostMapping("")
    @ApiMessage("Create Category")
    public ResponseEntity<?> createCategory(@Valid @RequestBody Category category, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errorMessages = bindingResult
                        .getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Category newCategory = this.categoryService.handleCreateCategory(category);
            return ResponseEntity.ok("Category created successfully" + "\n" + newCategory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ApiMessage("Update Category")
    public ResponseEntity<Category> updateCategory(@PathVariable long id, @RequestBody Category category) throws InvalidException {
        boolean check = this.categoryService.handleCheckCategory(id);
        if (!check) {
            throw new InvalidException("Not update because id invalid");
        }
        Category updateCategory = this.categoryService.handleUpdateCategory(category);
        return ResponseEntity.ok().body(updateCategory);
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete Category")
    public ResponseEntity<String> deleteCategory(@PathVariable long id) {
        this.categoryService.handleDeleteCategory(id);
        return ResponseEntity.ok().body("Delete successfully id " + id);
    }
}
