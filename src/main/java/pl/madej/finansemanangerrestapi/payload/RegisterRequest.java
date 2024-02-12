package pl.madej.finansemanangerrestapi.payload;

public record RegisterRequest(
        String username,
        String password,
        String email
) {
}
