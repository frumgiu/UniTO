package org.taasproject.saleannuncmicroservice.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "saleannouncement")
public class SaleAnnouncement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "owneremail")
    private String owneremail;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "date")
    private Date datetime;

    @Column(name = "active")
    private boolean active;

    public SaleAnnouncement(String owneremail, String description, double price) {
        this.owneremail = owneremail;
        this.description = description;
        this.price = price;
        datetime = new Date();
        active = true;
    }

    public SaleAnnouncement() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwneremail() {
        return owneremail;
    }

    public void setOwneremail(String owneremail) {
        this.owneremail = owneremail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "SaleAnnouncement{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", datetime=" + datetime +
                ", active=" + active +
                '}';
    }
}