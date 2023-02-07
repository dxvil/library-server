package bof.mohyla.server.controller;

import java.util.*;

import bof.mohyla.server.exception.AuthorExceptionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import bof.mohyla.server.model.Author;
import bof.mohyla.server.repository.AuthorRepository;

@RestController
public class AuthorController {
    @Autowired private AuthorRepository authorRepository;

    @GetMapping("/api/v1/authors/")
    public List<Author> getListOfAuthors() {
        return authorRepository.findAll();
    }

    @GetMapping("/api/v1/authors/{id}")
    public Author getSingleAuthor(@PathVariable UUID id) {
        Optional<Author> searchResult = authorRepository.findById(id);

        if(searchResult.isEmpty()) {
            throw new AuthorExceptionController.AuthorNotFoundException();
        }

        Author author = searchResult.get();

        return author;
    }

    @PostMapping("/api/v1/authors/")
    public Author createAuthor(@RequestBody Author author) {
        boolean isEmptyName = author.getName() == null || author.getName().isEmpty();

        if(isEmptyName) {
            ArrayList<Object> errorList = new ArrayList<>();
            HashMap<String, String> error = new HashMap<>();
            error.put("name", "is required");
            errorList.add(error);

            throw new AuthorExceptionController.AuthorInvalidArgumentsException(
                    errorList
            );
        }

        return authorRepository.save(author);
    }

    @PutMapping("/api/v1/authors/{id}")
    public Author editAuthor(@PathVariable UUID id, @RequestBody Author updatedAuthor) {
        Optional<Author> searchResult = authorRepository.findById(id);
        
        if(searchResult.isEmpty()) {
            throw new AuthorExceptionController.AuthorNotFoundException();
        }

        boolean isEmptyName = updatedAuthor.getName() == null
                || updatedAuthor.getName().isEmpty();

        if(isEmptyName) {
            ArrayList<Object> errorList = new ArrayList<>();
            HashMap<String, String> error = new HashMap<>();
            error.put("name", "is required");
            errorList.add(error);

            throw new AuthorExceptionController.AuthorInvalidArgumentsException(
                    errorList
            );
        }

        Author author = searchResult.get();
        author.setName(updatedAuthor.getName());

        authorRepository.save(author);
        return author;
    }

    @DeleteMapping("/api/v1/authors/{id}")
    public void deleteAuthor(@PathVariable UUID id) {
        authorRepository.deleteById(id);
    }
    
}
