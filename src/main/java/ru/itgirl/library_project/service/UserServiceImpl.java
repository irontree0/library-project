package ru.itgirl.library_project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itgirl.library_project.dto.UserDto;
import ru.itgirl.library_project.entity.User;
import ru.itgirl.library_project.entity.Role;
import ru.itgirl.library_project.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public UserDto getUserById(Long id) {
        log.info("Try to find user by id {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            UserDto userDto = convertEntityToDto(user.get());
            log.info("User: {}", userDto);
            return userDto;
        } else {
            log.error("User with id {} is not found", id);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public UserDto getUserByName(String name) {
      log.info("Try to find user by name {}", name);
      Optional<User> user = userRepository.findUserByName(name);
      if (user.isPresent()) {
          UserDto userDto = convertEntityToDto(user.get());
          log.info("User: {}", userDto);
          return userDto;
      } else {
          log.error("User with name {} is not found", name);
          throw new NoSuchElementException("No value present");
      }
    }

    @Override
    public UserDto createUser(UserDto userCreateDto) {
        log.info("Create new user {}", userCreateDto.getName());
        User user = userRepository.save(convertDtoToEntity(userCreateDto));
        UserDto userDto = convertEntityToDto(user);
        log.debug("User {} is created", user);
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userUpdateDto) {
        log.info("Update user {}", userUpdateDto.getName());
        User user = userRepository.findById(userUpdateDto.getId()).orElseThrow();
        user.setName(userUpdateDto.getName());
        user.setPassword(userUpdateDto.getPassword());
        user.setRole(Role.valueOf(userUpdateDto.getRole()));
        User savedUser = userRepository.save(user);
        UserDto userDto = convertEntityToDto(savedUser);
        log.debug("User {} is updated", user);
        return userDto;
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Delete user by id {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Get all users");
        List<User> users = userRepository.findAll();
        log.info("Found {} users", users.size());
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
