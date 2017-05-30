package pro1a.ics.upjs.sk.the_heck.model;

import com.google.gson.annotations.Expose;

public class User {

    @Expose
    private long id;
    @Expose
    private String login;
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private String password;
    @Expose
    private String address;
    @Expose
    private String email;
    @Expose
    private String phoneNumber;
    @Expose
    private String postalCode;
    @Expose
    private String city;


    public User(String username, String password) {
        this.login = username;
        this.password = password;
    }

    public User(long id, String login, String firstName, String lastName, String password,
                String address, String email, String phoneNumber, String postalCode, String city) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.postalCode = postalCode;
        this.city = city;
    }

    public User(String login, String firstName, String lastName, String password,
                String address, String email, String phoneNumber, String postalCode, String city) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.postalCode = postalCode;
        this.city = city;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
