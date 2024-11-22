package ru.itgirl.library_project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itgirl.library_project.dto.BookDto;
import ru.itgirl.library_project.dto.GenreDto;
import ru.itgirl.library_project.entity.Book;
import ru.itgirl.library_project.entity.Genre;
import ru.itgirl.library_project.repository.BookRepository;
import ru.itgirl.library_project.repository.GenreRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    @Override
    public GenreDto getGenreById(Long id) {
        log.info("Try to find genre by id {}", id);
        Optional<Genre> genre = genreRepository.findById(id);
        if (genre.isPresent()) {
            log.info("Genre: {}", genre);
        } else {
            log.error("Genre with id {} not found", id);
            throw new NoSuchElementException("No value present");
        }
        log.info("Try to find books by genre {}", genre.get());
        List<Book> books = bookRepository.findBooksByGenre(genre.get());
        log.info("Found {} books by genre {}", books.size(), genre.get().getName());
        return convertToDto(genre.get(), books);
    }

    private GenreDto convertToDto(Genre genre, List<Book> books) {
        return GenreDto.builder()
                .name(genre.getName())
                .books(books.stream()
                        .map(book -> BookDto.builder()
                                .genre(book.getGenre().getName())
                                .name(book.getName())
                                .id(book.getId())
                                .build()
                        ).toList()
                ).build();
    }
}
