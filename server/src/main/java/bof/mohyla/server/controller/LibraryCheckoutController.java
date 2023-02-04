package bof.mohyla.server.controller;

import bof.mohyla.server.bean.LibraryCheckout;
import bof.mohyla.server.exception.LibraryCheckoutExceptionController;
import bof.mohyla.server.repository.LibraryCheckoutRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class LibraryCheckoutController {
    LibraryCheckoutRepository libraryRepository;

    @GetMapping("/api/v1/libCheckout/")
    public List<LibraryCheckout> getListOfCheckout() {
        return libraryRepository.findAll();
    }
    @GetMapping("/api/v1/libCheckout/{id}")
    public LibraryCheckout getSingleCheckout(@PathVariable UUID id) {
        Optional<LibraryCheckout> searchResult = libraryRepository.findById(id);

        if(searchResult.isEmpty()) {
            throw new LibraryCheckoutExceptionController.LibraryCheckoutNotFoundException();
        }

        LibraryCheckout checkout = searchResult.get();

        return checkout;
    }
    @PostMapping("/api/v1/libCheckout/")
    public LibraryCheckout checkout(@RequestBody LibraryCheckout libraryCheckout) {
        boolean isEmptyUser = libraryCheckout.getUser() == null;
        boolean isEmptyBook = libraryCheckout.getBook() == null;
        boolean isEmptyStartDate = libraryCheckout.getStartDate() == null;

        if(isEmptyBook || isEmptyUser || isEmptyStartDate) {

        }
        return libraryCheckout;
    }

    @PutMapping("/api/v1/libCheckout/{id}")


    @DeleteMapping("/api/v1/libCheckout/{id}")
    public void deleteCheckout(@PathVariable UUID id) {
        try {
            libraryRepository.deleteById(id);
        } catch(LibraryCheckoutExceptionController.LibraryCheckoutNotFoundException ex) {
            throw new LibraryCheckoutExceptionController.LibraryCheckoutNotFoundException();
        }
    }
}
