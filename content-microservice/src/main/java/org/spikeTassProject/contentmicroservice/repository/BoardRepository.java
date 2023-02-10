package org.spikeTassProject.contentmicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utils.models.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAll();
    Optional<Board> findById(Long id);
    List<Board> findByName(String nameBoard);
}