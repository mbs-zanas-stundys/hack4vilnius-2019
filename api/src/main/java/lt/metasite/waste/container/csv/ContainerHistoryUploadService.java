package lt.metasite.waste.container.csv;

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

import lt.metasite.waste.container.PickupHistory;
import lt.metasite.waste.container.repository.WasteContainerRepository;
import lt.metasite.waste.container.dto.PickupHistoryCsvDto;
import lt.metasite.waste.container.repository.ScheduleRepository;
import lt.metasite.waste.commo.CsvUploadService;
import lt.metasite.waste.system.GitService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

@Service
public class ContainerHistoryUploadService implements CsvUploadService {
    private final WasteContainerRepository repository;
    private final ScheduleRepository scheduleRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerHistoryUploadService.class);



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
            LOGGER.info("Upload started");
            CsvToBean<PickupHistoryCsvDto> csvToBean = new CsvToBeanBuilder<PickupHistoryCsvDto>(reader)
                    .withType(PickupHistoryCsvDto.class)
                    .withSeparator(';')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            StreamSupport.stream(csvToBean.spliterator(), true)
                         .map(this::fromCsv)
                         .forEach(p -> repository.pushHistory(p.getFirst(), p.getSecond()));

            LOGGER.info("Upload finished");
        } catch (IOException e) {
            LOGGER.error("Unknown error: ",e);
        }

    }

    @Override
    public String getFilePattern() {
        return "Konteineriu pakelimai";
    }

    @Override
    public int getOrder() {
        return 3;
    }

    private Pair<String, PickupHistory> fromCsv(PickupHistoryCsvDto csvDto){
        PickupHistory history = new PickupHistory();
        history.setWeight(csvDto.getWeight());
        history.setGarbageTruckRegNo(csvDto.getGarbageTruckRegNo());
        history.setDate(LocalDateTime.ofInstant(csvDto.getDate().toInstant(),
                                                ZoneId.systemDefault()));
        return Pair.of(csvDto.getContainerNo(),history);

    }
}
