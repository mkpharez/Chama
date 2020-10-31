package com.mobilefintech16;

public class ChamaMember {
    private String firstName,lastName;
    private String id;
    private String email,password;
    private String phone;

    public ChamaMember(String firstName,String lastName, String id, String email, String phone, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public ChamaMember(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public ChamaMember() {
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }
}
