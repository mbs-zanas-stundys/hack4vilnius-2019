package lt.vilnius.waste.container.csv;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lt.vilnius.waste.commo.CsvUploadService;
import lt.vilnius.waste.container.Pickup;
import lt.vilnius.waste.container.repository.WasteContainerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.StreamSupport;

@Service
public class ContainerHistoryUploadService implements CsvUploadService {
    private final WasteContainerRepository repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerHistoryUploadService.class);


    public ContainerHistoryUploadService(
            WasteContainerRepository repository) {
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
            CsvToBean<PickupHistoryCsvDto> csvToBean = new CsvToBeanBuilder<PickupHistoryCsvDto>(reader)
                    .withType(PickupHistoryCsvDto.class)
                    .withSeparator(';')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            StreamSupport.stream(csvToBean.spliterator(), true)
                    .map(this::fromCsv)
                    .forEach(p -> repository.findByContainerNo(p.getFirst())
                            .map(h -> h.withHistory(p.getSecond()))
                            .ifPresent(repository::save));

            LOGGER.info("Upload finished");
        } catch (IOException e) {
            LOGGER.error("Unknown error: ", e);
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

    private Pair<String, Pickup> fromCsv(PickupHistoryCsvDto csvDto) {
        Pickup history = new Pickup();
        history.setWeight(csvDto.getWeight());
        history.setGarbageTruckRegNo(csvDto.getGarbageTruckRegNo());
        history.setDate(LocalDateTime.ofInstant(csvDto.getDate().toInstant(),
                ZoneId.systemDefault()));
        history.setExternalId(csvDto.getExternalId());
        return Pair.of(csvDto.getContainerNo(), history);

    }
}
