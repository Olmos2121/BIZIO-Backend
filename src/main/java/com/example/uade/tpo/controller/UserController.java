package com.example.uade.tpo.controller;

import com.example.uade.tpo.dtos.request.UserRequestDto;
import com.example.uade.tpo.dtos.response.UserResponseDto;
import com.example.uade.tpo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/") //Get all users
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{userId}") //Get user by id
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        Optional<UserResponseDto> user = userService.getUserById(userId);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping //Create user
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userDto) {
        UserResponseDto newUser = userService.createUser(userDto);
        if (newUser == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}") //Update user
    public ResponseEntity<UserResponseDto> updateUser
            (@PathVariable Long userId, @RequestBody UserRequestDto userDetails) {
        UserResponseDto updatedUser = userService.updateUser(userId, userDetails);
        if (updatedUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}") //Delete user
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        Boolean deleted = userService.deleteUser(userId);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
