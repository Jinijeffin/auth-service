package com.jini.auth_service.mapper;

import com.jini.auth_service.dto.UserResponse;
import com.jini.auth_service.entity.User;

public class UserMapper {

   public static UserResponse toResponse(User user){
        UserResponse userResponse= new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRoles(),
                user.isEnabled(),
                user.getCreatedAt()
        );
      return userResponse;
    }
}
