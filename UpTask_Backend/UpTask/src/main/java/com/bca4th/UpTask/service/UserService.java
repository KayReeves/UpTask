package com.bca4th.UpTask.service;

import com.bca4th.UpTask.dto.UserDto;
import com.bca4th.UpTask.model.Role;
import com.bca4th.UpTask.model.User;
import com.bca4th.UpTask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto createUser(UserDto UserDto) {
        if (userRepository.existsByEmail(UserDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(UserDto.getEmail());
        user.setPassword(passwordEncoder.encode(UserDto.getPassword()));
        user.setName(UserDto.getName());
        user.setRole(UserDto.getRole());
        user.setActive(true);

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDto> getUsersByRole(Role role) {
        return userRepository.findByRole(role).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDto> getActiveStaff() {
        return userRepository.findByRoleAndActiveTrue(Role.STAFF).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto UserDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(UserDto.getName());
        user.setRole(UserDto.getRole());

        if (UserDto.getActive() != null) {
            user.setActive(UserDto.getActive());
        }

        if (UserDto.getPassword() != null && !UserDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(UserDto.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    private UserDto convertToDTO(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setRole(user.getRole());
        dto.setActive(user.getActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}
