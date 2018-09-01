package dk.darkares.PassProtect.services;

import dk.darkares.PassProtect.models.User;
import dk.darkares.PassProtect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUserByName(String name) {
        return userRepository.findByName(name);
    }

    public User createUser(User user) {
        user = userRepository.save(user);
        userRepository.flush();
        return user;
    }
}
