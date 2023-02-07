package bof.mohyla.server.dto;

import bof.mohyla.server.model.LibraryCheckout;

import java.time.LocalDate;
import java.util.UUID;

public class CheckoutReqDTO extends LibraryCheckout {
    private UUID userId;
    private UUID bookId;
    private LocalDate startDate;

    public CheckoutReqDTO(UUID bookId, UUID userId, LocalDate startDate) {
        this.userId = userId;
        this.bookId = bookId;
        this.startDate = startDate;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
