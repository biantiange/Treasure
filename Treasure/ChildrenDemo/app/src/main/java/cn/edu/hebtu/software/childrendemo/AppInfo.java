package cn.edu.hebtu.software.childrendemo;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private String appName;
    private long useTime;
    private String lastTime;
    private Drawable applicationIcon;

    public AppInfo(String appName, long useTime, String lastTime, Drawable applicationIcon) {
        this.appName = appName;
        this.useTime = useTime;
        this.lastTime = lastTime;
        this.applicationIcon = applicationIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }
    public Drawable getApplicationIcon() {
        return applicationIcon;
    }

    public void setApplicationIcon(Drawable applicationIcon) {
        this.applicationIcon = applicationIcon;
    }


    @Override
    public String toString() {
        return "AppInfo{" +
                "appName='" + appName + '\'' +
                ", useTime=" + useTime +
                ", lastTime='" + lastTime + '\'' +
                ", applicationIcon=" + applicationIcon +
                '}';
    }
}
