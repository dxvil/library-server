package bof.mohyla.server.exception;

import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AuthorExceptionController extends Throwable {
    public static class AuthorNotFoundException extends RuntimeException{}
    public static class AuthorInvalidArgumentsException extends RuntimeException{
        protected static ArrayList<Object> errorList;
        public AuthorInvalidArgumentsException(ArrayList<Object> errorList) {
            super();
            this.errorList = errorList;
        }
    }
    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<Object> exception(AuthorNotFoundException exception) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("message", "Author not found");
        map.put("status", String.valueOf(HttpStatus.NOT_FOUND));

        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorInvalidArgumentsException.class)
    public ResponseEntity<Object>
    exception(AuthorInvalidArgumentsException exception) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", "Arguments is invalid");
        map.put("status", String.valueOf(HttpStatus.BAD_REQUEST));
        map.put("errors", AuthorInvalidArgumentsException.errorList);

        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
