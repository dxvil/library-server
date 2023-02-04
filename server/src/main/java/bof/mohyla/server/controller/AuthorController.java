package bof.mohyla.server.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import bof.mohyla.server.bean.Author;
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
            throw new RuntimeException("Author with id: " + id + " is not found");
        }

        Author author = searchResult.get();

        return author;
    }

    @PostMapping("/api/v1/authors/")
    public Author createAuthor(@RequestBody Author author) {
        return authorRepository.save(author);
    }

    @PutMapping("/api/v1/authors/{id}")
    public Author editAuthor(@PathVariable UUID id, @RequestBody Author updatedAuthor) {
        Optional<Author> searchResult = authorRepository.findById(id);
        
        if(searchResult.isEmpty()) {
            throw new RuntimeException("Author with id: " + id + " is not found");
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
