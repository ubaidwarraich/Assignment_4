package com.example.emailactivation.models;

public class Form {
    private int id;
    private int applicantId;
    private String message;
    private String approvedBy;
    private int isApproved;
    private String comments;

    public Form() {
    }

    public Form(int id, int applicantId, String message, String approvedBy, int isApproved, String comments) {
        this.id = id;
        this.applicantId = applicantId;
        this.message = message;
        this.approvedBy = approvedBy;
        this.isApproved = isApproved;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(int applicantId) {
        this.applicantId = applicantId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public int isApproved() {
        return isApproved;
    }

    public void setApproved(int approved) {
        isApproved = approved;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
