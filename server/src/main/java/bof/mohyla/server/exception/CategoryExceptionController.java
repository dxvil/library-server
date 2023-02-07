package bof.mohyla.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CategoryExceptionController extends Throwable{
    public static class CategoryNotFoundException extends RuntimeException {};
    public static class CategoryInvalidArgumentsException extends RuntimeException {
        protected static ArrayList<Object> errorList;
        public CategoryInvalidArgumentsException(ArrayList<Object> errorList) {
            super();
            this.errorList = errorList;
        }
    }
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Object> exception (CategoryNotFoundException exception) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("message", "Category not found");
        map.put("status", String.valueOf(HttpStatus.NOT_FOUND));

        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryExceptionController.CategoryInvalidArgumentsException.class)
    public ResponseEntity<Object>
    exception(CategoryExceptionController.CategoryInvalidArgumentsException exception) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", "Arguments is invalid");
        map.put("status", String.valueOf(HttpStatus.BAD_REQUEST));
        map.put("errors", CategoryExceptionController.CategoryInvalidArgumentsException.errorList);

        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
