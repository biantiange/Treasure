package com.example.lenovo.maandroid.Entity;

public class Discuss {
    private  int id;
    private  String postId;
    private  String commentatorId;
    private  String responderId;
    private  String resComId;
    private  String time;
    private  String content;

    public Discuss(int id, String postId, String commentatorId, String responderId, String resComId, String time, String content) {
        this.id = id;
        this.postId = postId;
        this.commentatorId = commentatorId;
        this.responderId = responderId;
        this.resComId = resComId;
        this.time = time;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCommentatorId() {
        return commentatorId;
    }

    public void setCommentatorId(String commentatorId) {
        this.commentatorId = commentatorId;
    }

    public String getResponderId() {
        return responderId;
    }

    public void setResponderId(String responderId) {
        this.responderId = responderId;
    }

    public String getResComId() {
        return resComId;
    }

    public void setResComId(String resComId) {
        this.resComId = resComId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
