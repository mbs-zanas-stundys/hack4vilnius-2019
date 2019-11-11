package lt.vilnius.waste.system;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface UploadEventRepository extends CrudRepository<UploadEvent, String> {
    Optional<UploadEvent> findByTypeAndDateBetween(UploadType type, LocalDateTime dateFrom, LocalDateTime dateTo);

    Optional<UploadEvent> findByTypeAndDate(UploadType type, LocalDate date);
}
