package com.example.lenovo.maandroid.Entity;

public class Dates {
    private String time;
    private String title;
    private int statu;

    public Dates() {
    }

    @Override
    public String toString() {
        return "Dates{" +
                "time='" + time + '\'' +
                ", title='" + title + '\'' +
                ", statu=" + statu +
                '}';
    }

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }

    public Dates(String time, String title, int statu) {
        this.time = time;
        this.title = title;
        this.statu = statu;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Dates(String time) {

        this.time = time;
        this.title = title;
    }
}
