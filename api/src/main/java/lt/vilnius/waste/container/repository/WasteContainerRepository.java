package lt.vilnius.waste.container.repository;

import java.util.Optional;

import lt.vilnius.waste.container.Container;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface WasteContainerRepository extends MongoRepository<Container, String>, WasteContainerRepositoryCustom {
    Optional<Container> findByContainerNo(String containerNo);
}
