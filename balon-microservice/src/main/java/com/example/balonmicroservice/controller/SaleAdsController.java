package com.example.balonmicroservice.controller;

import com.example.balonmicroservice.model.SaleAds;
import com.example.balonmicroservice.repository.SaleAdsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saleads")
public class SaleAdsController {


    @Autowired
    SaleAdsRepository saleAdsRepository;


    /**
     * @return all the Sale Announcement
     */
    @GetMapping(value = "/getAll")
    public List<SaleAds> getAllAnnouncements() {
        System.out.println("Get all sale announcements");
        return saleAdsRepository.findAll();
    }


    /**
     * @return all the Sale Announcement ACTIVE
     */
    @GetMapping(value = "/getActive")
    public List<SaleAds> getAllActive() {
        System.out.println("Get all active sale announcements");
        return saleAdsRepository.findByActive(true);
    }


    /**
     * @param email --> EMAIL of the owner user
     * @return all the Sale Announcement published by the user with email = EMAIL
     */
    @GetMapping(value = "getAllOfUser/{email}")
    public List<SaleAds> getAllAdsOfUser(@PathVariable("email") String email) {
        System.out.println("Get all SaleAds of a User");
        return saleAdsRepository.findByOwnerEmail(email);
    }


    /**
     * @param id --> ID of the Sale Announcement
     * @return the Sale Announcement with id = ID with status changed to !status
     */
    @PostMapping(value = "/changeActiveStatus/{id}")
    public SaleAds changeStatus(@PathVariable("id") Long id) {
        SaleAds result;
        if (saleAdsRepository.findById(id).isPresent()) {
            result = saleAdsRepository.findById(id).get();
            result.setActive(!result.isActive());
            return saleAdsRepository.save(result);
        }
        return null;
    }


    /**
     * @param param --> new Sale Announcement to create
     * @return a new Sale Announcement created and saved
     */
    @PostMapping(value = "/createAds")
    public SaleAds createSaleAds(@RequestBody SaleAds param) {
        System.out.println("Create new sale announcements..." + param.toString());
        return saleAdsRepository.save(
                new SaleAds(param.getOwneremail(), param.getDescription(), param.getPrice()));
    }


    /**
     * @param id --> ID of the Sale Announcement to delete
     * @return TRUE if the Sale Announcement is deleted, FALSE otherwise
     */
    @DeleteMapping(value = "/deleteAds/{id}")
    public boolean deleteSaleAds(@PathVariable("id") Long id) {
        System.out.println("Elimina l'annuncio: " + id);
        saleAdsRepository.deleteById(id);
        return !saleAdsRepository.findById(id).isPresent();
    }
}