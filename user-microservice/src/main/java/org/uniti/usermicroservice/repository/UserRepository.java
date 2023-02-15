package org.uniti.usermicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uniti.usermicroservice.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findAll();
    List<User> findUserByNameOrSurname(String name, String surname);
    List<User> findUserByNameAndSurname(String name, String surname);
//    User findUserByEmail(String email);
    Optional<User> findUserByEmail(String email);
    User getUserById(Long id);
    Optional<User> findById(Long id);
}