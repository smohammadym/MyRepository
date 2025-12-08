package ch.bbcag.backend.todolist.tag;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(TagController.PATH)
public class TagController {
    public static final String PATH = "/tags";

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a Tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag found",
                    content = @Content(schema = @Schema(implementation = TagResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tag was not found",
                    content = @Content)
    })
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        try {
            Tag tag = tagService.findById(id);
            return ResponseEntity.ok(TagMapper.toResponseDTO(tag));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag was not found");
        }
    }

    @PostMapping
    @Operation(summary = "Create a tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "tag inserted",
                    content = @Content(schema = @Schema(implementation = TagRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "tag was not inserted",
                    content = @Content)
    })
    public ResponseEntity<?> insert(@Valid @RequestBody TagRequestDTO newTagDTO) {
        try {
            Tag newTag = TagMapper.fromRequestDTO(newTagDTO);
            Tag savedTag = tagService.insert(newTag);
            return ResponseEntity.status(HttpStatus.CREATED).body(TagMapper.toResponseDTO(savedTag));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tag could not be created");
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tag was deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Tag could not be deleted",
                    content = @Content)
    })
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        try {
            tagService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag was not found");
        }
    }

    @GetMapping
    @Operation(summary = "Get all tags / Get tags by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tags found",
                    content = @Content(schema = @Schema(implementation = TagResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tags were not found",
                    content = @Content)
    })
    public ResponseEntity<?> findTags(@RequestParam(required = false) String name) {
        List<Tag> tags = name != null
                ? tagService.findByName(name)
                : tagService.findAll();

        return ResponseEntity.ok(tags.stream()
                .map(TagMapper::toResponseDTO)
                .toList());
    }

    @PatchMapping("{id}")
    @Operation(summary = "Update a tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag was updated successfully",
                    content = @Content(schema = @Schema(implementation = TagResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tag was not found",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "There was a conflict while updating the tag",
                    content = @Content)
    })
    public ResponseEntity<?> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The tag to update")
            @RequestBody TagResponseDTO updateTagDTO,
            @PathVariable Integer id
    ) {
        try {
            Tag updateTag = TagMapper.fromRequestDTO(updateTagDTO);
            Tag savedTag = tagService.update(updateTag, id);
            return ResponseEntity.ok(TagMapper.toResponseDTO(savedTag));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tag could not be created");
        }
    }
}
