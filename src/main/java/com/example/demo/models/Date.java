package com.example.demo.models;

public class Date {
    public int year;
    int month;
    int day;
    int hours;
    int minutes;
    int seconds;

    public Date(String allDate) {
        String date = allDate.substring(0, 10);
        String time = allDate.substring(11, 19);
        this.year = Integer.parseInt(date.split("-")[0]);
        this.month = Integer.parseInt(date.split("-")[1]);
        this.day = Integer.parseInt(date.split("-")[2]);
        this.hours = Integer.parseInt(time.split(":")[0]);
        this.minutes = Integer.parseInt(time.split(":")[1]);
        this.seconds = Integer.parseInt(time.split(":")[2]);
    }

    @Override
    public String toString() {
        return "2021-" + gps(month) + "-" + gps(day) + " " + gps(hours) + ":"
                + gps(minutes) + ":" + gps(seconds) + " GMT";
        //2021-01-29 17:46:37 GMT
    }

    private String gps(int number) {
        if(number < 10) {
            return "0" + number;
        }
        else {
            return number + "";
        }
    }
}
