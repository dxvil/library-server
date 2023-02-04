package bof.mohyla.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LibraryCheckoutExceptionController extends Throwable {
    public static class LibraryCheckoutNotFoundException extends RuntimeException {};
    public static class LibraryCheckoutInvalidArgumentsException extends RuntimeException {
        protected static ArrayList<Object> errorList;
        public LibraryCheckoutInvalidArgumentsException(ArrayList<Object> errorList) {
            super();
            this.errorList = errorList;
        }
    };

    @ExceptionHandler(LibraryCheckoutExceptionController.LibraryCheckoutNotFoundException.class)
    public ResponseEntity<Object> exception (LibraryCheckoutExceptionController.LibraryCheckoutNotFoundException exception) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("message", "Category not found");
        map.put("status", String.valueOf(HttpStatus.NOT_FOUND));

        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LibraryCheckoutExceptionController.LibraryCheckoutInvalidArgumentsException.class)
    public ResponseEntity<Object>
    exception(LibraryCheckoutExceptionController.LibraryCheckoutInvalidArgumentsException exception) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", "Arguments is invalid");
        map.put("status", String.valueOf(HttpStatus.BAD_REQUEST));
        map.put("errors", LibraryCheckoutExceptionController.LibraryCheckoutInvalidArgumentsException.errorList);

        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
