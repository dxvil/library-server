package bof.mohyla.server.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import bof.mohyla.server.bean.Category;

import bof.mohyla.server.repository.CategoryRepository;

@RestController
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/api/v1/categories/")
    public List<Category> getListOfCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/api/v1/categories/{id}")
    public Category getSingleCategory(@PathVariable UUID id) {
        Optional<Category> searchResult = categoryRepository.findById(id);

        if(searchResult.isEmpty()) {
            throw new RuntimeException("Category with id: " + id + " is not found.");
        }

        Category category = searchResult.get();

        return category;
    }

    @PostMapping("/api/v1/categories/")
    public Category createNewCategory(@RequestBody Category newCategory){
        categoryRepository.save(newCategory);

        return newCategory;
    }

    @PutMapping("/api/v1/categories/{id}")
    public Category editCategory(@PathVariable UUID id, @RequestBody Category updatedCategory) {
        Optional<Category> searchResult = categoryRepository.findById(id);

        if(searchResult.isEmpty()) {
            throw new RuntimeException("Category with id: " + id + " is not found.");
        }

        Category category = searchResult.get();
        category.setName(updatedCategory.getName());

        categoryRepository.save(category);
        return category;
    }

    @DeleteMapping("/api/v1/categories/{id}")
    public void deleteCategory(@PathVariable UUID id) {
        categoryRepository.deleteById(id);
    }
}
