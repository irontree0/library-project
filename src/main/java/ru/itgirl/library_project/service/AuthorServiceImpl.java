package ru.itgirl.library_project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.itgirl.library_project.dto.AuthorCreateDto;
import ru.itgirl.library_project.dto.AuthorDto;
import ru.itgirl.library_project.dto.AuthorUpdateDto;
import ru.itgirl.library_project.dto.BookDto;
import ru.itgirl.library_project.entity.Author;
import ru.itgirl.library_project.repository.AuthorRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    @Override
    public AuthorDto getAuthorById(Long id) {
        log.info("Try to find author by id {}", id);
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            AuthorDto authorDto = convertEntityToDto(author.get());
            log.info("Author: {}", authorDto);
            return authorDto;
        } else {
            log.error("Author with id {} is not found", id);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public AuthorDto getAuthorByNameV1(String surname) {
        log.info("Try to find author by his surname {}", surname);
        Optional<Author> author = authorRepository.findAuthorBySurname(surname);
        if (author.isPresent()) {
            AuthorDto authorDto = convertEntityToDto(author.get());
            log.info("Author: {}", authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with surname {} is not found", surname);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public AuthorDto getAuthorByNameV2(String name, String surname) {
        log.info("Try to find author with name {} and surname {} by SQL request", name, surname);
        Optional<Author> author = authorRepository.findAuthorByNameBySql(name, surname);
        if (author.isPresent()) {
            AuthorDto authorDto = convertEntityToDto(author.get());
            log.info("Author: {}", authorDto);
            return authorDto;
        } else {
            log.error("Author with name {} and surname {} is not found", name, surname);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public AuthorDto getAuthorByNameV3(String name, String surname) {
        Specification<Author> specification = Specification
                .where((Specification<Author>) (root, query, cb) ->
                        cb.and(cb.equal(root.get("name"), name), cb.equal(root.get("surname"), surname)));
        Author author = authorRepository.findOne(specification).orElseThrow();
        return convertToDto(author);
    }

    private AuthorDto convertToDto(Author author) {
        List<BookDto> bookDtoList = author.getBooks()
                .stream()
                .map(book -> BookDto.builder()
                        .genre(book.getGenre().getName())
                        .name(book.getName())
                        .id(book.getId())
                        .build()
                ).toList();
        return AuthorDto.builder()
                .books(bookDtoList)
                .id(author.getId())
                .name(author.getName())
                .surname(author.getSurname())
                .build();
    }

    @Override
    public AuthorDto createAuthor(AuthorCreateDto authorCreateDto) {
        log.info("Create new author {}", authorCreateDto.getName());
        Author author = authorRepository.save(convertDtoToEntity(authorCreateDto));
        AuthorDto authorDto = convertEntityToDto(author);
        log.debug("Author {} is created", author);
        return authorDto;
    }

    private Author convertDtoToEntity(AuthorCreateDto authorCreateDto) {
        return Author.builder()
                .name(authorCreateDto.getName())
                .surname(authorCreateDto.getSurname())
                .build();
    }

    private AuthorDto convertEntityToDto(Author author) {
        List<BookDto> bookDtoList = null;
        if (author.getBooks() != null) {
            bookDtoList = author.getBooks()
                    .stream()
                    .map(book -> BookDto.builder()
                            .genre(book.getGenre().getName())
                            .name(book.getName())
                            .id(book.getId())
                            .build())
                    .toList();
        }

        AuthorDto authorDto = AuthorDto.builder()
                .id(author.getId())
                .name(author.getName())
                .surname(author.getSurname())
                .books(bookDtoList)
                .build();
        return authorDto;
    }

    @Override
    public AuthorDto updateAuthor(AuthorUpdateDto authorUpdateDto) {
        log.info("Update author {}", authorUpdateDto.getName());
        Author author = authorRepository.findById(authorUpdateDto.getId()).orElseThrow();
        author.setName(authorUpdateDto.getName());
        author.setSurname(authorUpdateDto.getSurname());
        Author savedAuthor = authorRepository.save(author);
        AuthorDto authorDto = convertEntityToDto(savedAuthor);
        log.debug("Author {} is updated", author);
        return authorDto;
    }

    @Override
    public void deleteAuthor(Long id) {
        log.info("Delete author by id {}", id);
        authorRepository.deleteById(id);
    }

    @Override
    public List<AuthorDto> getAllAuthors() {
        log.info("Get all authors");
        List<Author> authors = authorRepository.findAll();
        log.info("Found {} authors", authors.size());
        return authors.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }
}