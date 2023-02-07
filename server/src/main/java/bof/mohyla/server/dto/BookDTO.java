package bof.mohyla.server.dto;

import java.util.UUID;

public class BookDTO {
    private UUID authorId;
    private UUID categoryId;
    private String title;
    private String description;
    private boolean isBorrowed;

    public BookDTO(UUID authorId, UUID categoryId, String title, String description, boolean isBorrowed) {
        this.authorId = authorId;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.isBorrowed = isBorrowed;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }
}
