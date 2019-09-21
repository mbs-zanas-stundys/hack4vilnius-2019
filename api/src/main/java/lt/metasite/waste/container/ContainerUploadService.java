package lt.metasite.waste.container;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import lt.metasite.waste.container.dto.ContainerCsvDto;
import lt.metasite.waste.csv.CsvUploadService;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

@Service
public class ContainerUploadService implements CsvUploadService {

    private final WasteContainerRepository repository;

    public ContainerUploadService(WasteContainerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void parseFile(Path pathToFile) {
            try (
                    Reader reader =
                            new BufferedReader(new FileReader(new File(pathToFile.toString()),
                                                              Charset.forName("Windows-1257")));
            ) {
                CsvToBean<ContainerCsvDto> csvToBean = new CsvToBeanBuilder<ContainerCsvDto>(reader)
                        .withType(ContainerCsvDto.class)
                        .withSeparator(';')
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                List<Container> list = StreamSupport.stream(csvToBean.spliterator(), false)
                                                    .map(this::fromCsv)
                                                    .collect(Collectors.toList());
                List<String> existingContainers = repository.findAll()
                        .stream()
                        .map(Container::getContainerNo)
                        .collect(Collectors.toList());

             repository.saveAll(list.stream()
                                                    .filter(c-> !existingContainers.contains(c.getContainerNo()))
                                                    .collect(Collectors.toList()));

            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    private Container fromCsv(ContainerCsvDto csvDto){
        Container container = new Container();
        container.setLocation(csvDto.getLocation());
        container.setPosition(new GeoJsonPoint(csvDto.getLongitude(), csvDto.getLatitutde()));
        container.setHouseNo(csvDto.getHouseNo());
        container.setContainerNo(csvDto.getContainerNo());
        container.setCapacity(csvDto.getCapacity());
        container.setStreet(csvDto.getStreet());
        container.setCompany(csvDto.getCompany());
        return container;
    }

    @Override
    public String getFilePattern() {
        return "Konteineriu sarasas";
    }
}
