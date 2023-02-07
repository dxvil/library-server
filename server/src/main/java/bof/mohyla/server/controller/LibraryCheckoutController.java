package bof.mohyla.server.controller;

import bof.mohyla.server.model.Book;
import bof.mohyla.server.model.LibraryCheckout;
import bof.mohyla.server.model.User;
import bof.mohyla.server.dto.CheckoutReqDTO;
import bof.mohyla.server.dto.CheckoutResDTO;
import bof.mohyla.server.dto.mapper.CheckoutMapper;
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
import java.util.stream.Collectors;

@RestController
public class LibraryCheckoutController {
    @Autowired
    LibraryCheckoutRepository libraryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookRepository bookRepository;

    @Autowired
    CheckoutMapper mapper;

    @GetMapping("/api/v1/libCheckout/")
    public List<CheckoutResDTO> getListOfCheckout() {
        return libraryRepository.findAll().stream().map(checkout -> mapper.toCheckoutRes(checkout)).collect(Collectors.toList());
    }
    @GetMapping("/api/v1/libCheckout/{id}")
    public CheckoutResDTO getSingleCheckout(@PathVariable UUID id) {
        Optional<LibraryCheckout> searchResult = libraryRepository.findById(id);

        if(searchResult.isEmpty()) {
            throw new LibraryCheckoutExceptionController.LibraryCheckoutNotFoundException();
        }

        LibraryCheckout checkout = searchResult.get();

        return mapper.toCheckoutRes(checkout);
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

        Book book = searchBookResult.get();
        User user = searchUserResult.get();

        if(book.isBorrowed()) {
            ArrayList<Object> errorList = new ArrayList<>();
            HashMap<String, String> error = new HashMap<>();
            error.put("bookId", "The book is already borrowed by other user");
            errorList.add(error);

            throw new LibraryCheckoutExceptionController.LibraryCheckoutInvalidArgumentsException(errorList);
        }

        //give a book for 2 months
        LocalDate endDate = libraryCheckout.getStartDate().plusMonths(2);

        book.setBorrowed(true);

        LibraryCheckout checkout = mapper.toCheckout(libraryCheckout, book, user, endDate, false);

        bookRepository.save(book);
        libraryRepository.save(checkout);

        //create res based on dto
        return mapper.toCheckoutRes(checkout);
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

        return mapper.toCheckoutRes(checkout);
    }
}
