package ru.itgirl.library_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itgirl.library_project.dto.BookDto;
import ru.itgirl.library_project.dto.GenreDto;
import ru.itgirl.library_project.entity.Book;
import ru.itgirl.library_project.entity.Genre;
import ru.itgirl.library_project.repository.BookRepository;
import ru.itgirl.library_project.repository.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    @Override
    public GenreDto getGenreById(Long id) {
        Genre genre = genreRepository.findById(id).orElseThrow();
        List<Book> books = bookRepository.findByBookIdGenre(genre);
        return convertToDto(genre, books);
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
