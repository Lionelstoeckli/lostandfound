package ch.stoeckli.lionel.lostandfound.claim;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ch.stoeckli.lionel.lostandfound.report.Report;
import ch.stoeckli.lionel.lostandfound.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Claim {

    @Id
    @GeneratedValue
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    Long id;

    @Column(nullable = false, length = 1000)
    @Size(max = 1000)
    @NotEmpty
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private ClaimStatus status;

    @Column(nullable = false)
    @NotNull
    private LocalDate createdAt;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties({"claims"})
    @JoinColumn(name = "report_id")
    private Report report;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties({"claims", "reports"})
    @JoinColumn(name = "user_id")
    private User user;

    public Claim() {}

    public Claim(String message, ClaimStatus status, LocalDate createdAt, Report report, User user) {
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
        this.report = report;
        this.user = user;
    }
}
