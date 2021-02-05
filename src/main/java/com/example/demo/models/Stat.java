package com.example.demo.models;

import java.util.*;

public class Stat {
    private Map<String, Double> info= new HashMap<>();

    private Date date;

    public Stat() { }

    public void add(String id, double rate) {
        info.put(id, rate);
    }

    public boolean check() {
        return info.size() >= 3;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean compare(String date) {
        return this.date.toString().equals(date);
    }

    public boolean notEmpty(String id) {
        return info.containsKey(id);
    }

    public Map<String, Double> getInfo() {
        return info;
    }

    public void merge(String id, double rate) {
        double newRate = (info.get(id) + rate) * 0.5;
        info.replace(id, newRate);
    }
}
