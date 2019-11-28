package com.example.lenovo.maandroid.Community;

import java.sql.Timestamp;

public class Comment {
    private int id;
    private int postId;
    private int commentatorId; //评论人ID
    private int responderId;   //回复人ID
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

    public int getCommentatorId() {
        return commentatorId;
    }

    public void setCommentatorId(int commentatorId) {
        this.commentatorId = commentatorId;
    }

    public int getResponderId() {
        return responderId;
    }

    public void setResponderId(int responderId) {
        this.responderId = responderId;
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
                ", commentatorId=" + commentatorId +
                ", responderId=" + responderId +
                ", resComId=" + resComId +
                ", time=" + time +
                ", content='" + content + '\'' +
                '}';
    }
}
