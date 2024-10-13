package ru.itgirl.library_project.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.itgirl.library_project.entity.Book;
import ru.itgirl.library_project.entity.Genre;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Optional<Book> findBookByName(String name);

    @Query("select b from Book b where b.genre = ?1")
    List<Book> findByBookIdGenre(Genre genre);

    @Query(nativeQuery = true, value = "SELECT * FROM BOOK WHERE name = ?")
    Optional<Book> findBookByNameBySql(String name);
}