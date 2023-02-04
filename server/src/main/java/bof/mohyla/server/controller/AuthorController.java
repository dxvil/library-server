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
        Optional<Author> author = authorRepository.findById(id);

        if(author.isEmpty()) {
            throw new RuntimeException("Author with id: " + id + " is not found");
        }

        return author.get();
    }

    @PostMapping("/api/v1/authors")
    public Author createAuthor(@RequestBody Author author) {
        return authorRepository.save(author);
    }

    @PutMapping("/api/v1/authors/{id}")
    public Author editAuthor(@PathVariable UUID id, @RequestBody Author author) {
        Optional<Author> searchRes = authorRepository.findById(id);
        
        if(searchRes.isEmpty()) {
            throw new RuntimeException("Author with id: " + id + " is not found");
        }

        searchRes.get().setName(author.getName());
        authorRepository.save(searchRes.get());

        return searchRes.get();
    }

    @DeleteMapping("/api/v1/authors/{id}")
    public void deleteAuthor(@PathVariable UUID id) {
        authorRepository.deleteById(id);
    }
    
}
