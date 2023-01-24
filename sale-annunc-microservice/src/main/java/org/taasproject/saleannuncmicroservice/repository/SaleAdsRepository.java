package org.taasproject.saleannuncmicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.taasproject.saleannuncmicroservice.model.SaleAnnouncement;
import java.util.List;
import java.util.Optional;

public interface SaleAdsRepository extends JpaRepository<SaleAnnouncement, Long> {
    List<SaleAnnouncement> findAll();
    List<SaleAnnouncement> findByActive(boolean active);
    Optional<SaleAnnouncement> findById(Long id);
    void deleteById(Long id);
}