package com.teorerras.buynowdotcom.service.user;

import com.teorerras.buynowdotcom.dtos.ProductDto;
import com.teorerras.buynowdotcom.dtos.UserDto;
import com.teorerras.buynowdotcom.model.Product;
import com.teorerras.buynowdotcom.model.User;
import com.teorerras.buynowdotcom.request.CreateUserRequest;
import com.teorerras.buynowdotcom.request.UpdateUserRequest;

import java.util.List;

public interface IUserService {
    User createUser(CreateUserRequest request);
    User updateUser(UpdateUserRequest request, Long userId);
    User getUserById(Long userId);
    void deleteUser(Long userId);

    List<UserDto> getConvertedUsers(List<User> users);
    UserDto convertToDto(User user);
}
