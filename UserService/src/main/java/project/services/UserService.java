package project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.entities.User;
import project.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserByUuid(int id) {
        Optional<User> userFound = userRepository.findUserByUuid(id);
        return userFound.orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

