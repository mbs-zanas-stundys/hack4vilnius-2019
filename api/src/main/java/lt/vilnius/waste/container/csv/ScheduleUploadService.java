package lt.vilnius.waste.container.csv;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lt.vilnius.waste.commo.CsvUploadService;
import lt.vilnius.waste.container.Schedule;
import lt.vilnius.waste.container.repository.WasteContainerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.groupingBy;

@Service
public class ScheduleUploadService implements CsvUploadService {

    private final WasteContainerRepository repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleUploadService.class);


    public ScheduleUploadService(WasteContainerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void parseFile(Path pathToFile) {
        try (
                Reader reader =
                        new BufferedReader(new FileReader(new File(pathToFile.toString()),
                                                          Charset.forName("Windows-1257")));
        ) {

            LOGGER.info("Upload started");
            CsvToBean<ScheduleCsvDto> csvToBean = new CsvToBeanBuilder<ScheduleCsvDto>(reader)
                    .withType(ScheduleCsvDto.class)
                    .withSeparator(';')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            StreamSupport.stream(csvToBean.spliterator(), true)
                    .collect(groupingBy(ScheduleCsvDto::getContainerNo))
                    .forEach(this::saveSchedules);

           LOGGER.info("Upload finished");
        } catch (IOException e) {
            LOGGER.error("unkonw error", e);
        }
    }

    @Override
    public String getFilePattern() {
        return "Konteineriu isvezimo grafikas";
    }

    @Override
    public int getOrder() {
        return 2;
    }

    private void saveSchedules(String containerNo, List<ScheduleCsvDto> scheduleCsvDtos){
        Map<LocalDate, List<Schedule>> schedules =
                scheduleCsvDtos.stream()
                        .map(this::fromCsv)
                        .collect(groupingBy(i -> i.getExpectedDate().withDayOfMonth(1)));
        repository.findByContainerNo(containerNo)
                .map(c -> c.withSchedules(schedules))
                .ifPresent(repository::save);
    }

    private Schedule fromCsv(ScheduleCsvDto csvDto){
        Schedule schedule = new Schedule();
        schedule.setExpectedDate(LocalDate.ofInstant(csvDto.getExpectedDate().toInstant(), ZoneId.systemDefault()));
        schedule.setExternalId(csvDto.getExternalId());
        return schedule;
    }
}
