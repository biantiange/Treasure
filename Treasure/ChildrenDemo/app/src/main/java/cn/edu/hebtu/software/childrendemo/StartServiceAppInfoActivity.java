package cn.edu.hebtu.software.childrendemo;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class StartServiceAppInfoActivity extends AppCompatActivity implements DataPicker {
    private List<AppInfo> appList = new ArrayList<>();
    private List<AppInfo> newList=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startservice);
        Log.e("StartServiceActivity", "启动");
        Intent intent1=getIntent();
        String childId=intent1.getStringExtra("childId");
        //启动IntentService
        Intent intent = new Intent(StartServiceAppInfoActivity.this,MyAppInfoIntentService.class);
        intent.putExtra("ci",childId);
        Log.e("childId",childId);
        MyAppInfoIntentService.setDataPicker(StartServiceAppInfoActivity.this);
        startService(intent);
        //Log.d("mainactivity》》","启动服务"+startService(intent));
        finish();
    }

    @Override
    public List<AppInfo> getAppInfo() {
        Log.e("getAppInfo", "调用");
        /*startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));*/
        Calendar beginCal = Calendar.getInstance();
        beginCal.add(Calendar.HOUR_OF_DAY, -1);
        Calendar endCal = Calendar.getInstance();
        List<UsageStats> stats = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager manager = (UsageStatsManager) getApplicationContext().getSystemService(USAGE_STATS_SERVICE);
            stats = manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginCal.getTimeInMillis(), endCal.getTimeInMillis());
        }
        StringBuilder sb = new StringBuilder();
        for (UsageStats us : stats) {
            try {
                PackageManager pm = getApplicationContext().getPackageManager();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ApplicationInfo applicationInfo = pm.getApplicationInfo(us.getPackageName(), PackageManager.GET_META_DATA);
                    if ((applicationInfo.flags & applicationInfo.FLAG_SYSTEM) <= 0) {
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
                        format1.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
                        String lastTimeUsed = format.format(new Date(us.getLastTimeUsed()));
                        long totalTimeInForeground = us.getTotalTimeInForeground();
                        if (totalTimeInForeground > 0) {
                            AppInfo appInfo = new AppInfo(pm.getApplicationLabel(applicationInfo).toString(), totalTimeInForeground, lastTimeUsed, pm.getApplicationIcon(applicationInfo));
                            appList.add(appInfo);
                            //Log.e("tt",pm.getApplicationLabel(applicationInfo) + "\t" + lastTimeUsed+ "\t" +format1.format(new Date(totalTimeInForeground))+ "\t"+pm.getApplicationIcon(applicationInfo)+"\n");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Collections.sort(appList, new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo o1, AppInfo o2) {
                return o1.getUseTime() == o2.getUseTime() ? 0 : (o1.getUseTime() < o2.getUseTime() ? 1 : -1);
            }
        });
        if (appList.size() > 3) {
            newList = appList.subList(0, 3);
            Log.e("newList",newList.size()+"");
            return newList;
        } else {
            Log.e("appList",appList.size()+"");
            return appList;
        }
    }

    @Override
    public List<PositionInfo> getPostionInfo() {
        return null;
    }


}
