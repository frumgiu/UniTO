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

    @GetMapping(value = "/getAll")
    public List<Content> getAllContents() {
        System.out.println("Get all contents");
        return contentRepository.findAll();
    }

    @GetMapping(value = "/getAllByBoard")
    public List<Content> getAllContentsByBoard(@RequestParam("board_id") Long board_id) {
        System.out.println("Get all contents from a specific Board");
        if (!boardRepository.findById(board_id).isPresent())
            return null;
        Board board = boardRepository.findById(board_id).get();
        return board.getBoardContentsList();
    }

    @GetMapping(value = "/getAllFromUser/{email}")
    public List<Content> getAllContents(@PathVariable("email") String email) {
        System.out.println("Get all contents from a specific user");
        return contentRepository.findByOwneremail(email);
    }

    /* TODO: testare funzionamento con postman */
    @GetMapping(value = "/getFromName")
    public List<Content> getContentsByName(@RequestParam("title") String title) {
        System.out.println("Get all contents with a specific title");
        return contentRepository.findByTitle(title);
    }

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

    @DeleteMapping(value = "/deleteContent/{id}")
    public String deleteContent(@PathVariable("id") Long id) {
        System.out.println("Elimino contenuto: " + id);
        contentRepository.deleteById(id);
        return "done";
    }


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