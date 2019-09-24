package lt.metasite.waste.container.csv;

import com.opencsv.bean.CsvBindByName;

public class ContainerCsvDto {
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
    private Double capacity;
    @CsvBindByName(column = "Vezejas")
    private String company;

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitutde() {
        return latitutde;
    }

    public void setLatitutde(Double latitutde) {
        this.latitutde = latitutde;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
