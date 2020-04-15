package com.example.lenovo.maandroid.Login;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.maandroid.Entity.Parent;
import com.example.lenovo.maandroid.Host.MainActivity;
import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Utils.Constant;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private OkHttpClient okHttpClient;
    private SharedPreferences sharedPreferences;
    //控件
    private EditText etUserPhone;
    private EditText etUserPwd;
    private Button btnLook;
    private Button btnLogin;
    private FloatingActionButton btnRegist;
    private Button btnForget;
    //private ImageView ivLogo;
    private TextView textView;
    private LinearLayout llLogin;
    private LinearLayout llWait;
    //控件值
    private String userPhone;
    private String userPwd;
    private Handler mainHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.e("tt2" ,"1");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    break;
                case 2:
                    Log.e("tt3" ,"设置别名");
                    llLogin.setVisibility(View.GONE);
                    llWait.setVisibility(View.VISIBLE);
                    Parent parent = new Gson().fromJson(msg.obj.toString(), Parent.class);
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", "要开始存储了");
                    sharedPreferences = getSharedPreferences("parent", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("phoneNumber", parent.getPhoneNumber());
                    editor.putInt("parentId", parent.getId());
                    editor.putString("headerPath", parent.getHeaderPath());
                    editor.putString("nickName", parent.getNickName());
                    editor.putString("deviceId",DEVICE_ID);
                    editor.putBoolean(Constant.AUTO_LOGIN_KEY , true);
                    editor.commit();   //提交
                    Log.e("LoginActivity", "存储结束了" + parent.toString() + parent.getId());
                    editDeviceId(DEVICE_ID,parent.getId()+"");
                    break;
                case 3:
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_SET_ALIAS:
                    Log.e("logs", "Set alias in handler.*");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;
                default:
                    Log.e("logs", "Unhandled msg - " + msg.what+"*");
            }
        }
    };
    private String DEVICE_ID=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        /*DEVICE_ID = tm.getDeviceId();
        Log.e("DEVICE_ID",DEVICE_ID);*/
        //setContentView( R.layout.activity_login1);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xffffcc00);
        }
        findViews();
        //okHttpClient
        okHttpClient = new OkHttpClient();
        ImageView progress_img = (ImageView) findViewById(R.id.iv_bg);
        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.anim_upload_progress);
        //LinearInterpolator lin = new LinearInterpolator();
        // operatingAnim.setInterpolator(lin);
        progress_img.setAnimation(operatingAnim);

       // 2. 判断sharedP中是否已经有登录用户
        sharedPreferences = getSharedPreferences("parent", MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Constant.AUTO_LOGIN_KEY, Constant.DEFAULT_LOGIN_KEY)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
           /* if (sharedPreferences.getString("deviceId", null).toString().equals(DEVICE_ID)) {
                // 此时不为第一次登陆或退出登录情况，自动登陆
               *//**//**//**//* Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
               setAlias(DEVICE_ID);
            }else{
                editDeviceId(DEVICE_ID,sharedPreferences.getInt("parentId",-1)+"");
                setAlias(DEVICE_ID);
            }*/
        }

    }


    private void findViews() {
        //控件
        etUserPhone = findViewById(R.id.et_userPhone);
        etUserPwd = findViewById(R.id.et_userPassword);
        btnLogin = findViewById(R.id.btn_login);
        //btnRegist = findViewById(R.id.btn_regist);
        btnRegist = findViewById(R.id.fab);  //注册
        btnForget = findViewById(R.id.btn_forgetPwd);
        //ivLogo = findViewById(R.id.logo);
        //RequestOptions options = new RequestOptions().circleCrop();
        //Glide.with(this).load(getResources().getDrawable(R.drawable.logo)).apply(options).into(ivLogo);
        //textView = findViewById(R.id.textView);
        llLogin=findViewById(R.id.ll_login);
        llWait=findViewById(R.id.ll_wait);

        //给按钮设置监听器
        MyListener myListener = new MyListener();
        btnLogin.setOnClickListener(myListener);
        btnRegist.setOnClickListener(myListener);
        btnForget.setOnClickListener(myListener);

    }

    public class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    //发给数据库验证用户是否存在
                    //值
                    userPhone = etUserPhone.getText().toString();
                    userPwd = etUserPwd.getText().toString();
                    Log.e("LoginActivity", "手机号：" + userPhone + "密码：" + userPwd);
                    if (userPhone == null || userPhone.equals("") || userPwd == null || userPwd.equals("")) {
                        Toast.makeText(LoginActivity.this, "请按要求输入哦", Toast.LENGTH_SHORT).show();
                    } else {
                        MyOkHttp(Constant.BASE_IP + "LoginServlet?phoneNumber=" + userPhone + "&&password=" + userPwd);
                    }
                    break;
                //case R.id.btn_regist:
                case R.id.fab:
                    //跳转到注册界面
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    intent.putExtra("flag", 1);
                    startActivity(intent);
                    break;
                case R.id.btn_forgetPwd:
                    Intent intent1 = new Intent(LoginActivity.this, RegisterActivity.class);
                    intent1.putExtra("flag", 2);
                    startActivity(intent1);
                    break;
            }
        }
    }

    public void MyOkHttp(String url) {
        final Request request = new Request.Builder().url(url).build();
        final Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                //登录失败
                Looper.prepare();
                Toast.makeText(LoginActivity.this, "因网络错误登录失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //登录成功，返回的是parent的全部信息
                String jsonStr = response.body().string();
                Log.e("LoginActivity", "响应：" + jsonStr);
                //Log.e("aa",(jsonStr== JSONObject.NULL)+"");
                if (jsonStr != null && !jsonStr.equals("")) {
                    Message message=new Message();
                    message.what=2;
                    message.obj=jsonStr;
                    mainHandle.sendMessage(message);
                    //设置别名
                    setAlias(DEVICE_ID);
                    //跳转到MainActivity
                    //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                  //  Looper.loop();

                   /* //从SharedPreferences中取时
                    sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
                    String phoneNumber = sharedPreferences.getString("phoneNumber","");*/

                } else {
                    /*Looper.prepare();
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    Looper.loop();*/
                    Message message1 = new Message();
                    message1.what=3;
                    mainHandle.sendMessage(message1);
                }
            }
        });
    }

    public void editDeviceId(String deviceId,String parentId){
        OkHttpClient okHttpClient = new OkHttpClient();
        Log.e("test2", Constant.BASE_IP + "editDeviceId/parent");
        //2、创建Request对象
        FormBody formBody =new FormBody.Builder().add("deviceId",deviceId).add("parentId",parentId).build();;
        Request request = new Request.Builder() //创建Builder对象
                .url(Constant.BASE_IP + "editDeviceId/parent")//设置网络请求的UrL地址
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
            }
        });
    }

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    public void setAlias(String DEVICE_ID) {
        if (TextUtils.isEmpty(DEVICE_ID)) {
            return;
        }
        // 调用 Handler 来异步设置别名
        mainHandle.sendMessage(mainHandle.obtainMessage(MSG_SET_ALIAS, DEVICE_ID));
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
                    //tag=1;
                    sharedPreferences = getSharedPreferences("parent", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("isResign*", "1");
                    Log.e("tt","1");
                    editor.commit();
                    Message msg = new Message();
                    msg.what = 1;
                    mainHandle.sendMessage(msg);

                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.*";
                    Log.e("logs", logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mainHandle.sendMessageDelayed(mainHandle.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("logs", logs);
            }
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
   /* private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.e("logs", "Set alias in handler.*");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;
                default:
                    Log.e("logs", "Unhandled msg - " + msg.what+"*");
            }
        }
    };*/

}
