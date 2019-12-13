package entity;

public class Child {
private String imgPath;
	private String nickName;
	private int birthday;
	private int parentId;
	private int id;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getBirthday() {
		return birthday;
	}
	public void setBirthday(int birthday) {
		this.birthday = birthday;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public Child(String imgPath, String nickName, int birthday, int parentId, int id) {
		super();
		this.imgPath = imgPath;
		this.nickName = nickName;
		this.birthday = birthday;
		this.parentId = parentId;
		this.id = id;
	}
	
	

}
