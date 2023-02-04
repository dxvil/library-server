package bof.mohyla.server.bean;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
public class Book {
    @Id
    @GeneratedValue
    private UUID id;
    private String title;
    private String description;
    private boolean isBorrowed;
    //relations
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "author_id")
    private Author author;

    public Book() {}

    public Book(String title, String description, Category category, Author author, boolean isBorrowed){
        super();
        this.title = title;
        this.description = description;
        this.category = category;
        this.author = author;
        this.isBorrowed = isBorrowed;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public UUID getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
