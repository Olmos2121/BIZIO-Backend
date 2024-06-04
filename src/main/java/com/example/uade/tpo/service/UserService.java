package com.example.uade.tpo.service;

import com.example.uade.tpo.Utils.Mapper;
import com.example.uade.tpo.dtos.request.UserRequestDto;
import com.example.uade.tpo.dtos.response.UserResponseDto;
import com.example.uade.tpo.entity.User;
import com.example.uade.tpo.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    public List<UserResponseDto> getUsers() {
        return userRepository.findAll().stream().map(Mapper::convertToUserResponseDto).collect(Collectors.toList());
    }

    public Optional<UserResponseDto> getUserById(Long userId) {
        return userRepository.findById(userId).map(Mapper::convertToUserResponseDto);
    }

    public UserResponseDto createUser(UserRequestDto userDto) {
        User user = new User();
        List<User> users = userRepository.findAll();
        for (User u : users) {
            if (u.getEmail().equals(userDto.getEmail())) {
                return null;
            }
        }
        user.setName(userDto.getName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return Mapper.convertToUserResponseDto(userRepository.save(user));
    }

    public UserResponseDto updateUser(Long userId, UserRequestDto userDetails) {
        Optional<User> userOptional = userRepository.findById(userId);
        List<User> users = userRepository.findAll();
        for (User u : users) {
            if (u.getEmail().equals(userDetails.getEmail())) {
                return null;
            }
        }
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setName(userDetails.getName());
            user.setLastName(userDetails.getLastName());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            return Mapper.convertToUserResponseDto(userRepository.save(user));
        }
        return null;
    }

    public Boolean deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

}
