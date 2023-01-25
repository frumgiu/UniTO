package org.spikeTassProject.usermicroservice.repository;

import org.spikeTassProject.usermicroservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findAll();
    List<User> findUserByNameOrSurname(String name, String surname);
    List<User> findUserByNameAndSurname(String name, String surname);
    User findUserByEmail(String email);
    User getUserById(Long id);
    Optional<User> findById(Long id);
}