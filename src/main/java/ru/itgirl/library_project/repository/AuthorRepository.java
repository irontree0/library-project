package ru.itgirl.library_project.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itgirl.library_project.entity.Author;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {
    Optional<Author> findAuthorBySurname(String surname);

    @Query(nativeQuery = true, value = "SELECT * FROM author WHERE name = :name and surname = :surname")
    Optional<Author> findAuthorByNameBySql(@Param("name") String name, @Param("surname") String surname);
}