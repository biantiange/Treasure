package entity;

public class GrowthRecord {
    private int id;
    private String upTime;
    private int parentId;
    private String content;
    

    public GrowthRecord() {
	
	}

	public GrowthRecord(String upTime, int parentId, String content) {
		super();
		this.upTime = upTime;
		this.parentId = parentId;
		this.content = content;
	}

	public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getParentId() {
        return parentId;
    }

    
    public int getId() {
        return id;

    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "GrowthRecord{" +
                "id=" + id +
                ", upTime='" + upTime + '\'' +
                ", parentId=" + parentId +
                ", content='" + content + '\'' +
                '}';
    }
}
