package com.example.lenovo.maandroid.Entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class PostImg implements Serializable {
    private int id;
    private String path;
    private int postId;
    private Timestamp time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "PostImg{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", postId=" + postId +
                ", time=" + time +
                '}';
    }
}
