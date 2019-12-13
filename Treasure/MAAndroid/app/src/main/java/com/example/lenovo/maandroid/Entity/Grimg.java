package com.example.lenovo.maandroid.Entity;

public class Grimg {
    private String imgPath;
    private String tag;
    private int growthRecordId;
    private String upTime;

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getGrowthRecordId() {
        return growthRecordId;
    }

    public void setGrowthRecordId(int growthRecordId) {
        this.growthRecordId = growthRecordId;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    @Override
    public String toString() {
        return "Grimg{" +
                "imgPath='" + imgPath + '\'' +
                ", tag='" + tag + '\'' +
                ", growthRecordId=" + growthRecordId +
                ", upTime='" + upTime + '\'' +
                '}';
    }
}
