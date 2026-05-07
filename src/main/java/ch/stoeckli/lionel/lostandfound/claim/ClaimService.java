package ch.stoeckli.lionel.lostandfound.claim;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import ch.stoeckli.lionel.lostandfound.report.Report;
import ch.stoeckli.lionel.lostandfound.report.ReportRepository;
import ch.stoeckli.lionel.lostandfound.storage.EntityNotFoundException;
import ch.stoeckli.lionel.lostandfound.user.User;
import ch.stoeckli.lionel.lostandfound.user.UserRepository;

@Service
public class ClaimService {

    private final ClaimRepository repository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public ClaimService(ClaimRepository repository,
                        ReportRepository reportRepository,
                        UserRepository userRepository) {
        this.repository = repository;
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

    public List<Claim> getClaims() {
        return repository.findAll();
    }

    public Claim getClaim(@NonNull Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Claim.class));
    }

    public List<Claim> getClaimsByStatus(@NonNull ClaimStatus status) {
        return repository.findByStatus(status);
    }

    public Claim insertClaim(@NonNull Claim claim) {
        claim.setId(null);
        claim.setReport(resolveReport(claim.getReport()));
        claim.setUser(resolveUser(claim.getUser()));
        return repository.save(claim);
    }

    public Claim updateClaim(@NonNull Claim claim, @NonNull Long id) {
        return repository.findById(id)
                .map(orig -> {
                    orig.setMessage(claim.getMessage());
                    orig.setStatus(claim.getStatus());
                    orig.setCreatedAt(claim.getCreatedAt());
                    orig.setReport(resolveReport(claim.getReport()));
                    orig.setUser(resolveUser(claim.getUser()));
                    return repository.save(orig);
                })
                .orElseGet(() -> {
                    claim.setId(null);
                    claim.setReport(resolveReport(claim.getReport()));
                    claim.setUser(resolveUser(claim.getUser()));
                    return repository.save(claim);
                });
    }

    public void deleteClaim(@NonNull Long id) {
        repository.deleteById(id);
    }

    private Report resolveReport(Report report) {
        if (report == null || report.getId() == null) {
            throw new IllegalArgumentException("Claim needs a report with a valid id");
        }
        if (!reportRepository.existsById(report.getId())) {
            throw new EntityNotFoundException(report.getId(), Report.class);
        }
        return reportRepository.getReferenceById(report.getId());
    }

    private User resolveUser(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("Claim needs a user with a valid id");
        }
        if (!userRepository.existsById(user.getId())) {
            throw new EntityNotFoundException(user.getId(), User.class);
        }
        return userRepository.getReferenceById(user.getId());
    }
}
