package lt.metasite.waste.container;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import lt.metasite.waste.container.dto.ContainerCsvDto;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

@Service
public class ContainerService {
    private final WasteContainerRepository repository;

    public ContainerService(
            WasteContainerRepository repository) {
        this.repository = repository;
    }

    public String save(){
        Container container = new Container();


        return repository.save(container).getId();
    }

    public List<Container> findByPosition(Double longitude, Double latitude, Double distance){

        return repository.findByGeoLocation(longitude, latitude, distance);
    }

    public List<Container> findByAddress(String street, String house, String flat){

        return repository.findByAddress(street, house, flat);
    }
}
