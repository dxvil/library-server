package bof.mohyla.server.dto;

import java.time.LocalDate;
import java.util.UUID;

public class CheckoutReqDTO {
    private UUID userId;
    private UUID bookId;
    private LocalDate startDate;

    public UUID getUserId() {
        return userId;
    }

    public UUID getBookId() {
        return bookId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
