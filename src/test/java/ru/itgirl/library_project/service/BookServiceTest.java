package ru.itgirl.library_project.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.itgirl.library_project.dto.BookDto;
import ru.itgirl.library_project.dto.BookUpdateDto;
import ru.itgirl.library_project.dto.BookCreateDto;
import ru.itgirl.library_project.entity.Author;
import ru.itgirl.library_project.entity.Book;
import ru.itgirl.library_project.entity.Genre;
import ru.itgirl.library_project.repository.AuthorRepository;
import ru.itgirl.library_project.repository.BookRepository;
import ru.itgirl.library_project.repository.GenreRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    BookServiceImpl bookService;

    @Test
    public void testGetBookById() {
        Long bookId = 1L;

        Genre genre = new Genre();
        genre.setName("Fiction");

        Author author = Author.builder()
                .id(2L)
                .name("John")
                .surname("Doe")
                .build();

        Book book = Book.builder()
                .id(bookId)
                .name("New Book")
                .genre(genre)
                .authors(Set.of(author))
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        BookDto result = bookService.getBookById(bookId);

        verify(bookRepository, times(1)).findById(bookId);

        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertEquals("New Book", result.getName());
        assertEquals("Fiction", result.getGenre());

        assertNotNull(result.getAuthors());
        assertEquals("John Doe", result.getAuthors());
    }

    @Test
    public void testGetBookByIdFailed() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            bookService.getBookById(bookId);
        });

        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    public void testGetByNameV1() {
        String bookName = "New Book";
        Book book = new Book();
        book.setId(1L);
        book.setName(bookName);
        book.setGenre(new Genre(1L,"Fiction"));
        book.setAuthors(Set.of(new Author(2L, "John", "Doe", null)));

        when(bookRepository.findBookByName(bookName)).thenReturn(Optional.of(book));

        BookDto result = bookService.getByNameV1(bookName);

        assertNotNull(result);
        assertEquals(bookName, result.getName());
        assertEquals("Fiction", result.getGenre());
        assertEquals("John Doe", result.getAuthors());
        verify(bookRepository, times(1)).findBookByName(bookName);
    }

    @Test
    public void testGetByNameV1Failed() {
        String bookName = "Nonexistent Book";
        when(bookRepository.findBookByName(bookName)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            bookService.getByNameV1(bookName);
        });

        assertEquals("No value present", exception.getMessage());
        verify(bookRepository, times(1)).findBookByName(bookName);
    }

    @Test
    public void testGetByNameV2() {
        String bookName = "New Book";
        Book book = new Book();
        book.setId(1L);
        book.setName(bookName);
        book.setGenre(new Genre(1L, "Fiction"));
        book.setAuthors(Set.of(new Author(2L, "John", "Doe", null)));

        when(bookRepository.findBookByNameBySql(bookName)).thenReturn(Optional.of(book));

        BookDto result = bookService.getByNameV2(bookName);

        assertNotNull(result);
        assertEquals(bookName, result.getName());
        assertEquals("Fiction", result.getGenre());
        assertEquals("John Doe", result.getAuthors());
        verify(bookRepository, times(1)).findBookByNameBySql(bookName);
    }

    @Test
    public void testGetByNameV2Failed() {
        String bookName = "Nonexistent Book";
        when(bookRepository.findBookByNameBySql(bookName)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            bookService.getByNameV2(bookName);
        });

        assertEquals("No value present", exception.getMessage());
        verify(bookRepository, times(1)).findBookByNameBySql(bookName);
    }

    @Test
    public void testCreateBook() {
        BookCreateDto bookCreateDto = new BookCreateDto("New Book", 1L, Set.of(2L));
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Fantasy");

        Author author = new Author();
        author.setName("John");
        author.setSurname("Doe");

        when(genreRepository.findById(bookCreateDto.getGenreId())).thenReturn(Optional.of(genre));
        when(authorRepository.findById(2L)).thenReturn(Optional.of(author));

        Book book = new Book();
        book.setName(bookCreateDto.getName());
        book.setGenre(genre);
        book.setAuthors(Set.of(author));

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDto result = bookService.createBook(bookCreateDto);

        assertNotNull(result);
        assertEquals("New Book", result.getName());
        assertEquals("Fantasy", result.getGenre());
        assertEquals("John Doe", result.getAuthors());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testUpdateBook() {
        Long bookId = 1L;
        BookUpdateDto bookUpdateDto = new BookUpdateDto(bookId, "Updated Book", 1L, Set.of(2L));
        Genre genre = new Genre(1L, "Fiction");
        Author author = new Author(2L, "John", "Doe", Set.of());
        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setName("Old Book");
        existingBook.setGenre(genre);
        existingBook.setAuthors(Set.of(author));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(genreRepository.findById(bookUpdateDto.getGenreId())).thenReturn(Optional.of(genre));
        when(authorRepository.findById(2L)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        existingBook.setName("Updated Book");

        BookDto result = bookService.updateBook(bookUpdateDto);

        assertNotNull(result);
        assertEquals("Updated Book", result.getName());
        assertEquals("Fiction", result.getGenre());
        assertEquals("John Doe", result.getAuthors());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testUpdateBookFailed() {
        BookUpdateDto bookUpdateDto = new BookUpdateDto(1L, "Updated Book", 1L, Set.of(2L));
        when(bookRepository.findById(bookUpdateDto.getId())).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            bookService.updateBook(bookUpdateDto);
        });

        assertEquals("No value present", exception.getMessage());
        verify(bookRepository, times(1)).findById(bookUpdateDto.getId());
    }

    @Test
    public void testDeleteBook() {
        Long bookId = 1L;
        doNothing().when(bookRepository).deleteById(bookId);
        bookService.deleteBook(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    public void testDeleteBookFailed() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        bookService.deleteBook(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    public void testGetAllBooks() {
        Book book1 = new Book(1L, "Book 1", new Genre(1L,"Fiction"), Set.of(new Author(2L, "John", "Doe", Set.of())));
        Book book2 = new Book(2L, "Book 2", new Genre(2L,"Non-Fiction"), Set.of(new Author(3L, "Jane", "Smith", Set.of())));

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        List<BookDto> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Book 1", result.get(0).getName());
        assertEquals("Fiction", result.get(0).getGenre());
        assertEquals("John Doe", result.get(0).getAuthors());

        assertEquals("Book 2", result.get(1).getName());
        assertEquals("Non-Fiction", result.get(1).getGenre());
        assertEquals("Jane Smith", result.get(1).getAuthors());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllBooksFailed() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        List<BookDto> result = bookService.getAllBooks();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookRepository, times(1)).findAll();
    }
}
