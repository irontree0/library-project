package ru.itgirl.library_project.service;
import ru.itgirl.library_project.dto.AuthorDto;

public interface AuthorService {
    AuthorDto getAuthorById(Long id);

    AuthorDto getAuthorByNameV1(String surname);

    AuthorDto getAuthorByNameV2(String name, String surname);

    AuthorDto getAuthorByNameV3(String name, String surname);
}