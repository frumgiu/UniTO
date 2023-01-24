package org.spikeTassProject.contentmicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utils.models.Content;
import java.util.List;

public interface ContentRepository extends JpaRepository<Content,Long> {
    List<Content> findAll();
    List<Content> findByTitle(String title);
    List<Content> findByOwneremail(String owneremail);
    void deleteById(Long id);
}