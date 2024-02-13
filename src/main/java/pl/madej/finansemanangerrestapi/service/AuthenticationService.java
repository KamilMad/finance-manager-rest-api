package pl.madej.finansemanangerrestapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.madej.finansemanangerrestapi.payload.security.AuthenticationRequest;
import pl.madej.finansemanangerrestapi.payload.security.AuthenticationResponse;
import pl.madej.finansemanangerrestapi.payload.security.RegisterRequest;
import pl.madej.finansemanangerrestapi.repository.UserRepository;
import pl.madej.finansemanangerrestapi.security.JwtService;
import pl.madej.finansemanangerrestapi.model.User;
import pl.madej.finansemanangerrestapi.security.JwtUserDetails;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {

        User user = new User();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());

        user.setPassword(passwordEncoder.encode(registerRequest.password()));

        userRepository.save(user);
        UserDetails userDetails = new JwtUserDetails(user);
        var jwtToken = jwtService.generateToken(userDetails);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + request.getUsername()));

        UserDetails userDetails = new JwtUserDetails(user);

        var jwtToken = jwtService.generateToken(userDetails);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }
}
