package ch.stoeckli.lionel.lostandfound.report;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByType(ReportType type);
    List<Report> findByStatus(ReportStatus status);
}
