package entity;

import java.sql.Timestamp;

public class Comment {
	private int id;
	private int postId;
	private int commentatorId;
	private int responderId;
	private int resComId;
	private Timestamp timestamp;
	private String content;
	
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
	public String getContent() {
		return content;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "Comment [id=" + id + ", postId=" + postId + ", commentatorId=" + commentatorId + ", responderId="
				+ responderId + ", resComId=" + resComId + ", timestamp=" + timestamp + ", content=" + content + "]";
	}
	
}
