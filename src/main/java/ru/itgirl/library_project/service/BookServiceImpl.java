package ru.itgirl.library_project.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.itgirl.library_project.dto.BookCreateDto;
import ru.itgirl.library_project.dto.BookDto;
import ru.itgirl.library_project.dto.BookUpdateDto;
import ru.itgirl.library_project.entity.Book;
import ru.itgirl.library_project.repository.AuthorRepository;
import ru.itgirl.library_project.repository.BookRepository;
import ru.itgirl.library_project.repository.GenreRepository;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;

    @Override
    public BookDto getByNameV1(String name) {
            Book book = bookRepository.findBookByName(name).orElseThrow();
        return convertEntityToDto(book);
    }

    @Override
    public BookDto getByNameV2(String name) {
        Book book = bookRepository.findBookByNameBySql(name).orElseThrow();
        return convertEntityToDto(book);
    }

    @Override
    public BookDto getByNameV3(String name) {
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
        Book book = bookRepository.save(convertDtoToEntity(bookCreateDto));
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
        Book book = bookRepository.findById(bookUpdateDto.getId()).orElseThrow();
        book.setName(book.getName());
        book.setGenre(genreRepository.findById(bookUpdateDto.getGenreId()).orElseThrow());
        book.setAuthors(bookUpdateDto.getAuthorIds().stream()
                .map(id -> authorRepository.findById(id).orElseThrow())
                .collect(Collectors.toSet()));
        return convertEntityToDto(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}