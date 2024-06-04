package com.example.uade.tpo.controller;

import com.example.uade.tpo.dtos.request.CategoryRequestDto;
import com.example.uade.tpo.dtos.response.CategoryResponseDto;
import com.example.uade.tpo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")//Get all categories
    public ResponseEntity<List<CategoryResponseDto>> getCategories() {
        List<CategoryResponseDto> categories = categoryService.getCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")//Get category by id
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long categoryId) {
        Optional<CategoryResponseDto> category = categoryService.getCategoryById(categoryId);
        return category.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping//Create category
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryRequestDto categoryDto) {
        CategoryResponseDto newCategory = categoryService.createCategory(categoryDto);
        if (newCategory == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")//Update category
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long categoryId,
                                                              @RequestBody CategoryRequestDto categoryDetails) {
        CategoryResponseDto updatedCategory = categoryService.updateCategory(categoryId, categoryDetails);
        if (updatedCategory == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")//Delete category
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        Boolean deleted = categoryService.deleteCategory(categoryId);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{categoryId}/product/{productId}")//Add product to category
    public ResponseEntity<Void> addProductToCategory(@PathVariable Long categoryId, @PathVariable Long productId) {
        Boolean add = categoryService.addProductToCategory(categoryId, productId);
        if (!add) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
