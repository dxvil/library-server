package bof.mohyla.server.controller;

import bof.mohyla.server.bean.User;
import bof.mohyla.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.UUID;

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
            throw new RuntimeException("User with id: " + id + " is not found.");
        }

        return searchResult.get();
    }

    @PostMapping("/api/v1/users/")
    public User createNewUser(@RequestBody User newUser) {
        userRepository.save(newUser);

        return newUser;
    }

    @PutMapping("/api/v1/users/{id}")
    public User editUser(@PathVariable UUID id, @RequestBody User updatedUser) {
        Optional<User> searchResult = userRepository.findById(id);

        if(searchResult.isEmpty()) {
            throw new RuntimeException("User with id: " + id + " is not found.");
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
