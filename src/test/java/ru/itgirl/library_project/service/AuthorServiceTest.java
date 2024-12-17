package ru.itgirl.library_project.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.itgirl.library_project.dto.AuthorDto;
import ru.itgirl.library_project.dto.AuthorCreateDto;
import ru.itgirl.library_project.dto.AuthorUpdateDto;
import ru.itgirl.library_project.entity.Author;
import ru.itgirl.library_project.entity.Book;
import ru.itgirl.library_project.repository.AuthorRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthorServiceTest {

    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    AuthorServiceImpl authorService;

    @Test
    public void testGetAuthorById() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();

        Author author = new Author(id, name, surname, books);

        when(authorRepository.findById(id)).thenReturn(Optional.of(author));

        AuthorDto authorDto = authorService.getAuthorById(id);
        verify(authorRepository).findById(id);
        assertEquals(authorDto.getId(), author.getId());
        assertEquals(authorDto.getName(), author.getName());
        assertEquals(authorDto.getSurname(), author.getSurname());
    }

    @Test
    public void testGetAuthorByIdFailed() {
        Long id = 1L;

        when(authorRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getAuthorById(id));
        verify(authorRepository).findById(id);
    }

    @Test
    public void testGetAuthorByNameV1() {
        String surname = "Doe";
        Author author = new Author();
        author.setName("John");
        author.setSurname(surname);

        when(authorRepository.findAuthorBySurname(surname)).thenReturn(Optional.of(author));

        AuthorDto result = authorService.getAuthorByNameV1(surname);

        verify(authorRepository, times(1)).findAuthorBySurname(surname);
        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getSurname());
    }

    @Test
    public void testGetAuthorByNameV1Failed() {
        String surname = "Doe";

        when(authorRepository.findAuthorBySurname(surname)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getAuthorByNameV1(surname));
        verify(authorRepository).findAuthorBySurname(surname);
    }

    @Test
    public void testGetAuthorByNameV2() {
        String name = "John";
        String surname = "Doe";
        Author author = new Author();
        author.setName(name);
        author.setSurname(surname);

        when(authorRepository.findAuthorByNameBySql(name, surname)).thenReturn(Optional.of(author));

        AuthorDto result = authorService.getAuthorByNameV2(name, surname);

        verify(authorRepository, times(1)).findAuthorByNameBySql(name, surname);
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(surname, result.getSurname());
    }

    @Test
    public void testGetAuthorByNameV2Failed() {
        String name = "John";
        String surname = "Doe";

        when(authorRepository.findAuthorByNameBySql(name, surname)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getAuthorByNameV2(name, surname));
        verify(authorRepository).findAuthorByNameBySql(name, surname);
    }

    @Test
    public void testCreateAuthor() {
        AuthorCreateDto createDto = new AuthorCreateDto();
        createDto.setName("John");
        createDto.setSurname("Doe");

        Author savedAuthor = new Author();
        savedAuthor.setName("John");
        savedAuthor.setSurname("Doe");

        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);

        AuthorDto result = authorService.createAuthor(createDto);

        verify(authorRepository, times(1)).save(any(Author.class));
        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getSurname());
    }

    @Test
    public void testCreateAuthorFailed() {
        AuthorCreateDto createDto = new AuthorCreateDto();
        createDto.setName("John");
        createDto.setSurname("Doe");

        when(authorRepository.save(any(Author.class))).thenThrow(new DataIntegrityViolationException("Duplicate name"));

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> authorService.createAuthor(createDto));

        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    public void testUpdateAuthor() {
        Long authorId = 1L;
        AuthorUpdateDto updateDto = new AuthorUpdateDto();
        updateDto.setId(authorId);
        updateDto.setName("UpdatedName");
        updateDto.setSurname("UpdatedSurname");

        Author existingAuthor = new Author();
        existingAuthor.setName("OldName");
        existingAuthor.setSurname("OldSurname");

        Author updatedAuthor = new Author();
        updatedAuthor.setName("UpdatedName");
        updatedAuthor.setSurname("UpdatedSurname");

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(existingAuthor)).thenReturn(updatedAuthor);

        AuthorDto result = authorService.updateAuthor(updateDto);

        verify(authorRepository, times(1)).findById(authorId);
        verify(authorRepository, times(1)).save(existingAuthor);

        assertNotNull(result);
        assertEquals("UpdatedName", result.getName());
        assertEquals("UpdatedSurname", result.getSurname());
    }

    @Test
    public void testUpdateAuthorFailed() {
        Long authorId = 1L;
        AuthorUpdateDto updateDto = new AuthorUpdateDto();
        updateDto.setId(authorId);
        updateDto.setName("UpdatedName");
        updateDto.setSurname("UpdatedSurname");

        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.updateAuthor(updateDto));

        verify(authorRepository, times(1)).findById(authorId);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    public void testDeleteAuthor() {
        Long authorId = 1L;

        authorService.deleteAuthor(authorId);

        verify(authorRepository, times(1)).deleteById(authorId);
    }

    @Test
    public void testGetAllAuthors() {
        Author author1 = new Author();
        author1.setName("John");
        author1.setSurname("Doe");

        Author author2 = new Author();
        author2.setName("Jane");
        author2.setSurname("Smith");

        when(authorRepository.findAll()).thenReturn(List.of(author1, author2));

        List<AuthorDto> result = authorService.getAllAuthors();

        verify(authorRepository, times(1)).findAll();
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("John", result.get(0).getName());
        assertEquals("Doe", result.get(0).getSurname());
        assertEquals("Jane", result.get(1).getName());
        assertEquals("Smith", result.get(1).getSurname());
    }
}
