package com.example.balonmicroservice.model;

import java.util.List;

public class Balon {
    private String description;
    private List<SaleAds> listAds;

    // Pattern Singleton --> esiste una sola istanza di Balon
    private static final Balon balon = new Balon();

    private Balon() {
        description = "Descrizione del Balon";
    }

    public static Balon getBalon() {
        return balon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SaleAds> getListAds() {
        return listAds;
    }

    public void setListAds(List<SaleAds> listAds) {
        this.listAds = listAds;
    }
}