package bof.mohyla.server.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import bof.mohyla.server.bean.User;

public interface UserRepository extends JpaRepository<User, UUID>{
    
}
