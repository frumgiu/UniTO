package utils.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Content")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "owneremail")
    private String owneremail;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "datetime")
    private Date dateTime;

    /**
     * Questa Java Annotation @JsonIgnore serve per il seguente motivo:
     * Nel momento in cui viene richiesto un Content, sappiamo che al suo
     * interno avremo un parametro di tipo Board. Quest'ultimo avrà a sua
     * volta una lista di Content (dove ogni Content avrà a sua volta una
     * Board).
     * Questo causa una ricorsione infinita, e causa problemi.
     * Per questo motivo si utilizza @JsonIgnore che ignora il parametro
     * nella conversione in Json
     */
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "board_id")
    private Board board;

    public Content(String owneremail, String title, String description) {
        this.owneremail = owneremail;
        this.title = title;
        this.description = description;
        this.dateTime = new Date();
    }

    public Content() {}

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public String toString() {
        return "Content{" +
                "id=" + id +
                ", owneremail='" + owneremail + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}