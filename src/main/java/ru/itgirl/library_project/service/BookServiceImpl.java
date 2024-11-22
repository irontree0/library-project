package ru.itgirl.library_project.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.itgirl.library_project.dto.BookCreateDto;
import ru.itgirl.library_project.dto.BookDto;
import ru.itgirl.library_project.dto.BookUpdateDto;
import ru.itgirl.library_project.entity.Book;
import ru.itgirl.library_project.repository.AuthorRepository;
import ru.itgirl.library_project.repository.BookRepository;
import ru.itgirl.library_project.repository.GenreRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;

    @Override
    public BookDto getBookById(Long id) {
        log.info("Try to find book by id {}", id);
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book: {}", bookDto);
            return bookDto;
        } else {
            log.error("Book with id {} is not found", id);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public BookDto getByNameV1(String name) {
        log.info("Try to find author by his surname {}", name);
        Optional<Book> book = bookRepository.findBookByName(name);
        if(book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book: {}", bookDto.toString());
            return bookDto;
        } else {
            log.error("Book with name {} id not found", name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public BookDto getByNameV2(String name) {
        log.info("Try to find book with name {} by SQL request", name);
        Optional<Book> book = bookRepository.findBookByNameBySql(name);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book: {}", bookDto.toString());
            return bookDto;
        } else {
            log.error("Book with name {} is not found", name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public BookDto getByNameV3(String name) {
        log.info("Try to find book by name {}", name);
        Specification<Book> specification = Specification.where(new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                return cb.equal(root.get("name"), name);
            }
        });
        Book book = bookRepository.findOne(specification).orElseThrow();
        return convertEntityToDto(book);
    }

    private BookDto convertEntityToDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .genre(book.getGenre().getName())
                .authors(book.getAuthors().stream().map(a -> a.getName() + " " + a.getSurname())
                        .collect(Collectors.joining(", ")))
                .name(book.getName())
                .build();
    }

    @Override
    public BookDto createBook(BookCreateDto bookCreateDto) {
        log.info("Create new user {}", bookCreateDto.getName());
        Book book = bookRepository.save(convertDtoToEntity(bookCreateDto));
        log.debug("Book {} is created", book);
        return convertEntityToDto(book);
    }

//    private Book convertDtoToEntity(BookCreateDto bookCreateDto) {
//        return Book.builder()
//                .name(bookCreateDto.getName())
//                .genre(Genre.builder().id(bookCreateDto.getGenreId()).build())
//                .authors(bookCreateDto.getAuthorIds().stream()
//                        .map(id -> Author.builder().id(id).build())
//                        .collect(Collectors.toSet()))
//                .build();
//    }

    private Book convertDtoToEntity(BookCreateDto bookCreateDto) {
        return Book.builder()
                .name(bookCreateDto.getName())
                .genre(genreRepository.findById(bookCreateDto.getGenreId()).orElseThrow())
                .authors(bookCreateDto.getAuthorIds().stream()
                        .map(id -> authorRepository.findById(id).orElseThrow())
                        .collect(Collectors.toSet()))
                .build();
    }

    @Override
    public BookDto updateBook(BookUpdateDto bookUpdateDto) {
        log.info("Create new user {}", bookUpdateDto.getName());
        Book book = bookRepository.findById(bookUpdateDto.getId()).orElseThrow();
        book.setName(book.getName());
        book.setGenre(genreRepository.findById(bookUpdateDto.getGenreId()).orElseThrow());
        book.setAuthors(bookUpdateDto.getAuthorIds().stream()
                .map(id -> authorRepository.findById(id).orElseThrow())
                .collect(Collectors.toSet()));
        log.debug("Book {} is update", book);
        return convertEntityToDto(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        log.info("Delete book by id {}", id);
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> getAllBooks() {
        log.info("Get all books");
        List<Book> books = bookRepository.findAll();
        log.info("Found {} books", books.size());
        return books.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }
}