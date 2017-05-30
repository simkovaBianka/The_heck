package pro1a.ics.upjs.sk.the_heck.model;

import com.google.gson.annotations.Expose;

public class Appointments {
    @Expose
    private Long id;
    @Expose
    private String from;
    @Expose
    private String to;
    @Expose
    private String note;
    @Expose
    private String subject;
    @Expose
    private Boolean occupied;
    @Expose
    private AppointmentUser user;
    @Expose
    private AppointmentDoctor doctor;
    @Expose
    private String patient;
    @Expose
    private Boolean canceled;
    @Expose
    private Boolean holiday;

    public Appointments() {

    }

    public Appointments(Long id, String from, String to, String note, String subject, Boolean occupied,
                        AppointmentUser user, AppointmentDoctor doctor, String patient,
                        Boolean canceled, Boolean holiday) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.note = note;
        this.subject = subject;
        this.occupied = occupied;
        this.user = user;
        this.doctor = doctor;
        this.patient = patient;
        this.canceled = canceled;
        this.holiday = holiday;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Boolean getOccupied() {
        return occupied;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }

    public AppointmentUser getUser() {
        return user;
    }

    public void setUser(AppointmentUser user) {
        this.user = user;
    }

    public AppointmentDoctor getDoctor() {
        return doctor;
    }

    public void setDoctor(AppointmentDoctor doctor) {
        this.doctor = doctor;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public Boolean getCanceled() {
        return canceled;
    }

    public void setCanceled(Boolean canceled) {
        this.canceled = canceled;
    }

    public Boolean getHoliday() {
        return holiday;
    }

    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }
}
