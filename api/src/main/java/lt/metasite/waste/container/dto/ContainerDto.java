package lt.metasite.waste.container.dto;

import com.opencsv.bean.CsvBindByName;

public class ContainerDto {
    @CsvBindByName(column = "Konteinerio Nr")
    private String containerNo;
    @CsvBindByName(column = "Konteinerio longitude")
    private Double longitude;
    @CsvBindByName(column = "Konteinerio latitude")
    private Double latitutde;
    @CsvBindByName(column = "Konteinerio_vietove")
    private String location;
    @CsvBindByName(column = "Konteinerio_gatve")
    private String street;
    @CsvBindByName(column = "Konteinerio_namas")
    private String houseNo;
    @CsvBindByName(column = "Konteinerio_butas")
    private String flatNo;
    @CsvBindByName(column = "Konteinerio talpa")
    private String capacity;
}
