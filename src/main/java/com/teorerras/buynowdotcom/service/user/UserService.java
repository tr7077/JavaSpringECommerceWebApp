package com.teorerras.buynowdotcom.service.user;

import com.teorerras.buynowdotcom.dtos.UserDto;
import com.teorerras.buynowdotcom.model.User;
import com.teorerras.buynowdotcom.repository.UserRepository;
import com.teorerras.buynowdotcom.request.CreateUserRequest;
import com.teorerras.buynowdotcom.request.UpdateUserRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(createUserRequest -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User newUser = new User();
                    newUser.setFirstName(request.getFirstName());
                    newUser.setLastName(request.getLastName());
                    newUser.setEmail(request.getEmail());
                    newUser.setPassword(passwordEncoder.encode(request.getPassword()));
                    return userRepository.save(newUser);
                }).orElseThrow(() -> new EntityExistsException("A user with email: " + request.getEmail() + " already exists"));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository :: delete, () -> {
            throw new EntityNotFoundException("User not found");
        });
    }

    @Override
    public List<UserDto> getConvertedUsers(List<User> users) {
        return users.stream().map(this::convertToDto).toList();
    }

    @Override
    public UserDto convertToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return Optional.ofNullable(userRepository.findByEmail(email)).orElseThrow(
                () -> new EntityNotFoundException("Log in required")
        );
    }
}
