package com.example.lenovo.maandroid.Monitor;

import android.Manifest;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.lenovo.maandroid.Entity.AppInfo;
import com.example.lenovo.maandroid.Entity.PositionInfo;
import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Utils.Constant;
import com.example.lenovo.maandroid.Utils.PieChart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MonitorFragment extends Fragment {
    private List<Map<String, Object>> childList = null;
    private CustomAdapterMonitor customAdapterMonitor;
    private ListView monitorlistView = null;
    private Handler mainHandler;
    private OkHttpClient okHttpClient;
    private List<AppInfo> appInfos = new ArrayList<>();
    private List<PositionInfo> positionInfos = new ArrayList<>();
    private LinearLayout llmp;
    private PieChart pc;
    private MapView mapView = null;
    private BaiduMap baiduMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fagment_monitor, container, false);
        pc=new PieChart(view.getContext());
        EventBus.getDefault().register(this);
        monitorlistView = view.findViewById(R.id.lv_monitor);
        //百度地图定位
        mapView = view.findViewById(R.id.map_view);
        baiduMap = mapView.getMap();
        // 不显示百度LOGO
        View child = mapView.getChildAt(1);
        if ((child != null) && (child instanceof ImageView)) {
            child.setVisibility(View.GONE);
        }

        llmp = view.findViewById(R.id.ll_mp);
        //pc = view.findViewById(R.id.pc);
        //1、构造方法创建OkHttpClient对象————属性都为默认值
        okHttpClient = new OkHttpClient();
        //初始化孩子信息
        initChild();
        //主线程
        mainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String info = (String) msg.obj;
                Log.e("childInfo", info);
                childList = Analysis(info);
                customAdapterMonitor = new CustomAdapterMonitor(getContext(), childList, R.layout.listview_monitor_item, appInfos, llmp, mapView, pc);
                monitorlistView.setAdapter(customAdapterMonitor);
            }
        };
        return view;
    }

    //当接受到通知时调用
    //sticky=true:该方法可以接受粘性事件
    @Subscribe(sticky = true)
    public void onMessageReceived(String event) {
        Log.e("MonitorFragment", "接收到信息" + event);
        List<Map<String, Object>> list = new Gson().fromJson(event, new TypeToken<List<Map<String, Object>>>() {}.getType());
        String str=list.get(0).get("tag").toString();
        Map<String, Object> map = new HashMap<String, Object>();
        map = new Gson().fromJson(str, map.getClass());
        String tag1=null;
        Iterator<String> iter = map.keySet().iterator();
        while(iter.hasNext()){
            String key=iter.next();
            tag1 = map.get(key).toString();
        }
        //定位孩子
        if (tag1.equals("position")) {
            Log.e("tag", "定位");
            Type listType = new TypeToken<List<PositionInfo>>() {}.getType();
            positionInfos = new Gson().fromJson(list.get(0).get("content").toString(), listType);
            getPosition(positionInfos.get(0).getCurLatitude(), positionInfos.get(0).getCurLatitude());
        }

        //监控APP
        if (tag1.equals("appInfo")) {
            Log.e("tag", "获取APP信息");
            Type listType = new TypeToken<List<AppInfo>>() {}.getType();
            appInfos = new Gson().fromJson(list.get(0).get("content").toString(), listType);
            List<String> mTitlesList = new ArrayList<>();
            List<Double> mValuesList = new ArrayList<>();
            for (int i = 0; i < appInfos.size(); i++) {
                mTitlesList.add(appInfos.get(i).getAppName());
                mValuesList.add(new Double(appInfos.get(i).getUseTime()));
            }
            int length = mValuesList.size();
            double[] mValues = new double[length];
            for (int i = 0; i < length; i++) {
                mValues[i] = (double) mValuesList.get(i);
            }
            String[] mTitles = mTitlesList.toArray(new String[mTitlesList.size()]);
            pc.setmValues(mValues);
            pc.setmTitles(mTitles);
            pc.draw(new Canvas());
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    //标注孩子位置
    public void getPosition(double curLatitude, double curLongitude) {
        //添加标注覆盖物，标识当前的位置
        LatLng point = new LatLng(curLatitude, curLongitude);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);
        MarkerOptions markerOptions = new MarkerOptions().position(point).icon(icon);
        baiduMap.addOverlay(markerOptions);
        //移动地图显示当前位置
        MapStatusUpdate move = MapStatusUpdateFactory.newLatLng(point);
        baiduMap.animateMapStatus(move);
    }

    //初始化孩子数据
    public void initChild() {
        Log.e("test", Constant.BASE_IP + "monitor/child");
        //2、创建Request对象
        Request request = new Request.Builder() //创建Builder对象
                .url(Constant.BASE_IP + "monitor/child")//设置网络请求的UrL地址
                .build();
        //3、创建Call对象
        final Call call = okHttpClient.newCall(request);
        //4、发送请求
        call.enqueue(new Callback() {
            //请求失败之后回调
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            //请求成功之后回调
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                wrapperMessage(json);
            }
        });
    }

    //解析json
    public static List<Map<String, Object>> Analysis(String jsonStr) {
        JSONArray jsonArray = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // 初始化map数组对象
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("id", jsonObject.getString("id"));
                map.put("name", jsonObject.getString("name"));
                map.put("age", jsonObject.getString("age"));
                map.put("parentId", jsonObject.getString("parentId"));
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    //向主线程传输数据
    private void wrapperMessage(String info) {
        Message msg = Message.obtain();
        msg.obj = info;
        mainHandler.sendMessage(msg);
    }
}






