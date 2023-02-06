package bof.mohyla.server.bean;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class LibraryCheckout {
    @Id
    @GeneratedValue
    private UUID id;
    @OneToOne(cascade = {CascadeType.ALL})
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne(cascade = {CascadeType.ALL})
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "book_id")
    private Book book;
    public User getUser() {
        return user;
    }
    private LocalDate startDate;
    private LocalDate endDate;
    public boolean isReturned;

    public LibraryCheckout(){};

    public LibraryCheckout(
            LocalDate startTime,
            LocalDate endTime,
            Book bookId,
            User userId,
            boolean isReturned
    ) {
        this.startDate = startTime;
        this.endDate = endTime;
        this.book = bookId;
        this.user = userId;
        this.isReturned = isReturned;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    public UUID getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
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

    public void setUser(User user) {
        this.user = user;
    }
}
