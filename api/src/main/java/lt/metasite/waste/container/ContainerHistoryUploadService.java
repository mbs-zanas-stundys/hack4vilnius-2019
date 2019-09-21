package lt.metasite.waste.container;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.StreamSupport;

import lt.metasite.waste.container.dto.PickupHistoryCsvDto;
import lt.metasite.waste.container.schedule.ScheduleRepository;
import lt.metasite.waste.csv.CsvUploadService;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

@Service
public class ContainerHistoryUploadService implements CsvUploadService {
    private final WasteContainerRepository repository;
    private final ScheduleRepository scheduleRepository;


    public ContainerHistoryUploadService(
            WasteContainerRepository repository,
            ScheduleRepository scheduleRepository) {
        this.repository = repository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public void parseFile(Path pathToFile) {
        try (
                Reader reader =
                        new BufferedReader(new FileReader(new File(pathToFile.toString()),
                                                          Charset.forName("Windows-1257")));
        ) {
            CsvToBean<PickupHistoryCsvDto> csvToBean = new CsvToBeanBuilder<PickupHistoryCsvDto>(reader)
                    .withType(PickupHistoryCsvDto.class)
                    .withSeparator(';')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            StreamSupport.stream(csvToBean.spliterator(), true)
                         .map(this::fromCsv)
                         .peek(p -> repository.pushHistory(p.getFirst(), p.getSecond()))
                         .forEach(p -> scheduleRepository.setNearestScheduleToCompleted(p.getSecond()
                                                                                         .getDate(),
                                                                                        p.getFirst()));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getFilePattern() {
        return "Konteineriu pakelimai";
    }


    private Pair<String,PickupHistory> fromCsv(PickupHistoryCsvDto csvDto){
        PickupHistory history = new PickupHistory();
        history.setWeight(csvDto.getWeight());
        history.setGarbageTruckRegNo(csvDto.getGarbageTruckRegNo());
        history.setDate(LocalDateTime.ofInstant(csvDto.getDate().toInstant(),
                                                ZoneId.systemDefault()));
        return Pair.of(csvDto.getContainerNo(),history);

    }
}
