package com.example.lenovo.maandroid.Community;

import java.io.Serializable;
import java.sql.Timestamp;

public class Post implements Serializable {
    private int id;         //帖子ID
    private String content; //内容
    private Timestamp time; //发布时间
    private int praiseCount;//点赞数
    private Parent parent;  //发帖人

    public Post() {
    }

    public Post(int id, String content, Timestamp time, int praiseCount, Parent parent) {
        this.id = id;
        this.content = content;
        this.time = time;
        this.praiseCount = praiseCount;
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", praiseCount=" + praiseCount +
                ", parent=" + parent +
                '}';
    }
}
