package lt.vilnius.waste.container.upload;

import lt.vilnius.waste.container.repository.WasteContainerRepository;
import lt.vilnius.waste.system.UploadEvent;
import lt.vilnius.waste.system.UploadEventRepository;
import lt.vilnius.waste.system.UploadType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ContainerUploadService implements UploadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerUploadService.class);

    private final WasteContainerRepository repository;
    private final RestContainerService restContainerService;
    private final ScheduleUploadService scheduleUploadService;
    private final UploadEventRepository eventRepository;

    public ContainerUploadService(WasteContainerRepository repository,
                                  RestContainerService restContainerService, ScheduleUploadService scheduleUploadService, UploadEventRepository eventRepository) {
        this.repository = repository;
        this.restContainerService = restContainerService;
        this.scheduleUploadService = scheduleUploadService;
        this.eventRepository = eventRepository;
    }


    @Override
    public void fetchAndStore() {

        LOGGER.info("Upload started");

        PageRequest request = PageRequest.of(1000);
        do {
            Page<ContainerDto> page = restContainerService.getContainers(request);
            page.getData()
                    .stream()
                    .filter(ContainerDto::isValid)
                    .forEach(repository::addOrReplaceContainer);

            request = page.next();
            LOGGER.info(String.format("Uploaded %s out of %s", page.getTo(), page.getTotal()));
        } while (request != null);


        LOGGER.info("Container Upload finished");
        scheduleUploadService.fetchAndStore();
        eventRepository.save(UploadEvent.now(UploadType.CONTAINER_UPLOAD));
    }

    @Scheduled(cron = "0 0 0 1 * *")
    public void scheduledUpload() {
        if (eventRepository.findByTypeAndDate(UploadType.CONTAINER_UPLOAD, LocalDate.now()).isEmpty()) {
            fetchAndStore();
        }
    }

}

