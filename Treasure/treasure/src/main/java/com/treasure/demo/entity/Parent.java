package com.treasure.demo.entity;

/**
 * @ClassName Parent
 * @Description
 * @Author SkySong
 * @Date 2020-04-14 17:36
 */
public class Parent {
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Parent{" +
                "phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
