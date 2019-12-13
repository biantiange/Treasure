package com.example.lenovo.maandroid.Monitor;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.bumptech.glide.Glide;
import com.example.lenovo.maandroid.Entity.AppInfo;
import com.example.lenovo.maandroid.Entity.Child;
import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Utils.Constant;
import com.example.lenovo.maandroid.Utils.PieChart;

import java.io.Console;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CustomAdapterMonitor extends BaseAdapter {
    private Handler mainHandler;
    //原始数据
    private List<Child>  monitorList = null;
    //上下文环境
    private Context context = null;
    //item对应的布局文件
    private int item_layout_id;
    private  List<AppInfo> appInfos;
    private LinearLayout llmp;
    private PieChart pc;
    private MapView mapView;

    //第一：自定义Adapter类———对Adapter构造器进行初始化
    public CustomAdapterMonitor(Context context, List<Child>  dataSourse, int item_layout_id, List<AppInfo> appInfos, LinearLayout llmp, MapView mapView, PieChart pc) {
        this.context = context;
        this.monitorList = dataSourse;
        this.item_layout_id = item_layout_id;
        this.appInfos=appInfos;
        this.llmp=llmp;
        this.mapView=mapView;
        this.pc=pc;
    }

    //第二：返回给listView当前数据的条数
    @Override
    public int getCount() {
        return monitorList.size();
    }

    //第三：根据位置获取原始数据
    @Override
    public Object getItem(int position) {
        return monitorList.get(position);
    }

    //第四：返回item的ID，（实际情况下每个原始数据都具有ID）
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (null == convertView) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(item_layout_id, null);
        }
        ImageView headPath = convertView.findViewById(R.id.iv_img);
        TextView tvName = convertView.findViewById(R.id.tv_name);
        TextView tvAge = convertView.findViewById(R.id.tv_age);
        final LinearLayout llmonitor=convertView.findViewById(R.id.ll_monitor);
        LinearLayout llposition=convertView.findViewById(R.id.ll_position);
        //根据位置从原始数据list获取要显示的数据
        Child child=monitorList.get(position);
        tvName.setText(child.getName().toString());
        tvAge.setText(child.getAge()+"");
        try {
            Log.e("头像路径",Constant.BASE_IP+child.getHeaderPath());
            URL url=new URL(Constant.BASE_IP+child.getHeaderPath());
            Glide.with(context)
                    .load(url)
                    .into(headPath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //监控
        llmonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAppInfo();
                llmp.setVisibility(View.VISIBLE);
                llmp.addView(pc);
                mapView.setVisibility(View.GONE);
                mainHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what){
                            case 1:
                                String info = (String) msg.obj;
                                Log.e("childInfo", info);
                                break;
                        }
                    }
                };
            }
        });
        //定位
        llposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPositionInfo();
                llmp.setVisibility(View.VISIBLE);
                llmp.removeView(pc);
                mapView.setVisibility(View.VISIBLE);
                mainHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what){
                            case 2:
                                String info = (String) msg.obj;
                                Log.e("childInfo", info);
                                break;
                        }
                    }
                };
            }
        });
        return convertView;
    }
    public void getPositionInfo() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Log.e("调用1：", Constant.BASE_IP + "toChild/position");

        FormBody formBody=new FormBody.Builder().add("childId","1").build();
        //2、创建Request对象
        Request request = new Request.Builder() //创建Builder对象
                .url(Constant.BASE_IP + "toChild/position")//设置网络请求的UrL地址
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
                String json=response.body().string();
                wrapperMessage(json,2);
            }
        });
    }

    public void getAppInfo() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Log.e("调用1：", Constant.BASE_IP + "toChild/appInfo");

        FormBody formBody=new FormBody.Builder().add("childId","1").build();
        //2、创建Request对象
        Request request = new Request.Builder() //创建Builder对象
                .url(Constant.BASE_IP + "toChild/appInfo")//设置网络请求的UrL地址
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
                String json=response.body().string();
                wrapperMessage(json,1);
            }
        });
    }

    //向主线程传输数据
    private void wrapperMessage(String info,int what) {
        Message msg = Message.obtain();
        msg.obj = info;
        msg.what=what;
        mainHandler.sendMessage(msg);
    }
}

