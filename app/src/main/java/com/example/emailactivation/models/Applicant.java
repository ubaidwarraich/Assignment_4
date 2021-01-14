package com.example.emailactivation.models;

public class Applicant extends Person {
    private int UserId;
    private String rollNo;
    private String semester;

    public Applicant(){
        super();
    }


    public Applicant(int id, String name, String cnic, int userId, String rollNo, String semester) {
        super(id, name, cnic);
        UserId = userId;
        this.rollNo = rollNo;
        this.semester = semester;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
