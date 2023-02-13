package org.spikeTassProject.contentmicroservice.controller;

import org.spikeTassProject.contentmicroservice.repository.BoardRepository;
import org.spikeTassProject.contentmicroservice.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.models.Board;
import utils.models.Content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/contents")
public class ContentController {


    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private BoardRepository boardRepository;


    /**
     * @return the whole List of Contents
     */
    @GetMapping(value = "/getAll")
    public List<Content> getAllContents() {
        System.out.println("Get all contents");
        return contentRepository.findAll();
    }


    /**
     * @param board_id --> ID of the Board
     * @return the List of Board contents with id = ID
     */
    @GetMapping(value = "/getAllByBoard/{board_id}")
    public List<Content> getAllContentsByBoard(@PathVariable("board_id") Long board_id) {
        System.out.println("Get all contents from a specific Board with id = " +board_id);
        if (!boardRepository.findById(board_id).isPresent())
            return null;
        Board board = boardRepository.findById(board_id).get();
        return board.getBoardContentsList();
    }


    /**
     * @param email --> EMAIL of the User
     * @return the List of Contents owned by the user with email = EMAIL
     */
    @GetMapping(value = "/getAllFromUser/{email}")
    public List<Content> getAllContents(@PathVariable("email") String email) {
        System.out.println("Get all contents from a specific user with email =" + email);
        return contentRepository.findByOwneremail(email);
    }


    /**
     * @param param the new Content
     * @param id --> the ID of the Board where the new Content will be inserted
     * @return the new Content created
     */
    @PostMapping(value = "/createContent")
    public Content postContent(@RequestBody Content param, @RequestParam("board_id") Long id) {

        if (!boardRepository.findById(id).isPresent() || param == null)
            return null;
        System.out.println("Create a new content... " + param.toString());
        Board board = boardRepository.findById(id).get();
        Content content = new Content(param.getOwneremail(), param.getTitle(), param.getDescription());
        content.setBoard(board);
        return contentRepository.save(content);
    }


    /**
     * @param id --> ID of the Content
     * @return TRUE if the content is Deleted, FALSE otherwise
     */
    @DeleteMapping(value = "/deleteContent/{id}")
    public boolean deleteContent(@PathVariable("id") Long id) {
        System.out.println("Deleting content with id = : " + id);
        contentRepository.deleteById(id);
        return !contentRepository.findById(id).isPresent();
    }


    /**
     * JUST FOR TEST
     * (DB population)
     */
    @PostMapping(value = "/populateDBContents")
    public boolean populateDBUsers() {
        System.out.println("Metodo di servizio/sviluppo per popolare il DB con alcuni contentuti dentro le bacheche");

        Board board_generale = boardRepository.findById((long) 1).get();
        Board board_informatica = boardRepository.findById((long) 2).get();
        Board board_matematica = boardRepository.findById((long) 3).get();
        ArrayList<Board> boards = new ArrayList<>(Arrays.asList(board_generale,board_informatica,board_matematica));


        String[] users = {"fabio.ferrero111@edu.unito.it","giulia.frumento@edu.unito.it","samuele.monasterolo@edu.unito.it"};

        for (int i = 0; i < 20; i++) {
            Content c = new Content(
                    users[i % users.length],"Contenuto prova n." + (i+1), "Descrizione prova n." + (i+1)
            );
            c.setBoard(boards.get(i % boards.size()));
            contentRepository.save(c);
        }

        return true;
    }
}