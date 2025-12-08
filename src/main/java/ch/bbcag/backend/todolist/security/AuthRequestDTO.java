package ch.bbcag.backend.todolist.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class AuthRequestDTO {

    @NotBlank(message = "username must not be empty")
    @Size(max = 45, message = "username length must be not more than 45")
    private String username;
    @NotBlank(message = "password must not be empty")
    @Size(min = 8, max = 255, message = "password length must be between 8 and 255")
    private String password;


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AuthRequestDTO authRequestDTO)) {
            return false;
        }

        return username.equals(authRequestDTO.username)
                && password.equals(authRequestDTO.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
