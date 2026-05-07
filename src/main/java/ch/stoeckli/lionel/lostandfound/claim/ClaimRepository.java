package ch.stoeckli.lionel.lostandfound.claim;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByStatus(ClaimStatus status);
}
