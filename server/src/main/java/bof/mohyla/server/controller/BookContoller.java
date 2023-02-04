package bof.mohyla.server.controller;

import bof.mohyla.server.exception.BookExceptionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import bof.mohyla.server.repository.BookRepository;

import java.util.*;

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
            throw new BookExceptionController.BookNotFoundException();
        }

        Book book = searchResult.get();

        return book;
    }

    @PostMapping("/api/v1/books/")
    public Book createNewBook(@RequestBody Book newBook) {
        boolean isEmptyTitle =newBook.getTitle() == null || newBook.getTitle().isEmpty();
        boolean isEmptyAuthor = newBook.getAuthor() == null;
        boolean isEmptyCategory = newBook.getCategory() == null;

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

        //when you are creating a book, it cannot be immediately be borrowed
        newBook.setBorrowed(false);

        bookRepository.save(newBook);

        return newBook;
    }

    @PutMapping("/api/v1/books/{id}")
    public Book editBook(@PathVariable UUID id, @RequestBody Book updatedBook) {
        Optional<Book> searchResult = bookRepository.findById(id);

        if(searchResult.isEmpty()) {
            throw new BookExceptionController.BookNotFoundException();
        }

        boolean isEmptyTitle =updatedBook.getTitle() == null || updatedBook.getTitle().isEmpty();
        boolean isEmptyDescription = updatedBook.getDescription() == null ||
                updatedBook.getDescription().isEmpty();
        boolean isEmptyAuthor = updatedBook.getAuthor() == null;
        boolean isEmptyCategory = updatedBook.getCategory() == null;

        if(isEmptyTitle && isEmptyAuthor && isEmptyCategory && isEmptyDescription) {
            ArrayList<Object> errorList = new ArrayList<>();
            HashMap<String, String> error = new HashMap<>();

            error.put("Missing fields", "Title, description, author, category");

            errorList.add(error);

            String message = "At least one value should be not empty";

            throw new BookExceptionController.BookInvalidArgumentsException(errorList, message);
        }

        Book book = searchResult.get();

        if(!isEmptyTitle) {
            book.setTitle(updatedBook.getTitle());
        }
        if(!isEmptyDescription) {
            book.setDescription(updatedBook.getDescription());
        }
        if(!isEmptyCategory) {
            book.setCategory(updatedBook.getCategory());
        }
        if(!isEmptyAuthor) {
            book.setAuthor(updatedBook.getAuthor());
        }

        // you cannot change status of book from this service
        // you can do it throw checkout service
        book.setBorrowed(book.isBorrowed());

        bookRepository.save(book);
        return book;
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
