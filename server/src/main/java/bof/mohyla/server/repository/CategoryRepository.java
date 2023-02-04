package bof.mohyla.server.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import bof.mohyla.server.bean.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    
}
