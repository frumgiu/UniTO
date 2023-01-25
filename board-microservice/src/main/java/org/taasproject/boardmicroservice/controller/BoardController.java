package org.taasproject.boardmicroservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.taasproject.boardmicroservice.repository.BoardRepository;
import utils.models.Board;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/boards")
public class BoardController {
    @Autowired
    private BoardRepository  boardRepository;

    @GetMapping("/getAll")
    public List<Board> getAllBoards() {
        System.out.println("Cerco tutte le bacheche dipartimentali");
        return boardRepository.findAll();
    }

    @GetMapping("/getBoardById/{id}")
    public Board getBoard(@PathVariable("id") Long id) {
        System.out.println("Cerco la bacheca dipartimentala con id: " + id);
        Board boardFromDB = null;
        if (boardRepository.findById(id).isPresent()) {
            System.out.println("Esiste una bacheca con questo id");
            boardFromDB = boardRepository.findById(id).get();
        }
        return boardFromDB;
    }

    @GetMapping("/getBoardsByName/{name}")
    public List<Board> getBoards(@PathVariable("name") String name) {
        System.out.println("Cerco le bacheche dipartimentali con il nome: " + name);
        return boardRepository.findByName(name);
    }

    @PostMapping("/createBoard")
    public Board createBoard(@RequestBody Board param) {
        System.out.println("Creo le bacheca dipartimentale");
        return boardRepository.save(
                new Board(param.getName(), param.getDescription()));
    }
}