package ch.bbcag.backend.todolist.security;


public class JwtResponseDTO {
    private final String accessToken;
    private final String username;

    public JwtResponseDTO(String accessToken, String username) {
        this.accessToken = accessToken;
        this.username = username;
    }

    @Override
    public String toString() {
        return "JwtResponseDTO[" +
                "accessToken=" + accessToken + ", " +
                "username=" + username + ']';
    }

    public String getAccessToken() {
        return accessToken;
    }
}
