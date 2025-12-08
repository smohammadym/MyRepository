package ch.bbcag.backend.todolist.security;

import java.util.Objects;

public class AuthResponseDTO {
    private final Integer id;
    private final String username;

    public AuthResponseDTO(Integer id, String username) {
        this.id = id;
        this.username = username;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (AuthResponseDTO) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    @Override
    public String toString() {
        return "AuthResponseDTO[" +
                "id=" + id + ", " +
                "username=" + username + ']';
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
