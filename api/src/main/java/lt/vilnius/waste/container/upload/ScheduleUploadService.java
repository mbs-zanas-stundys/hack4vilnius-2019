package lt.vilnius.waste.container.upload;

import lt.vilnius.waste.container.repository.WasteContainerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class ScheduleUploadService implements UploadService {

    private final WasteContainerRepository repository;
    private final RestContainerService restContainerService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleUploadService.class);


    public ScheduleUploadService(WasteContainerRepository repository, RestContainerService restContainerService) {
        this.repository = repository;
        this.restContainerService = restContainerService;
    }

    @Override
    public void fetchAndStore() {

        LOGGER.info("Upload started");
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1);
        PageRequest request = PageRequest.of(1000);

        do {
            Page<ScheduleDto> page = restContainerService.getSchedulesPageForDate(request, startDate, endDate);
            processPage(page);
            request = page.next();
            LOGGER.info(String.format("Uploaded %s out of %s", page.getTo(), page.getTotal()));
        } while (request != null);

        LOGGER.info("Upload finished");

    }

    private void processPage(Page<ScheduleDto> page) {
        page.getData()
                .stream()
                .filter(ScheduleDto::isValid)
                .collect(groupingBy(ScheduleDto::getContainerNo))
                .forEach(this::saveSchedules);
    }

    private void saveSchedules(String containerNo, List<ScheduleDto> scheduleCsvDtos) {
        repository.saveSchedules(containerNo,
                scheduleCsvDtos.stream()
                        .map(ScheduleDto::getDate)
                        .collect(Collectors.toSet()));
    }

}
