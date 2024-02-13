package pl.madej.finansemanangerrestapi.payload.security;

public record RegisterRequest(
        String username,
        String password,
        String email
) {
}
