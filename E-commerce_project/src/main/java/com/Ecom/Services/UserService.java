package com.Ecom.Services;

import com.Ecom.Dtos.UserDto;

import java.util.List;

public interface UserService {


        UserDto createUser(UserDto userDto);

        UserDto updateUser(Integer userId, UserDto userDto);


        void deleteUser(Integer userId);

          List<UserDto> getAllUsers();

          UserDto getUser(Integer userId);
    }

