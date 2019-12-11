package com.example.lenovo.maandroid.Community;

import java.io.Serializable;
import java.sql.Timestamp;

public class Comment implements Serializable {
    private int id;
    private int postId;
    private Parent commentator; //评论人
    private Parent responder;   //回复人
    private Comment resCom;      //回复评论ID
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

    public Comment getResCom() {
        return resCom;
    }

    public void setResCom(Comment resCom) {
        this.resCom = resCom;
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
                ", resCom=" + resCom +
                ", time=" + time +
                ", content='" + content + '\'' +
                '}';
    }
}
