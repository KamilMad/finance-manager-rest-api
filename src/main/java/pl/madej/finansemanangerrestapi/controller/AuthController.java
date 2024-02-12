package pl.madej.finansemanangerrestapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.madej.finansemanangerrestapi.payload.AuthenticationRequest;
import pl.madej.finansemanangerrestapi.payload.AuthenticationResponse;
import pl.madej.finansemanangerrestapi.payload.RegisterRequest;
import pl.madej.finansemanangerrestapi.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

   private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register (
            @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate (
            @RequestBody AuthenticationRequest request) {

        return ResponseEntity.ok(authenticationService.authenticate(request));

    }
}
