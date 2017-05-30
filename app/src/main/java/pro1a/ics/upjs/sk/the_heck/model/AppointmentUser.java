package pro1a.ics.upjs.sk.the_heck.model;


import com.google.gson.annotations.Expose;

public class AppointmentUser {
    @Expose
    private Long id;

    @Expose
    private String firstName;

    @Expose
    private String lastName;

    public AppointmentUser(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
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
}

