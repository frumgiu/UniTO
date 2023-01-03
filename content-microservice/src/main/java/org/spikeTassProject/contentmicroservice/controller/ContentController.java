package org.spikeTassProject.contentmicroservice.controller;

import org.spikeTassProject.contentmicroservice.model.Content;
import org.spikeTassProject.contentmicroservice.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/contents")
public class ContentController {
    @Autowired
    private ContentRepository contentRepository;

    @GetMapping("/getAll")
    public List<Content> getAllContents() {
        System.out.println("Get all Contents");
        return contentRepository.findAll();
    }

    @PostMapping(value = "/createContent")
    public Content postContent(@RequestBody Content param) {
        System.out.println("Create a new Content... " + param.toString());
        return contentRepository.save(
                new Content(param.getOwneremail(), param.getTitle(), param.getDescription()));
    }
}