package com.readly.booklist.users;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.readly.booklist.users.UserRegistrationRequest;
import com.readly.booklist.users.UserService;
import com.readly.booklist.users.User;


@RestController
//@CrossOrigin(origins = {
//  "http://localhost:4200",
//  "http://readly-book-app.s3-website.us-east-2.amazonaws.com"
//})
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRegistrationRequest userRegistrationRequest) {

        User newUser = userService.registeredUser(userRegistrationRequest.getUsername(),
               userRegistrationRequest.getEmail(), userRegistrationRequest.getPassword());

        return ResponseEntity.ok(newUser);
    }

}
