package ch.stoeckli.lionel.lostandfound.item;

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
@Tag(name = "Item", description = "Verwaltung der Gegenstände (Items) auf der Plattform")
public class ItemController {

    private final ItemService itemService;

    ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("api/item")
    @RolesAllowed({Roles.User, Roles.Admin})
    @Operation(summary = "Liste aller Items abrufen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste erfolgreich geladen"),
            @ApiResponse(responseCode = "401", description = "Nicht authentifiziert"),
            @ApiResponse(responseCode = "403", description = "Keine Berechtigung")
    })
    public ResponseEntity<List<Item>> all() {
        return new ResponseEntity<>(itemService.getItems(), HttpStatus.OK);
    }

    @GetMapping("api/item/{id}")
    @RolesAllowed({Roles.User, Roles.Admin})
    @Operation(summary = "Item per ID abrufen")
    public ResponseEntity<Item> one(
            @Parameter(description = "ID des Items") @PathVariable @NonNull Long id) {
        return new ResponseEntity<>(itemService.getItem(id), HttpStatus.OK);
    }

    @PostMapping("api/item")
    @RolesAllowed({Roles.User, Roles.Admin})
    @Operation(summary = "Neues Item anlegen")
    public ResponseEntity<Item> newItem(@Valid @RequestBody @NonNull Item item) {
        return new ResponseEntity<>(itemService.insertItem(item), HttpStatus.OK);
    }

    @PutMapping("api/item/{id}")
    @RolesAllowed(Roles.Admin)
    @Operation(summary = "Item aktualisieren (nur Admin)")
    public ResponseEntity<Item> updateItem(
            @Valid @RequestBody @NonNull Item item,
            @Parameter(description = "ID des Items") @PathVariable @NonNull Long id) {
        return new ResponseEntity<>(itemService.updateItem(item, id), HttpStatus.OK);
    }

    @DeleteMapping("api/item/{id}")
    @RolesAllowed(Roles.Admin)
    @Operation(summary = "Item löschen (nur Admin)")
    public void deleteItem(
            @Parameter(description = "ID des Items") @PathVariable @NonNull Long id) {
        itemService.deleteItem(id);
    }
}
