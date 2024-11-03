package ru.itgirl.library_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.itgirl.library_project.entity.User;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

//    @Query(nativeQuery = true, value = "SELECT * FROM library_user WHERE id = :id")
//    Optional<LibraryUser> findUserById(@Param("id") Long id);
}