package bof.mohyla.server.repository;

import bof.mohyla.server.model.LibraryCheckout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LibraryCheckoutRepository extends JpaRepository<LibraryCheckout, UUID> {
}
