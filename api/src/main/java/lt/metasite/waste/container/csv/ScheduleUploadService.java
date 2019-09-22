package lt.metasite.waste.container.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import lt.metasite.waste.container.Schedule;
import lt.metasite.waste.container.dto.ScheduleCsvDto;
import lt.metasite.waste.container.repository.ScheduleRepository;
import lt.metasite.waste.commo.CsvUploadService;
import lt.metasite.waste.system.GitService;

import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import static java.time.LocalDateTime.now;

@Service
public class ScheduleUploadService implements CsvUploadService {

    private final ScheduleRepository repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleUploadService.class);


    public ScheduleUploadService(ScheduleRepository repository) {
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

            List<Schedule> list = StreamSupport.stream(csvToBean.spliterator(), true)
                                               .map(this::fromCsv)
                                               .collect(Collectors.toList());
            List<String> existingContainers = repository.findAll()
                    .stream()
                    .map(s->s.getContainerNo()+"_"+s.getExpectedDate())
                    .collect(Collectors.toList());

           repository.saveAll(list.stream()
                                                .filter(s -> !existingContainers.contains(s.getContainerNo()+"_"+s.getExpectedDate()))
                                                .collect(Collectors.toList()));

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
        return 1;
    }

    private Schedule fromCsv(ScheduleCsvDto csvDto){
        Schedule schedule = new Schedule();
        schedule.setCompany(csvDto.getCompany());
        schedule.setContainerNo(csvDto.getContainerNo());
        schedule.setExpectedDate(LocalDate.ofInstant(csvDto.getExpectedDate().toInstant(), ZoneId.systemDefault()));
        return schedule;
    }
}
