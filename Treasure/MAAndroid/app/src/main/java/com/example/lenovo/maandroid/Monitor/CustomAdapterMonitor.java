package com.example.lenovo.maandroid.Monitor;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.MapView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lenovo.maandroid.Entity.AppInfo;
import com.example.lenovo.maandroid.Entity.Child;
import com.example.lenovo.maandroid.Login.LoginActivity;
import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Utils.Constant;
import com.example.lenovo.maandroid.Utils.PieChart;

import java.io.Console;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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

import static android.content.Context.MODE_PRIVATE;

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
    private RequestOptions options;
    private int tag=0;

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
        final Child child=monitorList.get(position);
        tvName.setText(child.getName().toString());
        tvAge.setText(child.getAge()+"");
        options = new RequestOptions().circleCrop().placeholder( R.drawable.aaa ).error( R.drawable.aaa ).fallback( R.drawable.aaa );
        try {
            Log.e("头像路径",Constant.BASE_IP+child.getHeaderPath());
            URL url=new URL(Constant.BASE_IP+child.getHeaderPath());
            Glide.with(context)
                    .load(url)
                    .apply( options )
                    .into(headPath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //监控
        llmonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(child.getIsResign()==0){
                    Toast.makeText(context,"请绑定孩子",Toast.LENGTH_LONG).show();
                }else{
                        getAppInfo(child.getId() + "", child.getDeviceId());
                        llmp.setVisibility(View.VISIBLE);
                        if (tag == 0) {
                            llmp.addView(pc);
                            tag = 1;
                        }
                        mapView.setVisibility(View.GONE);
                        mainHandler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                switch (msg.what) {
                                    case 1:
                                        String info = (String) msg.obj;
                                        Log.e("childInfo", info);
                                        break;
                                }
                            }
                        };
                    }
            }
        });
        //定位
        llposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(child.getIsResign()==0){
                    Toast.makeText(context,"请绑定孩子",Toast.LENGTH_LONG).show();
                }else {
                        getPositionInfo(child.getId()+"",child.getDeviceId());
                        llmp.setVisibility(View.VISIBLE);
                        if(tag==1){
                            llmp.removeView(pc);
                            tag=0;
                        }
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
            }
        });
        return convertView;
    }

    public void getPositionInfo(String id,String deviceId) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Log.e("调用1：", Constant.BASE_IP + "toChild/position");

        FormBody formBody=new FormBody.Builder().add("childId",id).add("deviceId",deviceId).build();
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

    public void getAppInfo(String id,String deviceId) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Log.e("调用1：", Constant.BASE_IP + "toChild/appInfo");

        FormBody formBody=new FormBody.Builder().add("childId",id).add("deviceId",deviceId).build();
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
    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    public void setAlias(String DEVICE_ID) {
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
            String logs;
            Log.e("code*", code + "");
            switch (code) {
                case 0:
                    logs = "Set tag and alias success*";
                    Log.e("logs", logs);
                   // tag=1;
                    SharedPreferences sharedPreferences = context.getSharedPreferences("parent", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("isResign*", "1");
                    Log.e("tt","1");
                    editor.commit();
                   /* Message msg = new Message();
                    msg.what = 1;
                    mainHandle.sendMessage(msg);
*/
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.*";
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
                    Log.e("logs", "Set alias in handler.*");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(context, (String) msg.obj, null, mAliasCallback);
                    break;
                default:
                    Log.e("logs", "Unhandled msg - " + msg.what+"*");
            }
        }
    };
}

