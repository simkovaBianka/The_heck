package pro1a.ics.upjs.sk.the_heck.model;

import com.google.gson.annotations.Expose;

public class AppointmentDoctor {
    @Expose
    private Long id;

    @Expose
    private String firstName;

    @Expose
    private String lastName;

    @Expose
    private String office;

    @Expose
    private String specialization;

    public AppointmentDoctor(Long id, String firstName, String lastName, String office, String specialization) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.office = office;
        this.specialization = specialization;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
