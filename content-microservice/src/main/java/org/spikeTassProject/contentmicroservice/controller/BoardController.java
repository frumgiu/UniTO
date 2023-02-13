package org.spikeTassProject.contentmicroservice.controller;

import org.spikeTassProject.contentmicroservice.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.models.Board;
import utils.models.DTO.BoardDTO;
import utils.models.mappers.BoardDTOMapper;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {


    @Autowired
    private BoardRepository boardRepository;


    /**
     * @return a List of BoardDTO containing all the Boards
     */
    @GetMapping("/getAll")
    public List<BoardDTO> getAllBoards() {
        System.out.println("Searching all the Unito (Departmental) Boards...");
        return BoardDTOMapper.mapAllToDTO(boardRepository.findAll());
        //return boardRepository.findAll();
    }


    /**
     * @param id --> the ID of the Board
     * @return the Board (with all its data) with ID = id
     */
    @GetMapping("/getBoardById/{id}")
    public Board getBoard(@PathVariable("id") Long id) {
        System.out.println("Searching the Unito (Departmental) Board with id = " + id);
        Board boardFromDB = null;
        if (boardRepository.findById(id).isPresent()) {
            boardFromDB = boardRepository.findById(id).get();
        }
        return boardFromDB;
    }


    /**
     * @param param --> the new Board
     * @return the created object of type Board.
     */
    @PostMapping("/createBoard")
    public Board createBoard(@RequestBody Board param) {
        System.out.println("Creating a new Unito (Departmental) Boards...");
        return boardRepository.save(
                new Board(param.getName(), param.getDescription()));
    }


    /**
     *
     * JUST FOR TEST
     * (DB population)
     */
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