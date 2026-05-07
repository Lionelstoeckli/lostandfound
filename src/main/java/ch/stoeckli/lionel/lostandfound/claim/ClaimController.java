package ch.stoeckli.lionel.lostandfound.claim;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ch.stoeckli.lionel.lostandfound.security.Roles;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@Validated
@Tag(name = "Claim", description = "Ansprüche auf gemeldete Gegenstände")
public class ClaimController {

    private final ClaimService claimService;

    ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @GetMapping("api/claim")
    @RolesAllowed({Roles.User, Roles.Admin})
    @Operation(summary = "Liste aller Ansprüche abrufen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste erfolgreich geladen"),
            @ApiResponse(responseCode = "401", description = "Nicht authentifiziert"),
            @ApiResponse(responseCode = "403", description = "Keine Berechtigung")
    })
    public ResponseEntity<List<Claim>> all() {
        return new ResponseEntity<>(claimService.getClaims(), HttpStatus.OK);
    }

    @GetMapping("api/claim/{id}")
    @RolesAllowed({Roles.User, Roles.Admin})
    @Operation(summary = "Anspruch per ID abrufen")
    public ResponseEntity<Claim> one(
            @Parameter(description = "ID des Anspruchs") @PathVariable @NonNull Long id) {
        return new ResponseEntity<>(claimService.getClaim(id), HttpStatus.OK);
    }

    @GetMapping("api/claim/status/{status}")
    @RolesAllowed({Roles.User, Roles.Admin})
    @Operation(summary = "Ansprüche nach Status filtern (PENDING, APPROVED, REJECTED)")
    public ResponseEntity<List<Claim>> byStatus(
            @Parameter(description = "Status des Anspruchs") @PathVariable @NonNull ClaimStatus status) {
        return new ResponseEntity<>(claimService.getClaimsByStatus(status), HttpStatus.OK);
    }

    @PostMapping("api/claim")
    @RolesAllowed({Roles.User, Roles.Admin})
    @Operation(summary = "Neuen Anspruch stellen")
    public ResponseEntity<Claim> newClaim(@Valid @RequestBody @NonNull Claim claim) {
        return new ResponseEntity<>(claimService.insertClaim(claim), HttpStatus.OK);
    }

    @PutMapping("api/claim/{id}")
    @RolesAllowed(Roles.Admin)
    @Operation(summary = "Anspruch aktualisieren (nur Admin)")
    public ResponseEntity<Claim> updateClaim(
            @Valid @RequestBody @NonNull Claim claim,
            @Parameter(description = "ID des Anspruchs") @PathVariable @NonNull Long id) {
        return new ResponseEntity<>(claimService.updateClaim(claim, id), HttpStatus.OK);
    }

    @DeleteMapping("api/claim/{id}")
    @RolesAllowed(Roles.Admin)
    @Operation(summary = "Anspruch löschen (nur Admin)")
    public void deleteClaim(
            @Parameter(description = "ID des Anspruchs") @PathVariable @NonNull Long id) {
        claimService.deleteClaim(id);
    }
}
