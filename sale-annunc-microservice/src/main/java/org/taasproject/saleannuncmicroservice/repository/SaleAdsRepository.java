package org.taasproject.saleannuncmicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.taasproject.saleannuncmicroservice.model.SaleAnnouncement;
import java.util.List;

public interface SaleAdsRepository extends JpaRepository<SaleAnnouncement, Long> {
    List<SaleAnnouncement> findAll();
    List<SaleAnnouncement> findByActive(boolean active);
}