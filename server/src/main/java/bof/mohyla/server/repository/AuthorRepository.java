package bof.mohyla.server.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import bof.mohyla.server.bean.Author;


public interface AuthorRepository extends JpaRepository<Author, UUID> {
    
}
