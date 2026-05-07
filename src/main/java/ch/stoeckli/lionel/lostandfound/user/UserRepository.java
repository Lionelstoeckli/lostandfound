package ch.stoeckli.lionel.lostandfound.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKeycloakSub(String keycloakSub);
}
