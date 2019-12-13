package com.example.lenovo.maandroid.Entity;

public class PositionInfo {
    private double curLatitude;
    private double curLongitude;

    public PositionInfo(double curLatitude, double curLongitude) {
        this.curLatitude = curLatitude;
        this.curLongitude = curLongitude;
    }

    public double getCurLatitude() {
        return curLatitude;
    }

    public void setCurLatitude(double curLatitude) {
        this.curLatitude = curLatitude;
    }

    public double getCurLongitude() {
        return curLongitude;
    }

    public void setCurLongitude(double curLongitude) {
        this.curLongitude = curLongitude;
    }
}
