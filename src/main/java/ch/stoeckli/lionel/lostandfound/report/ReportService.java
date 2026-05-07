package ch.stoeckli.lionel.lostandfound.report;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import ch.stoeckli.lionel.lostandfound.item.Item;
import ch.stoeckli.lionel.lostandfound.item.ItemRepository;
import ch.stoeckli.lionel.lostandfound.storage.EntityNotFoundException;
import ch.stoeckli.lionel.lostandfound.user.User;
import ch.stoeckli.lionel.lostandfound.user.UserRepository;

@Service
public class ReportService {

    private final ReportRepository repository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ReportService(ReportRepository repository,
                         ItemRepository itemRepository,
                         UserRepository userRepository) {
        this.repository = repository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public List<Report> getReports() {
        return repository.findAll();
    }

    public Report getReport(@NonNull Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Report.class));
    }

    public List<Report> getReportsByType(@NonNull ReportType type) {
        return repository.findByType(type);
    }

    public Report insertReport(@NonNull Report report) {
        report.setId(null);
        report.setItem(resolveItem(report.getItem()));
        report.setUser(resolveUser(report.getUser()));
        return repository.save(report);
    }

    public Report updateReport(@NonNull Report report, @NonNull Long id) {
        return repository.findById(id)
                .map(orig -> {
                    orig.setType(report.getType());
                    orig.setStatus(report.getStatus());
                    orig.setLocation(report.getLocation());
                    orig.setReportedAt(report.getReportedAt());
                    orig.setItem(resolveItem(report.getItem()));
                    orig.setUser(resolveUser(report.getUser()));
                    return repository.save(orig);
                })
                .orElseGet(() -> {
                    report.setId(null);
                    report.setItem(resolveItem(report.getItem()));
                    report.setUser(resolveUser(report.getUser()));
                    return repository.save(report);
                });
    }

    public void deleteReport(@NonNull Long id) {
        repository.deleteById(id);
    }

    private Item resolveItem(Item item) {
        if (item == null || item.getId() == null) {
            throw new IllegalArgumentException("Report needs an item with a valid id");
        }
        if (!itemRepository.existsById(item.getId())) {
            throw new EntityNotFoundException(item.getId(), Item.class);
        }
        return itemRepository.getReferenceById(item.getId());
    }

    private User resolveUser(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("Report needs a user with a valid id");
        }
        if (!userRepository.existsById(user.getId())) {
            throw new EntityNotFoundException(user.getId(), User.class);
        }
        return userRepository.getReferenceById(user.getId());
    }
}
