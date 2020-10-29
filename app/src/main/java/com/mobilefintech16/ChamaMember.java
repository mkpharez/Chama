package com.mobilefintech16;

public class ChamaMember {
    private String firstName,lastName;
    private String id;
    private String email;
    private String phone;

    public ChamaMember(String firstName,String lastName, String id, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.email = email;
        this.phone = phone;
    }

    public ChamaMember() {
    }
}
