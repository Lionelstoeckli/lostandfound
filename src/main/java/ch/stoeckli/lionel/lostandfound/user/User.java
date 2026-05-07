package ch.stoeckli.lionel.lostandfound.user;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.stoeckli.lionel.lostandfound.claim.Claim;
import ch.stoeckli.lionel.lostandfound.report.Report;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "platform_user")
public class User {

    @Id
    @GeneratedValue
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    Long id;

    @Column(nullable = false, unique = true)
    @Size(max = 255)
    @NotEmpty
    private String keycloakSub;

    @Column(nullable = false)
    @Size(max = 100)
    @NotEmpty
    private String username;

    @Column(nullable = false)
    @Email
    @Size(max = 255)
    @NotEmpty
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Report> reports;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Claim> claims;

    public User() {}

    public User(String keycloakSub, String username, String email) {
        this.keycloakSub = keycloakSub;
        this.username = username;
        this.email = email;
    }
}
