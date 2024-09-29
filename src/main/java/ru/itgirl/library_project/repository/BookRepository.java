package ru.itgirl.library_project.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itgirl.library_project.entity.Book;
import ru.itgirl.library_project.entity.Genre;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b where b.genre = ?1")
    List<Book> findByBookIdGenre(Genre genre);

}