package cn.edu.hebtu.software.childrendemo;

public class Child {
    private int id;
    private String name;
    private int age;
    private String headerPath;
    private int parentId;
    private int isResign;
    private String deviceId;
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
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

    @Override
    public String toString() {
        return "Child{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", headerPath='" + headerPath + '\'' +
                ", parentId=" + parentId +
                ", isResign=" + isResign +
                '}';
    }
}
