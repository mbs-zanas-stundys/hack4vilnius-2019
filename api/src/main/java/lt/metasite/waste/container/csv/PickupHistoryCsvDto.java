package lt.metasite.waste.container.csv;

import java.util.Date;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

public class PickupHistoryCsvDto {

    @CsvBindByName(column = "Pakelimo iraso ID")
    private String externalId;
    @CsvBindByName(column = "Konteinerio Nr")
    private String containerNo;
    @CsvDate(value = "yyyy-MM-dd HH:mm:ss")
    @CsvBindByName(column = "Pakelimo data, laikas")
    private Date date;
    @CsvBindByName(column = "Automobilis")
    private String garbageTruckRegNo;
    @CsvBindByName(column = "Svoris kg.")
    private Double weight;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getGarbageTruckRegNo() {
        return garbageTruckRegNo;
    }

    public void setGarbageTruckRegNo(String garbageTruckRegNo) {
        this.garbageTruckRegNo = garbageTruckRegNo;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
