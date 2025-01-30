package com.example.uade.tpo.controller;

import com.example.uade.tpo.dtos.request.ChangeRoleRequestDto;
import com.example.uade.tpo.dtos.response.UserResponseDto;
import com.example.uade.tpo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getUsers(@RequestHeader("Authorization") String token) {
        Boolean validate = userService.validateRole(token);
        if(validate){
            List<UserResponseDto> users = userService.getUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }

    }

    @PutMapping("/changeRole/{userId}")
    public ResponseEntity<?> changeRole(@PathVariable Long userId, @RequestBody ChangeRoleRequestDto request) {
        try {
            if (userService.changeRole(userId, request)) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/currentUser")
    public ResponseEntity<?> getCurrentUserFromToken(@RequestHeader("Authorization") String token) {
        try {
            UserResponseDto userData = userService.getCurrentUserFromToken(token);
            if (userData != null) {
                return ResponseEntity.ok(userData);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
    }

    @PostMapping("/contact/{subject}/{message}/{fullName}")
    public ResponseEntity<?> contact(@PathVariable String subject, @PathVariable String message, @PathVariable String fullName) {
        try {
            userService.contact(subject, message, fullName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el mensaje");
        }
    }



    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        if (userService.deleteUser(userId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
