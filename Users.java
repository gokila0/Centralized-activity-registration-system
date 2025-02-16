package com.example.activity_registration;

public class Users {
    String name,email,roll_no,mob_no;

    public Users() {
    }

    public Users(String name, String email, String roll_no, String mob_no) {
        this.name = name;
        this.email = email;
        this.roll_no = roll_no;
        this.mob_no = mob_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    public String getMob_no() {
        return mob_no;
    }

    public void setMob_no(String mob_no) {
        this.mob_no = mob_no;
    }
}
