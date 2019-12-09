package com.example.lenovo.maandroid.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.maandroid.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
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
    private Button btnRegist;
    private Button btnForget;
    //private ImageView ivLogo;
    private TextView textView;
    //控件值
    private String userPhone;
    private String userPwd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_login1);
        findViews();
        //okHttpClient
        okHttpClient = new OkHttpClient();
        //实现文字左右摇晃效果
        //摇摆
        TranslateAnimation alphaAnimation2 = new TranslateAnimation(20f, 100f, 50, 50);
        alphaAnimation2.setDuration(1000);
        alphaAnimation2.setRepeatCount(Animation.INFINITE);
        alphaAnimation2.setRepeatMode(Animation.REVERSE);
        textView.setAnimation(alphaAnimation2);
        alphaAnimation2.start();
    }


    private void findViews() {
        //控件
        etUserPhone = findViewById(R.id.et_userPhone);
        etUserPwd = findViewById(R.id.et_userPassword);
        btnLogin = findViewById(R.id.btn_login);
        btnRegist = findViewById(R.id.btn_regist);
        btnForget = findViewById(R.id.btn_forgetPwd);
        //ivLogo = findViewById(R.id.logo);
        //RequestOptions options = new RequestOptions().circleCrop();
        //Glide.with(this).load(getResources().getDrawable(R.drawable.logo)).apply(options).into(ivLogo);
        textView = findViewById(R.id.textView);

        //给按钮设置监听器
        MyListener myListener =  new MyListener();
        btnLogin.setOnClickListener(myListener);
        btnRegist.setOnClickListener(myListener);
        btnForget.setOnClickListener(myListener);
    }

    public class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_login:
                    //发给数据库验证用户是否存在
                    //值
                    userPhone = etUserPhone.getText().toString();
                    userPwd = etUserPwd.getText().toString();
                    Log.e("LoginActivity","手机号："+userPhone+"密码："+userPwd);
                    MyOkHttp(Constant.BASE_IP+"Java/LoginServlet/"+userPhone);
                    break;
                case R.id.btn_regist:
                    //跳转到注册界面
                    Intent intent =new Intent(LoginActivity.this,RegisterActivity.class);
                    intent.putExtra("flag",1);
                    startActivity(intent);
                    break;
                case R.id.btn_forgetPwd:
                    Intent intent1 =new Intent(LoginActivity.this,RegisterActivity.class);
                    intent1.putExtra("flag",2);
                    startActivity(intent1);
                    break;
            }
        }
    }
    public void MyOkHttp(String url){
        Request request = new Request.Builder().url(url).build();
        final Call call  = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                //Toast.makeText(LoginActivity.this,"用户名或密码错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                Log.e("LoginActivity","响应："+jsonStr);
                if(!jsonStr.equals("-1")){
                    Looper.prepare();
                    Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity","要开始存储了");
                    sharedPreferences = getSharedPreferences("parent",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("phoneNumber",userPhone);
                    editor.putInt("parentId",Integer.parseInt(jsonStr));
                    editor.commit();   //提交
                    Log.e("LoginActivity","存储结束了");
                    Looper.loop();

                   /* //从SharedPreferences中取时
                    sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
                    String phoneNumber = sharedPreferences.getString("phoneNumber","");*/

                }else{
                    Looper.prepare();
                    Toast.makeText(LoginActivity.this,"登录失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
    }
}
