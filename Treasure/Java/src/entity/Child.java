package entity;

public class Child {
	private int id;
	private String name;
	private String age;
	private String headerPath;
	private int parentId;
 	private int isResign;
 	private String deviceId;
	public String getHeaderPath() {
		return headerPath;
	}
	public void setHeaderPath(String headerPath) {
		this.headerPath = headerPath;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
    public int getIsResign() {
        return isResign;
    }

    public void setIsResign(int isResign) {
        this.isResign = isResign;
    }
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
    

}
