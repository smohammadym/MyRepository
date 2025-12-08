package ch.bbcag.backend.todolist.security;

import ch.bbcag.backend.todolist.person.Person;
import ch.bbcag.backend.todolist.person.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(AuthController.PATH)
public class AuthController {
    public static final String PATH = "/auth";

    private final PersonService personService;
    private final AuthenticationManager authenticationManager;

    public AuthController(PersonService personService, AuthenticationManager authenticationManager) {
        this.personService = personService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User was created successfully",
                    content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "User could not be created, username already in use",
                    content = @Content)
    })
    @SecurityRequirements //no security here, default is BEARER
    public ResponseEntity<?> signUp(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The user to register")
            @Valid @RequestBody AuthRequestDTO newAuthDTO
    ) {
        try {
            Person newAuth = AuthMapper.fromRequestDTO(newAuthDTO);
            Person savedAuth = personService.create(newAuth);
            return ResponseEntity.status(201).body(AuthMapper.toResponseDTO(savedAuth));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User could not be created, username already in use");
        }
    }

    @PostMapping("/signin")
    @Operation(summary = "Receive a token for BEARER authorization")
    @SecurityRequirements //no security here, default is BEARER
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = JwtResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content)
    })
    public ResponseEntity<?> signIn(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The user to login")
            @RequestBody AuthRequestDTO newAuthDTO
    ) {
        String username = newAuthDTO.getUsername();
        String password = newAuthDTO.getPassword();
        Authentication token = new UsernamePasswordAuthenticationToken(username, password);

        if (authenticationManager.authenticate(token).isAuthenticated()) {
            String jwt = JwtGenerator.generateJwtToken(username);
            return ResponseEntity.ok(new JwtResponseDTO(jwt, username));
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials!");
        }
    }
}
