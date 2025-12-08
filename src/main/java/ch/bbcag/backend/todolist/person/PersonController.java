package ch.bbcag.backend.todolist.person;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(PersonController.PATH)
public class PersonController {
    public static final String PATH = "/persons";

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    @Operation(summary = "Get all persons.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Persons found",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = PersonResponseDTO.class))
                    )
            ),
             @ApiResponse(responseCode = "404", description = "Persons not found",
                     content = @Content
             )
    })
    public ResponseEntity<?> findAll() {
        List<Person> persons = personService.findAll();

        return ResponseEntity.ok(persons.stream()
                .map(PersonMapper::toResponseDTO)
                .toList());
    }

    @GetMapping("{id}")
    @Operation(summary = "Get a person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person found",
                    content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Person was not found",
                    content = @Content)
    })
    public ResponseEntity<?> findById(@Parameter(description = "Id of person to get") @PathVariable Integer id) {
        try {
            Person person = personService.findById(id);
            return ResponseEntity.ok(PersonMapper.toResponseDTO(person));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person was not found");
        }
    }

    @PatchMapping("{id}")
    @Operation(summary = "Update a person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person was updated successfully",
                    content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Person was not found",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "There was a conflict while updating the person",
                    content = @Content)
    })
    public ResponseEntity<?> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The person to update")
            @RequestBody PersonRequestDTO updatePersonDTO,

            @PathVariable Integer id
    ) {
        try {
            Person updatePerson = PersonMapper.fromRequestDTO(updatePersonDTO);
            Person savedPerson = personService.update(updatePerson, id);
            return ResponseEntity.ok(PersonMapper.toResponseDTO(savedPerson));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "There was a conflict while updating the person");
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person was not found");
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Person was deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Person could not be deleted",
                    content = @Content)
    })
    public ResponseEntity<?> delete(@Parameter(description = "Id of person to delete") @PathVariable Integer id) {
        try {
            personService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person was not found");
        }
    }

}
