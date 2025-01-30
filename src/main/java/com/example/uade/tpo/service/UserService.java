package com.example.uade.tpo.service;

import com.example.uade.tpo.Utils.Mapper;
import com.example.uade.tpo.dtos.request.ChangeRoleRequestDto;
import com.example.uade.tpo.dtos.response.UserResponseDto;
import com.example.uade.tpo.entity.Role;
import com.example.uade.tpo.entity.User;
import com.example.uade.tpo.repository.IUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Value("${application.security.jwt.secretKey}")
    private String jwtSecret;

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public List<UserResponseDto> getUsers() {

        return userRepository.findAll().stream().map(Mapper::convertToUserResponseDto).collect(Collectors.toList());
    }

    public UserResponseDto getCurrentUserFromToken(String token) {
        String email = getEmailFromToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
        return Mapper.convertToUserResponseDto(user);
    }

    public User getCurrentUserFromTokenUser(String token) {
        String email = getEmailFromToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
        return(user);
    }

    public Boolean validateRole(String token) {
        String email = getEmailFromToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
        Role rol = user.getRole();
        if(rol.equals(Role.ROLE_ADMIN)){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }


    private String getEmailFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public boolean changeRole(Long userId, ChangeRoleRequestDto request) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            String newRole = request.getRole();
            if (newRole.equals("ROLE_ADMIN") || newRole.equals("ROLE_USER")) {
                user.get().setRole(Role.valueOf(newRole));
                userRepository.save(user.get());
                return true;
            }
        }
        return false;
    }

    public void contact(String subject, String message, String fullName) {
        notificationService.sendProblemMail(subject, message, fullName);
    }

}
