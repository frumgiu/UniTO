package org.spikeTassProject.contentmicroservice.repository;

import org.spikeTassProject.contentmicroservice.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContentRepository extends JpaRepository<Content,Long> {
    List<Content> findAll();
    List<Content> findByTitle(String title);
    List<Content> findByOwneremail(String owneremail);
}