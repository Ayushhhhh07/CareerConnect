
package com.example.placementportal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String applicantName;
    private String email;
    private String phone;
    private String internshipTitle;
    private String status;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public JobApplication() {
    }

    public JobApplication(
            String applicantName,
            String email,
            String phone,
            String internshipTitle) {
        this.applicantName = applicantName;
        this.email = email;
        this.phone = phone;
        this.internshipTitle = internshipTitle;
        this.status = "Pending";
    }

    public Long getId() {
        return id;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getInternshipTitle() {
        return internshipTitle;
    }

    public String getStatus() {
        return status;
    }

    public Student getStudent() {
        return student;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setInternshipTitle(String internshipTitle) {
        this.internshipTitle = internshipTitle;
    }

    public void setStatus(String status) {
        if (status == null
                || !(status.equals("Pending")
                || status.equals("Under Review")
                || status.equals("Shortlisted")
                || status.equals("Rejected"))) {
            throw new IllegalArgumentException(
                    "Invalid application status.");
        }

        this.status = status;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
