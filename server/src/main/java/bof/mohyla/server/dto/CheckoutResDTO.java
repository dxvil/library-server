package bof.mohyla.server.dto;

import jakarta.persistence.GeneratedValue;

import java.time.LocalDate;
import java.util.UUID;

public class CheckoutResDTO {
    private UUID id;
    private UUID userId;
    private UUID bookId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isReturned;

    public CheckoutResDTO(UUID id, UUID userId, UUID bookId, LocalDate startDate, LocalDate endDate, boolean isReturned) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isReturned = isReturned;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }
}
