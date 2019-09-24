package lt.metasite.waste.container.csv;

import java.util.Date;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

public class ScheduleCsvDto {

    @CsvBindByName(column = "Isvezimo plano ID")
    private String externalId;

    @CsvBindByName(column = "Konteinerio Nr")
    private String containerNo;

    @CsvBindByName(column = "Vezejas")
    private String company;

    @CsvDate(value = "yyyy-MM-dd")
    @CsvBindByName(column = "Planuojama isvezimo data")
    private Date expectedDate;

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public Date getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(Date expectedDate) {
        this.expectedDate = expectedDate;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
