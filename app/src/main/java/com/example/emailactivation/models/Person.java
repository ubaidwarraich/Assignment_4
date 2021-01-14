package com.example.emailactivation.models;

public abstract class Person {
    private int id;
    private String name;
   private  String cnic;

   public Person(){}
    public Person(int id, String name, String cnic) {
        this.id = id;
        this.name = name;
        this.cnic = cnic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }
}
