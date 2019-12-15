package entity;

import java.io.Serializable;

public class Parent implements Serializable {
    private int id;
    private String phoneNumber;
    private String nickName;
    private String password;
    private String headerPath;

    public Parent() {
    }

    public Parent(int id, String phoneNumber, String nickName, String password, String headerPath) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.nickName = nickName;
        this.password = password;
        this.headerPath = headerPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeaderPath() {
        return headerPath;
    }

    public void setHeaderPath(String headerPath) {
        this.headerPath = headerPath;
    }

    @Override
    public String toString() {
        return "Parent{" +
                "phoneNumber=" + phoneNumber +
                ", nickName='" + nickName + '\'' +
                ", password='" + password + '\'' +
                ", headerPath='" + headerPath + '\'' +
                '}';
    }
}
