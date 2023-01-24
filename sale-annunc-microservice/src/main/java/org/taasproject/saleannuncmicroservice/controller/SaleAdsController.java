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

    @GetMapping(value = "/getAll")
    public List<SaleAnnouncement> getAllAnnouncements() {
        System.out.println("Get all sale announcements");
        return saleAdsRepository.findAll();
    }

    @GetMapping(value = "/getActive")
    public List<SaleAnnouncement> getAllActive() {
        System.out.println("Get all active sale announcements");
        return saleAdsRepository.findByActive(true);
    }

    /* TODO: Non salva modifiche */
    @GetMapping(value = "/changeActiveStatus/{id}")
    public SaleAnnouncement changeStatus(@PathVariable("id") Long id) {
        System.out.println("Dentro cambio stato");
        SaleAnnouncement result = null;
        if (saleAdsRepository.findById(id).isPresent()) {
            System.out.println("Ho trovato l'annuncio");
            result = saleAdsRepository.findById(id).get();
            result.setActive(!result.isActive());
        }
        System.out.print("Ho finito");
        return result;
    }

    @PostMapping(value = "/createAds")
    public SaleAnnouncement createSaleAds(@RequestBody SaleAnnouncement param) {
        System.out.println("Create new sale annuncements..." + param.toString());
        return saleAdsRepository.save(
                new SaleAnnouncement(param.getOwneremail(), param.getDescription(), param.getPrice()));
    }

    @DeleteMapping(value = "/deleteAds/{id}")
    public String deleteSaleAds(@PathVariable("id") Long id) {
        System.out.println("Elimina l'annuncio: " + id);
        saleAdsRepository.deleteById(id);
        return "done";
    }
}