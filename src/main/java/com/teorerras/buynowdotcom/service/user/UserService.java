package com.teorerras.buynowdotcom.service.user;

import com.teorerras.buynowdotcom.dtos.UserDto;
import com.teorerras.buynowdotcom.model.Role;
import com.teorerras.buynowdotcom.model.User;
import com.teorerras.buynowdotcom.repository.AddressRepository;
import com.teorerras.buynowdotcom.repository.RoleRepository;
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
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final RoleRepository roleRepository;

    @Override
    public User createUser(CreateUserRequest request) {
        Role userRole = Optional.ofNullable(roleRepository.findByName("ROLE_USER"))
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        return Optional.of(request)
                .filter(createUserRequest -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User newUser = new User();
                    newUser.setFirstName(request.getFirstName());
                    newUser.setLastName(request.getLastName());
                    newUser.setEmail(request.getEmail());
                    newUser.setPassword(passwordEncoder.encode(request.getPassword()));
                    newUser.setRoles(Set.of(userRole));
                    User savedUser = userRepository.save(newUser);

                    Optional.ofNullable(req.getAddressList()).ifPresent(addressList -> {
                        addressList.forEach(address -> {
                            address.setUser(savedUser);
                            addressRepository.save(address);
                        });
                    });

                    return savedUser;

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
