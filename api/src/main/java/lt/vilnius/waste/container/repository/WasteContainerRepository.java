package lt.vilnius.waste.container.repository;

import lt.vilnius.waste.container.Container;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WasteContainerRepository extends MongoRepository<Container, String>, WasteContainerRepositoryCustom {
    Optional<Container> findByContainerNo(String containerNo);
}
