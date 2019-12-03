package com.example.lenovo.maandroid.Community;

import java.sql.Timestamp;

public class Comment {
    private int id;
    private int postId;
    private Parent commentator; //评论人
    private Parent responder;   //回复人
    private int resComId;      //回复评论ID
    private Timestamp time;    //时间
    private String content;    //内容

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public Parent getCommentator() {
        return commentator;
    }

    public void setCommentator(Parent commentator) {
        this.commentator = commentator;
    }

    public Parent getResponder() {
        return responder;
    }

    public void setResponder(Parent responder) {
        this.responder = responder;
    }

    public int getResComId() {
        return resComId;
    }

    public void setResComId(int resComId) {
        this.resComId = resComId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", postId=" + postId +
                ", commentator=" + commentator +
                ", responder=" + responder +
                ", resComId=" + resComId +
                ", time=" + time +
                ", content='" + content + '\'' +
                '}';
    }
}
