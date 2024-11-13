package co.edu.cue.pruebasConActions.service;

import co.edu.cue.pruebasConActions.domain.entities.User;
import co.edu.cue.pruebasConActions.repositories.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepo usersRepository;

    public UserService(UserRepo usersRepository) {
        this.usersRepository = usersRepository;
    }

    public User createUser(User user) {
        return usersRepository.save(user);
    }

    public User getUserById(Long userId) {
        return usersRepository.findById(userId)
                .orElse(null);
    }

    public User updateUser(Long userId, User user) {
        Optional<User> existingUser = usersRepository.findById(userId);

        if (existingUser.isPresent()) {
            User newProduct = existingUser.get();
            newProduct.setUserName(user.getUserName());
            newProduct.setUserEmail(user.getUserEmail());
            return usersRepository.save(newProduct);  // Save updated product
        }
        return null;
    }

    public void deleteUser(Long userId) {
        usersRepository.deleteById(userId);
    }

    public List<User> findUsersByUserEmailDomain(String domain) {
        return usersRepository.findByUserEmailEndingWith(domain);
    }
}