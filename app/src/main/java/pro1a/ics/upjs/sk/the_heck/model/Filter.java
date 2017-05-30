package pro1a.ics.upjs.sk.the_heck.model;


import java.security.Timestamp;
import java.util.Date;

public class Filter {
    private String specialization;
    private String lastName;
    private String city;
    private Date toDate;
    private Date fromDate;

    public Filter(String specialization, String lastName, String city, Date toDate, Date fromDate) {
        this.specialization = specialization;
        this.lastName = lastName;
        this.city = city;
        this.toDate = toDate;
        this.fromDate = fromDate;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
}
