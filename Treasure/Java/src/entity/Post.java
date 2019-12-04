package entity;

import com.sun.jmx.snmp.Timestamp;

public class Post {
	private int id;
	private String content;
	private Timestamp time;
	private int praiseCount;
	private int posterId;
	
	
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
	public int getPosterId() {
		return posterId;
	}
	public void setPosterId(int posterId) {
		this.posterId = posterId;
	}
	@Override
	public String toString() {
		return "Post [id=" + id + ", content=" + content + ", time=" + time + ", praiseCount=" + praiseCount
				+ ", posterId=" + posterId + "]";
	}

}
