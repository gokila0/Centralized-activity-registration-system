package com.example.activity_registration;

public class Admin {
    String name1,email1,profession,role,AdLink;
    public Admin()
    {
    }

    public Admin(String name1,String email1,String profession,String role,String AdLink)
    {
        this.name1=name1;
        this.email1=email1;
        this.profession=profession;
        this.role = role;
        this.AdLink=AdLink;
    }

    public String getName() {
        return name1;
    }

    public void setName(String name1) {
        this.name1 = name1;
    }

    public String getEmail() {
        return email1;
    }

    public void setEmail(String email1) {
        this.email1 = email1;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }


    public String getLink() {
        return AdLink;
    }

    public void setLink(String AdLink) {
        this.AdLink = AdLink;
    }

}
