package org.spikeTassProject.contentmicroservice.controller;

import org.spikeTassProject.contentmicroservice.repository.BoardRepository;
import org.spikeTassProject.contentmicroservice.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.models.Board;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/boards")
public class BoardController {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ContentRepository contentRepository;

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


    @PostMapping(value = "/populateDBBoards")
    public boolean populateDBUsers() {
        System.out.println("Metodo di servizio/sviluppo per popolare il DB con alcune bacheche");
        Board b1 = new Board("Bacheca Generale", "Bacheca Generale UNITI");
        Board b2 = new Board("Bacheca Dipartimentale Informatica", "Bacheca ufficiale del Dipartimento di Informatica");
        Board b3 = new Board("Bacheca Dipartimentale Matematica", "Bacheca ufficiale del Dipartimento di Matematica");
        Board b4 = new Board("Bacheca Dipartimentale Lettere", "Bacheca ufficiale del Dipartimento di Lettere");
        boardRepository.save(b1);
        boardRepository.save(b2);
        boardRepository.save(b3);
        boardRepository.save(b4);
        return true;
    }
}