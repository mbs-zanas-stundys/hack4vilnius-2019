package lt.metasite.waste.container.repository;

import lt.metasite.waste.container.Container;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface WasteContainerRepository extends MongoRepository<Container, String>, WasteContainerRepositoryCustom {
    Optional<Container> findByContainerNo(String containerNo);
}
