package ch.stoeckli.lionel.lostandfound.report;

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
@Tag(name = "Report", description = "Meldungen über verlorene und gefundene Gegenstände")
public class ReportController {

    private final ReportService reportService;

    ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("api/report")
    @RolesAllowed({Roles.User, Roles.Admin})
    @Operation(summary = "Liste aller Meldungen abrufen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste erfolgreich geladen"),
            @ApiResponse(responseCode = "401", description = "Nicht authentifiziert"),
            @ApiResponse(responseCode = "403", description = "Keine Berechtigung")
    })
    public ResponseEntity<List<Report>> all() {
        return new ResponseEntity<>(reportService.getReports(), HttpStatus.OK);
    }

    @GetMapping("api/report/{id}")
    @RolesAllowed({Roles.User, Roles.Admin})
    @Operation(summary = "Meldung per ID abrufen")
    public ResponseEntity<Report> one(
            @Parameter(description = "ID der Meldung") @PathVariable @NonNull Long id) {
        return new ResponseEntity<>(reportService.getReport(id), HttpStatus.OK);
    }

    @GetMapping("api/report/type/{type}")
    @RolesAllowed({Roles.User, Roles.Admin})
    @Operation(summary = "Meldungen nach Typ filtern (LOST oder FOUND)")
    public ResponseEntity<List<Report>> byType(
            @Parameter(description = "Typ der Meldung: LOST oder FOUND") @PathVariable @NonNull ReportType type) {
        return new ResponseEntity<>(reportService.getReportsByType(type), HttpStatus.OK);
    }

    @PostMapping("api/report")
    @RolesAllowed({Roles.User, Roles.Admin})
    @Operation(summary = "Neue Meldung erfassen")
    public ResponseEntity<Report> newReport(@Valid @RequestBody @NonNull Report report) {
        return new ResponseEntity<>(reportService.insertReport(report), HttpStatus.OK);
    }

    @PutMapping("api/report/{id}")
    @RolesAllowed(Roles.Admin)
    @Operation(summary = "Meldung aktualisieren (nur Admin)")
    public ResponseEntity<Report> updateReport(
            @Valid @RequestBody @NonNull Report report,
            @Parameter(description = "ID der Meldung") @PathVariable @NonNull Long id) {
        return new ResponseEntity<>(reportService.updateReport(report, id), HttpStatus.OK);
    }

    @DeleteMapping("api/report/{id}")
    @RolesAllowed(Roles.Admin)
    @Operation(summary = "Meldung löschen (nur Admin)")
    public void deleteReport(
            @Parameter(description = "ID der Meldung") @PathVariable @NonNull Long id) {
        reportService.deleteReport(id);
    }
}
