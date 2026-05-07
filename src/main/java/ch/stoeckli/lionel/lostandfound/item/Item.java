package ch.stoeckli.lionel.lostandfound.item;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.stoeckli.lionel.lostandfound.report.Report;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Item {

    @Id
    @GeneratedValue
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    Long id;

    @Column(nullable = false)
    @Size(max = 255)
    @NotEmpty
    private String name;

    @Column(length = 1000)
    @Size(max = 1000)
    private String description;

    @Column
    @Size(max = 50)
    private String color;

    @JsonIgnore
    @OneToMany(mappedBy = "item")
    private Set<Report> reports;

    public Item() {}

    public Item(String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
    }
}
