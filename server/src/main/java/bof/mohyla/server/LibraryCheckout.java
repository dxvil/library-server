package bof.mohyla.server;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class LibraryCheckout {
    @Id
    @GeneratedValue
    public UUID id;
    public LocalDate startTime;
    public LocalDate endTime;
    public UUID bookId;
    public UUID userId;
    public boolean isReturned;
    
    public LibraryCheckout(
        LocalDate startTime,
        LocalDate endTime,
        UUID bookId,
        UUID userId,
        boolean isReturned
    ) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.bookId = bookId;
        this.userId = userId;
        this.isReturned = isReturned;
    }


    public UUID getId() {
        return this.id;
    }

    public LocalDate getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return this.endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public UUID getBookId() {
        return this.bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public boolean isIsReturned() {
        return this.isReturned;
    }

    public boolean getIsReturned() {
        return this.isReturned;
    }

    public void setIsReturned(boolean isReturned) {
        this.isReturned = isReturned;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", bookId='" + getBookId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", isReturned='" + isIsReturned() + "'" +
            "}";
    }
}
