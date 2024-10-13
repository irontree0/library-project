package ru.itgirl.library_project.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itgirl.library_project.dto.AuthorDto;
import ru.itgirl.library_project.service.AuthorService;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/author/{id}")
    AuthorDto getAuthorById(@PathVariable("id") Long id) {
        return authorService.getAuthorById(id);
    }

    @GetMapping("/author")
    AuthorDto getAuthorByName(@RequestParam("surname") String surname) {
        return authorService.getAuthorByNameV1(surname);
    }

    @GetMapping("/author/v2")
    AuthorDto getAuthorByNameV2(String name, String surname) {
        return authorService.getAuthorByNameV2(name, surname);
    }

    @GetMapping("/author/v3")
    AuthorDto getAuthorByNameV3(String name, String surname) {
        return authorService.getAuthorByNameV3(name, surname);
    }
}