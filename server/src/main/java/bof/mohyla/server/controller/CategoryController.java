package bof.mohyla.server.controller;

import java.util.*;

import bof.mohyla.server.exception.CategoryExceptionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import bof.mohyla.server.model.Category;

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
           throw new CategoryExceptionController.CategoryNotFoundException();
        }

        Category category = searchResult.get();

        return category;
    }

    @PostMapping("/api/v1/categories/")
    public Category createNewCategory(@RequestBody Category newCategory){
        boolean isEmptyTitle = newCategory.getName() == null ||
                newCategory.getName().isEmpty();

        if(isEmptyTitle) {
            ArrayList<Object> errorList = new ArrayList<>();
            HashMap<String, String> error = new HashMap<>();
            error.put("name", "is required");
            errorList.add(error);

            throw new CategoryExceptionController.CategoryInvalidArgumentsException(errorList);
        }

        categoryRepository.save(newCategory);

        return newCategory;
    }

    @PutMapping("/api/v1/categories/{id}")
    public Category editCategory(@PathVariable UUID id, @RequestBody Category updatedCategory) {
        Optional<Category> searchResult = categoryRepository.findById(id);

        if(searchResult.isEmpty()) {
            throw new CategoryExceptionController.CategoryNotFoundException();
        }

        boolean isEmptyTitle = updatedCategory.getName() == null ||
                updatedCategory.getName().isEmpty();

        if(isEmptyTitle) {
            ArrayList<Object> errorList = new ArrayList<>();
            HashMap<String, String> error = new HashMap<>();
            error.put("name", "is required");
            errorList.add(error);

            throw new CategoryExceptionController.CategoryInvalidArgumentsException(errorList);
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
