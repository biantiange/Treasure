package cn.edu.hebtu.software.childrendemo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

import java.io.IOException;
import java.util.ArrayList;
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


public class MainActivity extends AppCompatActivity {
    private EditText etCname;
    private EditText etPname;
    private EditText etPphone;
    private Button btnOk;
    private String cname;
    private String pname;
    private String pphone;
    private OkHttpClient okHttpClient;
    private Handler mainHandler;
    private TextView tvWait;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        findView();
        okHttpClient = new OkHttpClient();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cname = etCname.getText().toString();
                pname = etPname.getText().toString();
                pphone = etPphone.getText().toString();
                Log.e("tt",cname+":"+pname+":"+pphone);
                if (cname != null && !cname.equals("") && pname != null && !pname.equals("") && pphone != null && !pphone.equals("")) {
                    tvWait.setVisibility(View.VISIBLE);
                    tvWait.setText("正在绑定请耐心等待...");
                    //绑定孩子与父母的设备
                    findChild(pphone,cname);
                }
            }
        });
        //主线程
        mainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        String info = (String) msg.obj;
                        if(info.equals("no")){
                            tvWait.setText("孩子未注册，绑定失败");

                        }else{
                            Log.e("childId", info);
                            setAlias(info);
                        }
                        break;
                }


            }
        };
    }

    private void findView() {
        etCname = findViewById(R.id.et_cname);
        etPname = findViewById(R.id.et_pname);
        etPphone = findViewById(R.id.et_pphone);
        btnOk = findViewById(R.id.btn_ok);
        tvWait=findViewById(R.id.tvWait);
    }

    //初始化孩子数据
    public void findChild(String phoneNumber,String childId) {
        Log.e("test", Constant.BASE_URL + "find/child");
        FormBody formBody=new FormBody.Builder().add("phoneNumber",phoneNumber).add("childName",childId).build();
        //2、创建Request对象
        Request request = new Request.Builder() //创建Builder对象
                .url(Constant.BASE_URL + "find/child")//设置网络请求的UrL地址
                .post(formBody)
                .build();
        //3、创建Call对象
        final Call call = okHttpClient.newCall(request);
        //4、发送请求
        call.enqueue(new Callback() {
            //请求失败之后回调
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("tt","11");
                e.printStackTrace();
            }

            //请求成功之后回调
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("tt","22");
                String json = response.body().string();
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
                    btnOk.setEnabled(false);
                    tvWait.setVisibility(View.GONE);
                    Intent intent=new Intent(MainActivity.this,SuccessActivity.class);
                    startActivity(intent);
                    //Toast.makeText(MainActivity.this,"绑定成功",Toast.LENGTH_LONG).show();
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
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;
                default:
                    Log.e("logs", "Unhandled msg - " + msg.what);
            }
        }
    };


}
