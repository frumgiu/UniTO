package utils.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "board")
    private List<Content> boardContentsList;

    public Board(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Board() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Content> getBoardContentsList() {
        return boardContentsList;
    }

    public void setBoardContentsList(List<Content> boardContentsList) {
        this.boardContentsList = boardContentsList;
    }

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}