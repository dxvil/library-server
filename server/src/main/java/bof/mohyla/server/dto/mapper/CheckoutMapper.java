package bof.mohyla.server.dto.mapper;

import bof.mohyla.server.bean.Book;
import bof.mohyla.server.bean.LibraryCheckout;
import bof.mohyla.server.bean.User;
import bof.mohyla.server.dto.CheckoutReqDTO;
import bof.mohyla.server.dto.CheckoutResDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CheckoutMapper {

    public CheckoutResDTO toCheckoutRes(LibraryCheckout libraryCheckout) {
        return new CheckoutResDTO(
                libraryCheckout.getId(),
                libraryCheckout.getUser().getId(),
                libraryCheckout.getBook().getId(),
                libraryCheckout.getStartDate(),
                libraryCheckout.getEndDate(),
                libraryCheckout.isReturned()
        );
    }

    public LibraryCheckout toCheckout(CheckoutReqDTO checkoutReqDTO, Book book, User user, LocalDate endDate, boolean isReturned) {
        return new LibraryCheckout(
                book,
                user,
                checkoutReqDTO.getStartDate(),
                endDate, isReturned);
    }
}
