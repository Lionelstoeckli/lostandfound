package ch.stoeckli.lionel.lostandfound.report;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ch.stoeckli.lionel.lostandfound.claim.Claim;
import ch.stoeckli.lionel.lostandfound.item.Item;
import ch.stoeckli.lionel.lostandfound.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Report {

    @Id
    @GeneratedValue
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private ReportType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private ReportStatus status;

    @Column(nullable = false)
    @Size(max = 255)
    @NotEmpty
    private String location;

    @Column(nullable = false)
    @NotNull
    private LocalDate reportedAt;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties({"reports"})
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties({"reports", "claims"})
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "report")
    private Set<Claim> claims;

    public Report() {}

    public Report(ReportType type, ReportStatus status, String location, LocalDate reportedAt, Item item, User user) {
        this.type = type;
        this.status = status;
        this.location = location;
        this.reportedAt = reportedAt;
        this.item = item;
        this.user = user;
    }
}
