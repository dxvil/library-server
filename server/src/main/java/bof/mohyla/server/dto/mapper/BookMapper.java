package bof.mohyla.server.dto.mapper;

import bof.mohyla.server.model.Author;
import bof.mohyla.server.model.Book;
import bof.mohyla.server.model.Category;
import bof.mohyla.server.dto.BookDTO;
import bof.mohyla.server.dto.BookResDTO;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    public Book toBook(BookDTO book, Author author, Category category) {
        return new Book(
                book.getTitle(),
                book.getDescription(),
                category,
                author,
                book.isBorrowed()
        );
    }

    public BookResDTO toDTO(Book book) {
        return new BookResDTO(
                book.getId(),
                book.getAuthor().getId(),
                book.getCategory().getId(),
                book.getTitle(),
                book.getDescription(),
                book.isBorrowed()
        );
    }
}
