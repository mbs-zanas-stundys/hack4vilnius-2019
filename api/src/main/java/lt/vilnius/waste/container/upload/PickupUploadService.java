package lt.vilnius.waste.container.upload;

import lt.vilnius.waste.container.Pickup;
import lt.vilnius.waste.container.repository.WasteContainerRepository;
import lt.vilnius.waste.system.UploadEvent;
import lt.vilnius.waste.system.UploadEventRepository;
import lt.vilnius.waste.system.UploadType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@Service
public class PickupUploadService implements UploadService {
    private final WasteContainerRepository repository;
    private final RestContainerService restContainerService;
    private final UploadEventRepository eventRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(PickupUploadService.class);


    public PickupUploadService(
            WasteContainerRepository repository, RestContainerService restContainerService, UploadEventRepository eventRepository) {
        this.repository = repository;
        this.restContainerService = restContainerService;
        this.eventRepository = eventRepository;
    }


    @Override
    public void fetchAndStore() {
        LOGGER.info("Upload started");
        LocalDate startDate = LocalDate.now().minusDays(1);
        PageRequest request = PageRequest.of(1000);
        do {
            Page<PickupDto> page = restContainerService.getPickupsForDateRange(request, startDate, startDate);
            processPage(page);
            request = page.next();
            LOGGER.info(String.format("Uploaded %s out of %s", page.getTo(), page.getTotal()));

        } while (request != null);

        eventRepository.save(UploadEvent.now(UploadType.PICKUP_UPLOAD));
        LOGGER.info("Upload finished");
    }

    @Scheduled(cron = "0 0 6 * * *")
    public void scheduledUpload() {
        if (eventRepository.findByTypeAndDateBetween(UploadType.PICKUP_UPLOAD, LocalDate.now().atTime(LocalTime.MIN), LocalDate.now().atTime(LocalTime.MAX)).isEmpty()) {
            fetchAndStore();
        }
    }

    private void processPage(Page<PickupDto> pickupDtoPage) {
        pickupDtoPage.getData()
                .stream()
                .filter(PickupDto::isValid)
                .forEach(dto -> repository.savePickup(dto.getContainerNo(), fromCsv(dto)));
    }

    private Pickup fromCsv(PickupDto dto) {
        Pickup pickup = new Pickup();
        pickup.setWeight(dto.getWeight());
        pickup.setDate(LocalDateTime.ofInstant(dto.getDate().toInstant(),
                ZoneId.systemDefault()));
        pickup.setCompany(dto.getCompany());
        return pickup;

    }
}
