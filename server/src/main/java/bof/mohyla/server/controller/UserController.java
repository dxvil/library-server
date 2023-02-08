package bof.mohyla.server.controller;

import bof.mohyla.server.config.JWTService;
import bof.mohyla.server.model.User;
import bof.mohyla.server.exception.UserExceptionController;
import bof.mohyla.server.repository.UserRepository;
import com.auth0.jwt.interfaces.Claim;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import bof.mohyla.server.model.Role;

import java.util.*;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public  ResponseEntity<List<User>> getListOfUsers() {
        return  ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/users/{id}")
    public  ResponseEntity<User> getSingleUser(@PathVariable UUID id) {
        Optional<User> searchResult =userRepository.findById(id);

        if(searchResult.isEmpty()) {
            throw new UserExceptionController.UserNotFoundException();
        }

        return  ResponseEntity.ok(searchResult.get());
    }

    @PostMapping("/")
    public String createNewUser(@RequestBody User newUser) {
        boolean isEmptyName = newUser.getName() == null || newUser.getName().isEmpty();
        boolean isEmptyRole = newUser.getRole() == null ||
                (newUser.getRole() != null &&
                        newUser.getRole().getValue() != null &&
                        newUser.getRole().getValue().isEmpty()
                );
        boolean isEmptyEmail = newUser.getEmail() == null || newUser.getEmail().isEmpty();
        boolean isEmptyPassword = newUser.getPassword() == null || newUser.getPassword().isEmpty();

        if(isEmptyName || isEmptyRole || isEmptyEmail || isEmptyPassword) {
            ArrayList<Object> errorList = new ArrayList<>();
            HashMap<String, String> error = new HashMap<>();

            if(isEmptyName) {
                error.put("name", "is required");
            }

            if(isEmptyEmail) {
                error.put("email", "is required");
            }

            if(isEmptyPassword) {
                error.put("password", "is required");
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

        User user = new User(
                newUser.getName(),
                newUser.getRole(),
                newUser.getEmail(),
                passwordEncoder.encode(newUser.getPassword())
        );

        var jwtToken = jwtService.generateToken(user);

        userRepository.save(user);

        return jwtToken;
    }

    @PostMapping("/login")
    public String login(@RequestHeader String email, @RequestHeader String password) {
        Optional<User> searchUser = userRepository.findByEmail(email);

        if(searchUser.isEmpty()){
            throw new UserExceptionController.UserNotFoundException();
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );
        User user = searchUser.get();

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().value);

        String token = jwtService.generateToken(claims, user);

        return token;
    }

    @PutMapping("/users/{id}")
    public  ResponseEntity<User> editUser(@PathVariable UUID id, @RequestBody User updatedUser) {
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
        return  ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable UUID id, HttpServletRequest request) {
        //don't need an extra validation, because we are already sign-in
        final String authHeader = request.getHeader("Authorization");
        final String jwt = authHeader.substring(7);
        final Claims claims = jwtService.extractAllClaims(jwt);
//
//        if(Objects.equals(claims., Role.ADMIN.value)){
//            userRepository.deleteById(id);
//        }

    }
}
