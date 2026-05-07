package ch.stoeckli.lionel.lostandfound.user;

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
@Tag(name = "User", description = "Verwaltung der Plattform-Benutzer")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("api/user")
    @RolesAllowed({Roles.User, Roles.Admin})
    @Operation(summary = "Liste aller Benutzer abrufen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste erfolgreich geladen"),
            @ApiResponse(responseCode = "401", description = "Nicht authentifiziert"),
            @ApiResponse(responseCode = "403", description = "Keine Berechtigung")
    })
    public ResponseEntity<List<User>> all() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("api/user/{id}")
    @RolesAllowed({Roles.User, Roles.Admin})
    @Operation(summary = "Benutzer per ID abrufen")
    public ResponseEntity<User> one(
            @Parameter(description = "ID des Benutzers") @PathVariable @NonNull Long id) {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @PostMapping("api/user")
    @RolesAllowed({Roles.User, Roles.Admin})
    @Operation(summary = "Neuen Benutzer anlegen")
    public ResponseEntity<User> newUser(@Valid @RequestBody @NonNull User user) {
        return new ResponseEntity<>(userService.insertUser(user), HttpStatus.OK);
    }

    @PutMapping("api/user/{id}")
    @RolesAllowed(Roles.Admin)
    @Operation(summary = "Benutzer aktualisieren (nur Admin)")
    public ResponseEntity<User> updateUser(
            @Valid @RequestBody @NonNull User user,
            @Parameter(description = "ID des Benutzers") @PathVariable @NonNull Long id) {
        return new ResponseEntity<>(userService.updateUser(user, id), HttpStatus.OK);
    }

    @DeleteMapping("api/user/{id}")
    @RolesAllowed(Roles.Admin)
    @Operation(summary = "Benutzer löschen (nur Admin)")
    public void deleteUser(
            @Parameter(description = "ID des Benutzers") @PathVariable @NonNull Long id) {
        userService.deleteUser(id);
    }
}
