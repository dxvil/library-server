package bof.mohyla.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import bof.mohyla.server.repository.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import bof.mohyla.server.bean.Book;

@RestController
public class BookContoller {
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/api/v1/books/")
    public List<Book> getListOfBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/api/v1/books/{id}")
    public Book getSingleBook(@PathVariable UUID id) {
        Optional<Book> searchResult = bookRepository.findById(id);

        if(searchResult.isEmpty()) {
            throw new RuntimeException("The book with id: " + id + " is not found.");
        }

        Book book = searchResult.get();

        return book;
    }

    @PostMapping("/api/v1/books/")
    public Book createNewBook(@RequestBody Book newBook) {
        bookRepository.save(newBook);

        return newBook;
    }

    @PutMapping("/api/v1/books/{id}")
    public Book editBook(@PathVariable UUID id, @RequestBody Book updatedBook) {
        Optional<Book> searchResult = bookRepository.findById(id);

        if(searchResult.isEmpty()) {
            throw new RuntimeException("The book with id: " + id + " is not found.");
        }

        Book book = searchResult.get();
        book.setDescription(updatedBook.getDescription());
        book.setTitle(updatedBook.getTitle());

        bookRepository.save(book);
        return book;
    }

    @DeleteMapping("/api/v1/books/{id}")
    public void deleteBook(@PathVariable UUID id) {
        bookRepository.deleteById(id);
    }
}
