package ch.bbcag.backend.todolist.item;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = ItemController.PATH)
public class ItemController {
    public static final String PATH = "/items";

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an Item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item found",
                    content = @Content(schema = @Schema(implementation = ItemResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Item was not found",
                    content = @Content)
    })
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        try {
            Item item = itemService.findById(id);
            return ResponseEntity.ok(ItemMapper.toResponseDTO(item));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item was not found");
        }
    }

    @PostMapping
    @Operation(summary = "Create an item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item inserted",
                    content = @Content(schema = @Schema(implementation = ItemRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Item was not inserted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)
    })
    public ResponseEntity<?> insert(@Valid @RequestBody ItemRequestDTO newItemDTO) {
        try {
            Item newItem = ItemMapper.fromRequestDTO(newItemDTO);
            Item savedItem = itemService.insert(newItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(ItemMapper.toResponseDTO(savedItem));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Item could not be created");
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete an item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item was deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Item could not be deleted",
                    content = @Content)
    })
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        try {
            itemService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item was not found");
        }
    }

    @GetMapping
    @Operation(summary = "Get all items / Get items by name / Get items by tag name / Get items by name and tag name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items found",
                    content = @Content(schema = @Schema(implementation = ItemResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Items were not found",
                    content = @Content)
    })
    public ResponseEntity<?> findItems(@RequestParam(required = false) String name,
                                       @RequestParam(required = false) String tagName) {
        try {
            List<Item> items;
            if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(tagName)) {
                items = itemService.findByNameAndTagName(name, tagName);
            } else if (StringUtils.isNotBlank(name)) {
                items = itemService.findByName(name);
            } else if (StringUtils.isNotBlank(tagName)) {
                items = itemService.findByTagName(tagName);
            } else {
                items = itemService.findAll();
            }

            return ResponseEntity.ok(items.stream()
                    .map(ItemMapper::toResponseDTO)
                    .toList());
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Items were not found");
        }
    }

    @PatchMapping("{id}")
    @Operation(summary = "Update an item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item was updated successfully",
                    content = @Content(schema = @Schema(implementation = ItemResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Item was not found",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "There was a conflict while updating the item",
                    content = @Content)
    })
    public ResponseEntity<?> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The item to update")
            @RequestBody ItemResponseDTO updateItemDTO,
            @PathVariable Integer id
    ) {
        try {
            Item updateItem = ItemMapper.fromRequestDTO(updateItemDTO);
            Item savedItem = itemService.update(updateItem, id);
            return ResponseEntity.ok(ItemMapper.toResponseDTO(savedItem));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Item could not be created");
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item was not found");
        }
    }
}
