package com.example.lenovo.maandroid.Monitor;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.example.lenovo.maandroid.Entity.Child;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MonitorFragment extends Fragment {
    private List<Child> childList = null;
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
    private SharedPreferences sharedPreferences;
    private int parentId;
    private int tag=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fagment_monitor, container, false);
        pc=new PieChart(view.getContext());
        sharedPreferences=getContext().getSharedPreferences( "parent", Context.MODE_PRIVATE );
        //parentId=sharedPreferences.getInt( "parentId",0 );
        parentId=1;
        //设置别名
        setAlias(parentId+"");

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
                switch (msg.what){
                    case 1:
                        String info = (String) msg.obj;
                        if(info.equals("no")){

                        }else {
                            Log.e("childInfo", info);
                            childList = Analysis(info);
                            //if(tag==1){
                            customAdapterMonitor = new CustomAdapterMonitor(getContext(), childList, R.layout.listview_monitor_item, appInfos, llmp, mapView, pc);
                            monitorlistView.setAdapter(customAdapterMonitor);
                            break;
                            // }
                        }
                }
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
        Log.e("tt",str);
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
        FormBody formBody=new FormBody.Builder().add("parentId",parentId+"").build();
        //2、创建Request对象
        Request request = new Request.Builder() //创建Builder对象
                .url(Constant.BASE_IP + "monitor/child")//设置网络请求的UrL地址
                .post(formBody)
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
                wrapperMessage(json,1);
            }
        });
    }

    //解析json
    public static List<Child> Analysis(String jsonStr) {
        JSONArray jsonArray = null;
        List<Child> list = new ArrayList<>();
        try {
            jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Child child=new Child();
                child.setName(jsonObject.getString("name"));
                //因为数据库中的数据是孩子的出生日期，不是年龄，只是年龄无法更新
                // 获取当前年月日期，设置孩子的年龄，后期不仅需要从年上判断，也需要将月份也算进去
                String strAge=jsonObject.getString("age");
                //int child_age=Integer.parseInt(jsonObject.getString("age").substring( 0,4));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-mm-dd" );
                Date curdate = new Date( System.currentTimeMillis() );
                //int t = Integer.parseInt( simpleDateFormat.format(strAge));
                Date childdate =new Date(simpleDateFormat.parse(strAge).toString());
                child.setAge(curdate.getYear()-childdate.getYear());//年龄：简单的判断
                child.setHeaderPath(jsonObject.getString("headerPath"));
                child.setId(Integer.parseInt(jsonObject.getString("id")));
                child.setParentId(Integer.parseInt(jsonObject.getString("parentId")));
                list.add(child);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    //向主线程传输数据
    private void wrapperMessage(String info,int what) {
        Message msg = Message.obtain();
        msg.obj = info;
        msg.what=what;
        mainHandler.sendMessage(msg);
    }

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void setAlias(String DEVICE_ID) {
        if (TextUtils.isEmpty(DEVICE_ID)) {
            return;
        }
        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, DEVICE_ID));
    }

    /**
     * /**
     * TagAliasCallback类是JPush开发包jar中的类，用于
     * 设置别名和标签的回调接口，成功与否都会回调该方法
     * 同时给定回调的代码。如果code=0,说明别名设置成功。
     * /**
     * 6001   无效的设置，tag/alias 不应参数都为 null
     * 6002   设置超时    建议重试
     * 6003   alias 字符串不合法    有效的别名、标签组成：字母（区分大小写）、数字、下划线、汉字。
     * 6004   alias超长。最多 40个字节    中文 UTF-8 是 3 个字节
     * 6005   某一个 tag 字符串不合法  有效的别名、标签组成：字母（区分大小写）、数字、下划线、汉字。
     * 6006   某一个 tag 超长。一个 tag 最多 40个字节  中文 UTF-8 是 3 个字节
     * 6007   tags 数量超出限制。最多 100个 这是一台设备的限制。一个应用全局的标签数量无限制。
     * 6008   tag/alias 超出总长度限制。总长度最多 1K 字节
     * 6011   10s内设置tag或alias大于3次 短时间内操作过于频繁
     **/
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            Log.e("code",code+"");
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.e("logs", logs);
                    tag=1;
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.e("logs", logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("logs", logs);
            }
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.e("logs", "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getActivity().getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;
                default:
                    Log.e("logs", "Unhandled msg - " + msg.what);
            }
        }
    };





}






