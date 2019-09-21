package lt.metasite.waste.container.schedule;

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

import lt.metasite.waste.csv.CsvUploadService;

import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import static java.time.LocalDateTime.now;

@Service
public class ScheduleUploadService implements CsvUploadService {

    private final ScheduleRepository repository;

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
            CsvToBean<ScheduleCsvDto> csvToBean = new CsvToBeanBuilder<ScheduleCsvDto>(reader)
                    .withType(ScheduleCsvDto.class)
                    .withSeparator(';')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Schedule> list = StreamSupport.stream(csvToBean.spliterator(), true)
                                               .map(this::fromCsv)
                                               .collect(Collectors.toList());
            List<LocalDate> existingContainers = repository.findAll()
                    .stream()
                    .map(Schedule::getExpectedDate)
                    .collect(Collectors.toList());

           repository.saveAll(list.stream()
                                                .filter(c -> !existingContainers.contains(c.getExpectedDate()))
                                                .collect(Collectors.toList()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getFilePattern() {
        return "Konteineriu isvezimo grafikas";
    }

    private Schedule fromCsv(ScheduleCsvDto csvDto){
        Schedule schedule = new Schedule();
        schedule.setCompany(csvDto.getCompany());
        schedule.setContainerNo(csvDto.getContainerNo());
        schedule.setExpectedDate(LocalDate.ofInstant(csvDto.getExpectedDate().toInstant(),
                                                     ZoneId.systemDefault()));
        if(schedule.getExpectedDate().isBefore(LocalDate.now())){
            schedule.setActualDate(now());
        }
        return schedule;
    }
}
