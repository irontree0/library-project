package ru.itgirl.library_project.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.itgirl.library_project.dto.UserDto;
import ru.itgirl.library_project.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("/user/{id}")
    UserDto getUserById(@PathVariable("id") Long id){
        return userService.getUserById(id);
    }

//    @GetMapping("/user")
//    UserDto getUserByName(@RequestParam("name") String name) {
//        return userService.getUserByName(name);
//    }
//
//    @GetMapping("/user/v2")
//    UserDto getUserByNameBySql(@RequestParam("name") String name) {
//        return userService.getUserByNameV2(name);
//    }

    @PostMapping("/user/create")
    UserDto createUser(@RequestBody UserDto userCreateDto) {
        return userService.createUser(userCreateDto);
    }

    @PostMapping("/user/update")
    UserDto updateUser(@RequestBody UserDto userUpdateDto) {
        return userService.updateUser(userUpdateDto);
    }

    @DeleteMapping("/user/delete/{id}")
    void updateUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }
}
