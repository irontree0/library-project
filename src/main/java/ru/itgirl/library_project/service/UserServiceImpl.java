package ru.itgirl.library_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itgirl.library_project.dto.UserDto;
import ru.itgirl.library_project.entity.User;
import ru.itgirl.library_project.entity.Role;
import ru.itgirl.library_project.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return convertEntityToDto(user);
    }

//    @Override
//    public UserDto getUserByName(String name) {
//        LibraryUser libraryUser = userRepository.findUserByName(name).orElseThrow();
//        return convertEntityToDto(libraryUser);
//    }
//
//    @Override
//    public UserDto getUserByNameV2(String name) {
//        LibraryUser libraryUser = userRepository.findUserByNameBySql(name).orElseThrow();
//        return convertEntityToDto(libraryUser);
//    }

    @Override
    public UserDto createUser(UserDto userCreateDto) {
        User user = userRepository.save(convertDtoToEntity(userCreateDto));
        UserDto userDto = convertEntityToDto(user);
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userUpdateDto) {
        User user = userRepository.findById(userUpdateDto.getId()).orElseThrow();
        user.setName(userUpdateDto.getName());
        user.setPassword(userUpdateDto.getPassword());
        user.setRole(Role.valueOf(userUpdateDto.getRole()));
        User savedUser = userRepository.save(user);
        UserDto userDto = convertEntityToDto(savedUser);
        return userDto;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private User convertDtoToEntity(UserDto userCreateDto) {
        return User.builder()
                .role(Role.valueOf(userCreateDto.getRole()))
                .name(userCreateDto.getName())
                .password(userCreateDto.getPassword())
                .build();
    }

    private UserDto convertEntityToDto(User user) {
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .password(user.getPassword())
                .role(user.getRole().name())
                .build();
        return userDto;
    }
}
