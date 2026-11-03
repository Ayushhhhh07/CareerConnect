
package com.example.placementportal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Internship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String company;
    private String skills;
    private String location;
    private String stipend;

    public Internship() {
    }

    public Internship(String title, String company, String skills,
                      String location, String stipend) {
        this.title = title;
        this.company = company;
        this.skills = skills;
        this.location = location;
        this.stipend = stipend;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getSkills() {
        return skills;
    }

    public String getLocation() {
        return location;
    }

    public String getStipend() {
        return stipend;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStipend(String stipend) {
        this.stipend = stipend;
    }
}
