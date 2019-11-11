package lt.vilnius.waste.system;

import lt.vilnius.waste.container.upload.ContainerUploadService;
import lt.vilnius.waste.container.upload.PickupUploadService;
import lt.vilnius.waste.container.upload.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static java.time.LocalDateTime.now;

@Component
public class ApplicationEventListener {
    private final ContainerUploadService containerUploadService;
    private final PickupUploadService pickupUploadService;
    private final UploadEventRepository uploadEventRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationEventListener.class);

    public ApplicationEventListener(ContainerUploadService containerUploadService,
                                    PickupUploadService pickupUploadService,
                                    UploadEventRepository uploadEventRepository) {
        this.containerUploadService = containerUploadService;
        this.pickupUploadService = pickupUploadService;
        this.uploadEventRepository = uploadEventRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void afterStartup() {
        run(UploadType.CONTAINER_UPLOAD, containerUploadService);
        run(UploadType.PICKUP_UPLOAD, pickupUploadService);
    }

    private void run(UploadType type, UploadService service) {
        uploadEventRepository.findByTypeAndDateBetween(type, now().minus(type.getPeriodBetweenEvents()), now())
                .ifPresentOrElse(systemEvent -> LOGGER.info(type + " was already run"), service::fetchAndStore);
    }
}
