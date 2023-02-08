package bof.mohyla.server.controller;

import bof.mohyla.server.model.Author;
import bof.mohyla.server.model.Category;
import bof.mohyla.server.dto.BookDTO;
import bof.mohyla.server.dto.BookResDTO;
import bof.mohyla.server.dto.mapper.BookMapper;
import bof.mohyla.server.exception.AuthorExceptionController;
import bof.mohyla.server.exception.BookExceptionController;
import bof.mohyla.server.exception.CategoryExceptionController;
import bof.mohyla.server.repository.AuthorRepository;
import bof.mohyla.server.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import bof.mohyla.server.repository.BookRepository;

import java.util.*;
import java.util.stream.Collectors;

import bof.mohyla.server.model.Book;

@RestController
public class BookContoller {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookMapper mapper;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/api/v1/books/")
    public ResponseEntity<List<BookResDTO>> getListOfBooks() {
        return
                ResponseEntity.ok(bookRepository
                        .findAll()
                        .stream()
                        .map(book -> mapper.toDTO(book))
                        .collect(Collectors.toList())
                );
    }

    @GetMapping("/api/v1/books/{id}")
    public ResponseEntity<Book> getSingleBook(@PathVariable UUID id) {
        Optional<Book> searchResult = bookRepository.findById(id);

        if(searchResult.isEmpty()) {
            throw new BookExceptionController.BookNotFoundException();
        }

        Book book = searchResult.get();

        return ResponseEntity.ok(book);
    }

    @PostMapping("/api/v1/books/")
    public ResponseEntity<BookResDTO> createNewBook(@RequestBody BookDTO newBook) {
        boolean isEmptyTitle =newBook.getTitle() == null || newBook.getTitle().isEmpty();
        boolean isEmptyAuthor = newBook.getAuthorId() == null;
        boolean isEmptyCategory = newBook.getCategoryId() == null;

        if(isEmptyTitle || isEmptyAuthor || isEmptyCategory) {
            ArrayList<Object> errorList = new ArrayList<>();
            HashMap<String, String> error = new HashMap<>();

            if(isEmptyTitle) {
                error.put("title", "is required");
            }

            if(isEmptyAuthor) {
                error.put("author", "is required");
            }

            if(isEmptyCategory) {
                error.put("category", "is required");
            }

            errorList.add(error);

            String message = "Missing required values.";

            throw new BookExceptionController.BookInvalidArgumentsException(errorList, message);
        }

        Optional<Author> searchAuthorResult = authorRepository.findById(newBook.getAuthorId());
        Optional<Category> searchCategoryResult = categoryRepository.findById(newBook.getCategoryId());

        if(searchAuthorResult.isEmpty()) {
            throw new AuthorExceptionController.AuthorNotFoundException();
        }

        if(searchCategoryResult.isEmpty()) {
            throw new CategoryExceptionController.CategoryNotFoundException();
        }

        Author author = searchAuthorResult.get();
        Category category = searchCategoryResult.get();

        //when you are creating a book, it cannot be immediately be borrowed
        newBook.setBorrowed(false);

        Book book = mapper.toBook(newBook, author, category);

        bookRepository.save(book);

        return ResponseEntity.ok(mapper.toDTO(book));
    }

    @PutMapping("/api/v1/books/{id}")
    public ResponseEntity<BookResDTO> editBook(@PathVariable UUID id, @RequestBody BookDTO updatedBook) {
        Optional<Book> searchResult = bookRepository.findById(id);

        if(searchResult.isEmpty()) {
            throw new BookExceptionController.BookNotFoundException();
        }

        boolean isEmptyTitle =updatedBook.getTitle() == null || updatedBook.getTitle().isEmpty();
        boolean isEmptyDescription = updatedBook.getDescription() == null ||
                updatedBook.getDescription().isEmpty();
        boolean isEmptyAuthor = updatedBook.getAuthorId() == null;
        boolean isEmptyCategory = updatedBook.getCategoryId() == null;

        if(isEmptyTitle && isEmptyAuthor && isEmptyCategory && isEmptyDescription) {
            ArrayList<Object> errorList = new ArrayList<>();
            HashMap<String, String> error = new HashMap<>();

            error.put("Missing fields", "Title, description, author, category");

            errorList.add(error);

            String message = "At least one value should be not empty";

            throw new BookExceptionController.BookInvalidArgumentsException(errorList, message);
        }

        Book book = searchResult.get();

        if(isEmptyTitle) {
            updatedBook.setTitle(book.getTitle());
        }

        if(isEmptyDescription) {
            updatedBook.setDescription(book.getDescription());
        }

        Optional<Author> searchAuthorResult = authorRepository.findById(updatedBook.getAuthorId());
        Optional<Category> searchCategoryResult = categoryRepository.findById(updatedBook.getCategoryId());

        if(searchAuthorResult.isEmpty()) {
            throw new AuthorExceptionController.AuthorNotFoundException();
        }

        if(searchCategoryResult.isEmpty()) {
            throw new CategoryExceptionController.CategoryNotFoundException();
        }

        Author author = searchAuthorResult.get();
        Category category = searchCategoryResult.get();

        book = mapper.toBook(updatedBook, author, category);

        // you cannot change status of book from this service
        // you can do it throw checkout service
        book.setBorrowed(book.isBorrowed());

        bookRepository.save(book);

        return ResponseEntity.ok(mapper.toDTO(book));
    }

    @DeleteMapping("/api/v1/books/{id}")
    public void deleteBook(@PathVariable UUID id) {
        try {
            bookRepository.deleteById(id);
        } catch (BookExceptionController.BookNotFoundException ex){
                throw new BookExceptionController.BookNotFoundException();
        }
    }
}
