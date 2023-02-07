package bof.mohyla.server.controller;

import bof.mohyla.server.bean.User;
import bof.mohyla.server.exception.UserExceptionController;
import bof.mohyla.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import bof.mohyla.server.bean.Role;
import javax.swing.text.html.Option;
import java.util.*;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/api/v1/users/")
    public List<User> getListOfUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/api/v1/users/{id}")
    public User getSingleUser(@PathVariable UUID id) {
        Optional<User> searchResult =userRepository.findById(id);

        if(searchResult.isEmpty()) {
            throw new UserExceptionController.UserNotFoundException();
        }

        return searchResult.get();
    }

    @PostMapping("/api/v1/users/")
    public User createNewUser(@RequestBody User newUser) {
        boolean isEmptyName = newUser.getName() == null ||
                newUser.getName().isEmpty();
        boolean isEmptyRole = newUser.getRole() == null ||
                (newUser.getRole() != null &&
                        newUser.getRole().getValue() != null &&
                        newUser.getRole().getValue().isEmpty()
                );

        if(isEmptyName || isEmptyRole) {
            ArrayList<Object> errorList = new ArrayList<>();
            HashMap<String, String> error = new HashMap<>();

            if(isEmptyName) {
                error.put("name", "is required");
            }

            if(isEmptyRole) {
                ArrayList<Role> roles =
                        new ArrayList<Role>(Arrays.asList(Role.values()));

                String role = "must be one of " + roles.toString();

                error.put("role", role);
            }

            errorList.add(error);

            throw new UserExceptionController.UserInvalidArgumentsException(errorList);
        }

        userRepository.save(newUser);

        return newUser;
    }

    @PutMapping("/api/v1/users/{id}")
    public User editUser(@PathVariable UUID id, @RequestBody User updatedUser) {
        Optional<User> searchResult = userRepository.findById(id);

        if(searchResult.isEmpty()) {
            throw new UserExceptionController.UserNotFoundException();
        }

        boolean isEmptyName = updatedUser.getName() == null ||
                updatedUser.getName().isEmpty();
        boolean isEmptyRole = updatedUser.getRole() == null ||
                (updatedUser.getRole() != null &&
                        updatedUser.getRole().getValue() != null &&
                        updatedUser.getRole().getValue().isEmpty()
                );

        if(isEmptyName || isEmptyRole) {
            ArrayList<Object> errorList = new ArrayList<>();
            HashMap<String, String> error = new HashMap<>();

            if(isEmptyName) {
                error.put("name", "is required");
            }

            if(isEmptyRole) {
                ArrayList<Role> roles =
                        new ArrayList<Role>(Arrays.asList(Role.values()));

                String role = "must be one of " + roles.toString();

                error.put("role", role);
            }

            errorList.add(error);

            throw new UserExceptionController.UserInvalidArgumentsException(errorList);
        }

        User user = searchResult.get();
        user.setName(updatedUser.getName());
        user.setRole(updatedUser.getRole());

        userRepository.save(user);
        return user;
    }

    @DeleteMapping("/api/v1/users/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userRepository.deleteById(id);
    }
}
