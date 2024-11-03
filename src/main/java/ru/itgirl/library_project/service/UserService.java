package ru.itgirl.library_project.service;

import ru.itgirl.library_project.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUserById(Long id);

    UserDto createUser(UserDto userCreateDto);

    UserDto updateUser(UserDto userUpdateDto);

    void deleteUser(Long id);

    List<UserDto> getAllUsers();
}
