package org.taasproject.saleannuncmicroservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.taasproject.saleannuncmicroservice.model.SaleAnnouncement;
import org.taasproject.saleannuncmicroservice.repository.SaleAdsRepository;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/saleads")
public class SaleAdsController {
    @Autowired
    SaleAdsRepository saleAdsRepository;

    @GetMapping("/getAll")
    public List<SaleAnnouncement> getAllAnnouncements() {
        System.out.println("Get all sale announcements");
        return saleAdsRepository.findAll();
    }

    @GetMapping("/getActive")
    public List<SaleAnnouncement> getAllActive() {
        System.out.println("Get all active sale announcements");
        return saleAdsRepository.findByActive(true);
    }

    @PostMapping("/changeActiveStatus")
    public SaleAnnouncement changeStatus(@RequestBody SaleAnnouncement param) {
        SaleAnnouncement result = param;
        if (saleAdsRepository.findById(param.getId()).isPresent()) {
            result = saleAdsRepository.findById(param.getId()).get();
            result.setActive(!param.isActive());
        }
        return result;
    }

    @PostMapping("/createAds")
    public SaleAnnouncement createSaleAds(@RequestBody SaleAnnouncement param) {
        System.out.println("Create new sale annuncements..." + param.toString());
        return saleAdsRepository.save(
                new SaleAnnouncement(param.getOwneremail(), param.getDescription(), param.getPrice()));
    }
}