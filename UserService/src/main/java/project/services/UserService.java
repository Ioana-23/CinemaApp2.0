package project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.entities.User;
import project.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findUserByUuid(int id) {
        return userRepository.findUserByUuid(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

