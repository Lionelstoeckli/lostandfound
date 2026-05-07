package ch.stoeckli.lionel.lostandfound.user;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import ch.stoeckli.lionel.lostandfound.storage.EntityNotFoundException;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getUsers() {
        return repository.findAll();
    }

    public User getUser(@NonNull Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, User.class));
    }

    public User insertUser(@NonNull User user) {
        user.setId(null);
        return repository.save(user);
    }

    public User updateUser(@NonNull User user, @NonNull Long id) {
        return repository.findById(id)
                .map(orig -> {
                    orig.setKeycloakSub(user.getKeycloakSub());
                    orig.setUsername(user.getUsername());
                    orig.setEmail(user.getEmail());
                    return repository.save(orig);
                })
                .orElseGet(() -> repository.save(user));
    }

    public void deleteUser(@NonNull Long id) {
        repository.deleteById(id);
    }
}
