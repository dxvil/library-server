package bof.mohyla.server.bean;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Book {
    @Id
    @GeneratedValue
    private UUID id;
    private String title;
    private String description;
    //relations
    // private Category category;
    // private Author author;

    public Book() {}

    public Book(String title, String description){
        super();
        this.title = title;
        this.description = description;
        // this.category = category;
        // this.author = author;
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

    // public Category getCategory() {
    //     return this.category;
    // }

    // public void setCategory(Category category) {
    //     this.category = category;
    // }

    // public Author getAuthor() {
    //     return this.author;
    // }

    // public void setAuthor(Author author) {
    //     this.author = author;
    // }


    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
