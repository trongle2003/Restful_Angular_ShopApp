package com.project.shopapp.controller;

import com.project.shopapp.domain.dtos.CategoryDTO;
import com.project.shopapp.domain.entity.Category;
import com.project.shopapp.domain.response.ResultPaginationDTO;
import com.project.shopapp.service.CategoryService;
import com.project.shopapp.ultil.anotation.ApiMessage;
import com.project.shopapp.ultil.error.InvalidException;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    @ApiMessage("Get All Categories")
    public ResponseEntity<ResultPaginationDTO> getAllCategory(@RequestParam("page") Optional<String> page, @RequestParam("size") Optional<String> size) {
        String sCurrent = page.isPresent() ? page.get() : "";
        String sPageSize = size.isPresent() ? size.get() : "";

        int current = Integer.parseInt(sCurrent) - 1;
        int pageSize = Integer.parseInt(sPageSize);

        Pageable pageable = PageRequest.of(current, pageSize);
        return ResponseEntity.ok().body(this.categoryService.handleGetAllCategory(pageable));
    }

    @PostMapping("")
    @ApiMessage("Create Category")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult bindingResult) throws InvalidException {
        if (bindingResult.hasErrors()) {
            throw new InvalidException(bindingResult.getFieldError().getDefaultMessage().toString());
        }
        Category newCategory = this.categoryService.handleCreateCategory(categoryDTO);
        return ResponseEntity.ok(newCategory);
    }

    @PutMapping("/{id}")
    @ApiMessage("Update Category")
    public ResponseEntity<?> updateCategory(@PathVariable long id, @RequestBody CategoryDTO categoryDTO) throws InvalidException {
        boolean check = this.categoryService.handleCheckCategory(id);
        if (!check) {
            throw new InvalidException("Not update because id invalid");
        }
        Category updateCategory = this.categoryService.handleUpdateCategory(categoryDTO, id);
        return ResponseEntity.ok().body(updateCategory);
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete Category")
    public ResponseEntity<String> deleteCategory(@PathVariable long id) throws InvalidException {
        boolean check = this.categoryService.handleCheckCategory(id);
        if (!check) {
            throw new InvalidException("Id invalid");
        }
        this.categoryService.handleDeleteCategory(id);
        return ResponseEntity.ok().body("Delete successfully id " + id);
    }
}
