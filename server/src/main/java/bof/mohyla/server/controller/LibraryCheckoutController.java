package bof.mohyla.server.controller;

import bof.mohyla.server.bean.Book;
import bof.mohyla.server.bean.LibraryCheckout;
import bof.mohyla.server.bean.User;
import bof.mohyla.server.dto.CheckoutReqDTO;
import bof.mohyla.server.dto.CheckoutResDTO;
import bof.mohyla.server.exception.BookExceptionController;
import bof.mohyla.server.exception.LibraryCheckoutExceptionController;
import bof.mohyla.server.exception.UserExceptionController;
import bof.mohyla.server.repository.BookRepository;
import bof.mohyla.server.repository.LibraryCheckoutRepository;
import bof.mohyla.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
public class LibraryCheckoutController {
    @Autowired
    LibraryCheckoutRepository libraryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookRepository bookRepository;

    @GetMapping("/api/v1/libCheckout/")
    public List<LibraryCheckout> getListOfCheckout() {
        return libraryRepository.findAll();
    }
    @GetMapping("/api/v1/libCheckout/{id}")
    public CheckoutResDTO getSingleCheckout(@PathVariable UUID id) {
        Optional<LibraryCheckout> searchResult = libraryRepository.findById(id);

        if(searchResult.isEmpty()) {
            throw new LibraryCheckoutExceptionController.LibraryCheckoutNotFoundException();
        }

        LibraryCheckout checkout = searchResult.get();

        CheckoutResDTO res = new CheckoutResDTO();

        res.setUserId(checkout.getUser().getId());
        res.setBookId(checkout.getBook().getId());
        res.setId(checkout.getId());
        res.setReturned(checkout.isReturned());
        res.setStartDate(checkout.getStartDate());
        res.setEndDate(checkout.getEndDate());

        return res;
    }
    @PostMapping("/api/v1/libCheckout/")
    public CheckoutResDTO borrowBook(@RequestBody CheckoutReqDTO libraryCheckout) {
        boolean isEmptyUser = libraryCheckout.getUserId() == null;
        boolean isEmptyBook = libraryCheckout.getBookId() == null;
        boolean isEmptyStartDate = libraryCheckout.getStartDate() == null;

        if(isEmptyBook || isEmptyUser || isEmptyStartDate) {
            ArrayList<Object> errorList = new ArrayList<>();
            HashMap<String, String> error = new HashMap<>();

            if(isEmptyUser) {
                error.put("userId", "is required");
            }
            if(isEmptyBook) {
                error.put("bookId", "is required");
            }
            if(isEmptyStartDate) {
                error.put("startDate", "is required");
            }

            if(libraryCheckout.getStartDate().isBefore(LocalDate.now())) {
                error.put("startDate", "is must be today or in the future");
            }

            errorList.add(error);

            throw new LibraryCheckoutExceptionController.LibraryCheckoutInvalidArgumentsException(errorList);
        }

        Optional<User> searchUserResult = userRepository.findById(libraryCheckout.getUserId());
        Optional<Book> searchBookResult = bookRepository.findById(libraryCheckout.getBookId());

        if(searchUserResult.isEmpty()) {
            throw new UserExceptionController.UserNotFoundException();
        }

        if(searchBookResult.isEmpty()) {
            throw new BookExceptionController.BookNotFoundException();
        }

        //give a book for 2 months
        LocalDate endDate = libraryCheckout.getStartDate().plusMonths(2);
        Book book = searchBookResult.get();
        User user = searchUserResult.get();

        if(book.isBorrowed()) {
            ArrayList<Object> errorList = new ArrayList<>();
            HashMap<String, String> error = new HashMap<>();
            error.put("bookId", "The book is already borrowed by other user");
            errorList.add(error);

            throw new LibraryCheckoutExceptionController.LibraryCheckoutInvalidArgumentsException(errorList);
        }

        LibraryCheckout checkout = new LibraryCheckout();

        checkout.setBook(book);
        checkout.setUser(user);
        checkout.setStartDate(libraryCheckout.getStartDate());
        checkout.setEndDate(endDate);
        checkout.setReturned(false);

        book.setBorrowed(true);

        bookRepository.save(book);
        libraryRepository.save(checkout);

        //create res based on dto
        CheckoutResDTO res = new CheckoutResDTO();

        res.setUserId(libraryCheckout.getUserId());
        res.setBookId(libraryCheckout.getBookId());
        res.setId(checkout.getId());
        res.setReturned(checkout.isReturned());
        res.setStartDate(checkout.getStartDate());
        res.setEndDate(checkout.getEndDate());

        return res;
    }

    @PutMapping("/api/v1/libCheckout/{id}")
    public CheckoutResDTO returnBook(@PathVariable UUID id) {
        if(id == null) {
            ArrayList<Object> errorList = new ArrayList<>();
            HashMap<String, String> error = new HashMap<>();
            error.put("id", "is required in request path");
            errorList.add(error);

            throw new LibraryCheckoutExceptionController.LibraryCheckoutInvalidArgumentsException(errorList);
        }

        Optional<LibraryCheckout> searchResult = libraryRepository.findById(id);

        if(searchResult.isEmpty()) {
            throw new LibraryCheckoutExceptionController.LibraryCheckoutNotFoundException();
        }

        LibraryCheckout checkout = searchResult.get();
        Book book = checkout.getBook();

        book.setBorrowed(false);
        checkout.setReturned(true);

        LocalDate now = LocalDate.now();

        if(checkout.getEndDate().isAfter(now)) {
            //provide some tax logic
        }

        libraryRepository.save(checkout);
        bookRepository.save(book);

        CheckoutResDTO res = new CheckoutResDTO();
        res.setUserId(checkout.getUser().getId());
        res.setBookId(checkout.getBook().getId());
        res.setId(checkout.getId());
        res.setReturned(checkout.isReturned());
        res.setStartDate(checkout.getStartDate());
        res.setEndDate(checkout.getEndDate());

        return res;
    }
}
