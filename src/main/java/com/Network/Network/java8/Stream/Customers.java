package com.Network.Network.java8.Stream;

import com.Network.Network.DevicemetamodelPojo.Customer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Customers {
    private int id;
    private String name;
    private String email;
    private List<String> phonenumber;
    private Integer Income;

    public Customers(int id, String name, String email, List<String> phonenumber, Integer income) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phonenumber = phonenumber;
        Income = income;
    }

    public Customers() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(List<String> phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Integer getIncome() {
        return Income;
    }

    public void setIncome(Integer income) {
        Income = income;
    }

    @Override
    public String toString() {
        return "Customers{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phonenumber=" + phonenumber +
                ", Income=" + Income +
                '}';
    }
}
