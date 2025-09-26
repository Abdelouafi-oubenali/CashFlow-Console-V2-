package main.java.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Client {
    private UUID id;
    private String firstname;
    private String lastname;
    private String cin;
    private String phone;
    private String email;
    private String address;
    private LocalDateTime createdAt;

    public Client( String firstname, String lastname, String cin, String phone, String email, String address) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.cin = cin;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.createdAt = createdAt;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getCin() {
        return cin;
    }
    public UUID getId()
    {
        return id ;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
