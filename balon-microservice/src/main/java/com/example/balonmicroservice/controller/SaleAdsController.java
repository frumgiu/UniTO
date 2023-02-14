package com.example.balonmicroservice.controller;

import com.example.balonmicroservice.model.Category;
import com.example.balonmicroservice.model.SaleAds;
import com.example.balonmicroservice.repository.CategoryRepository;
import com.example.balonmicroservice.repository.SaleAdsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saleads")
public class SaleAdsController {


    @Autowired
    SaleAdsRepository saleAdsRepository;

    @Autowired
    CategoryRepository categoryRepository;


    /**
     * @return all the Sale Announcement
     */
    @GetMapping(value = "/getAll")
    public List<SaleAds> getAllAnnouncements() {
        System.out.println("Get all sale announcements");
        return saleAdsRepository.findAll();
    }


    /**
     *
     * @return all the Catogories
     */
    @GetMapping(value = "/getAllCategories")
    public List<Category> getAllCategories() {
        System.out.println("Get all categories");
        return categoryRepository.findAll();
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
        return saleAdsRepository.findByOwneremail(email);
    }



    /**
     * @param id --> ID of the Sale Announcement
     * @param active --> ACTIVE value to set to the Sale Announcement
     * @return the Sale Announcement with id = ID with status changed to ACTIVE
     */
    @PostMapping(value = "/changeActiveStatus/{id}")
    public SaleAds changeStatus(@PathVariable("id") Long id, @RequestParam("active") boolean active) {
        SaleAds result;
        if (saleAdsRepository.findById(id).isPresent()) {
            result = saleAdsRepository.findById(id).get();
            result.setActive(active);
            return saleAdsRepository.save(result);
        }
        return null;
    }


    /**
     * @param param --> new Sale Announcement to create
     * @return a new Sale Announcement created and saved
     */
    @PostMapping(value = "/createAds")
    public SaleAds createSaleAds(@RequestBody SaleAds param, @RequestParam("category") String nCategory) {
        System.out.println("Create new sale announcements..." + param.toString());
        if(categoryRepository.findByName(nCategory).isPresent()) {
            Category category = categoryRepository.findByName(nCategory).get();
            SaleAds saleAds = new SaleAds(
                    param.getOwneremail(),
                    param.getTitle(),
                    param.getDescription(),
                    param.getPrice());
            saleAds.setCategory(category);
            return saleAdsRepository.save(saleAds);
        }
        return null;
    }


    /**
     * @param category --> new Category to create
     * @return the new Category created
     */
    @PostMapping(value = "/createCategory")
    public Category createCategory(@RequestBody Category category) {
        System.out.println("Creating new category... " + category.toString());
        if(categoryRepository.findByName(category.getName()).isPresent())
            return null;
        return categoryRepository.save(new Category(
           category.getName(), category.getDescription()
        ));
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