package pl.madej.finansemanangerrestapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.madej.finansemanangerrestapi.model.User;
import pl.madej.finansemanangerrestapi.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByUsername(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + id + " not found"));
    }
}
