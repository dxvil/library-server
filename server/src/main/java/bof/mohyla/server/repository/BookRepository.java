package bof.mohyla.server.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import bof.mohyla.server.model.Book;


public interface BookRepository extends JpaRepository<Book, UUID> {
    
}
