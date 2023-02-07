package bof.mohyla.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class UserExceptionController extends Throwable{
    public static class UserNotFoundException extends RuntimeException {};
    public static class UserInvalidArgumentsException extends RuntimeException {
        protected static ArrayList<Object> errorList;
        public UserInvalidArgumentsException(ArrayList<Object> errorList) {
            super();
            this.errorList = errorList;
        }
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> exception(UserNotFoundException exception) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("message", "User not found");
        map.put("status", String.valueOf(HttpStatus.NOT_FOUND));

        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserExceptionController.UserInvalidArgumentsException.class)
    public ResponseEntity<Object>
    exception(UserExceptionController.UserInvalidArgumentsException exception) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", "Arguments is invalid");
        map.put("status", String.valueOf(HttpStatus.BAD_REQUEST));
        map.put("errors", UserExceptionController.UserInvalidArgumentsException.errorList);

        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
