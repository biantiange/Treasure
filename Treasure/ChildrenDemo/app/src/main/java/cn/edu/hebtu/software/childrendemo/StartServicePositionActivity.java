package cn.edu.hebtu.software.childrendemo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;

public class StartServicePositionActivity extends AppCompatActivity implements DataPicker{
    private LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private List<PositionInfo> positionInfos=new ArrayList<>();
    private boolean tag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_service_position);
        Log.e("StartServicePosition","启动");
        Intent intent1=getIntent();
        String childId=intent1.getStringExtra("childId");
        Intent intent = new Intent(StartServicePositionActivity.this,MyPositionIntentService.class);
        intent.putExtra("ci",childId);
        MyPositionIntentService.setDataPicker(StartServicePositionActivity.this);
        startService(intent);
        finish();
    }

    @Override
    public List<AppInfo> getAppInfo() {
        return null;
    }

    @Override
    public List<PositionInfo> getPostionInfo() {
        getLocation();
        int i=0;
        while(true){
            i++;
            if(tag==true){
                break;
            }
        }
        return positionInfos;
    }

    public void getLocation(){
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式，默认高精度
        option.setIsNeedAddress(true);  //设置定位结果中需要字符串地址     //可选，是否需要地址信息，默认为不需要，即参数为false       如果开发者需要获得当前点的地址信息，此处必须为true
        option.setIsNeedLocationPoiList(true);  //设置定位结果中需要Poi
        option.setCoorType("bd09ll");//设置返回经纬度坐标类型，默认GCJ02
        mLocationClient.start();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            PositionInfo positionInfo=new PositionInfo(latitude,longitude);
            positionInfos.add(positionInfo);
            tag=true;
            /*Message message=new Message();
            message.what=1;
            mainHandler.sendMessage(message);*/
        }
    }
}
