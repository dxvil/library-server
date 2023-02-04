package bof.mohyla.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class BookExceptionController extends Throwable {
    public static class BookNotFoundException extends RuntimeException {};
    public static class BookInvalidArgumentsException extends RuntimeException {
        protected static ArrayList<Object> errorList;
        protected static String message;
        public BookInvalidArgumentsException(
                ArrayList<Object> errorList,
                String message) {
            super();
            this.errorList = errorList;
            this.message = message;
        }
    }
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Object> exception(BookNotFoundException exception){
        Map<String, String> map = new HashMap<String, String>();
        map.put("message", "Book not found");
        map.put("status", String.valueOf(HttpStatus.NOT_FOUND));

        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookInvalidArgumentsException.class)
    public ResponseEntity<Object>
    exception(BookExceptionController.BookInvalidArgumentsException exception) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", BookInvalidArgumentsException.message);
        map.put("status", String.valueOf(HttpStatus.BAD_REQUEST));
        map.put("errors", BookExceptionController.BookInvalidArgumentsException.errorList);

        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
