package com.example.lenovo.maandroid.Mine;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lenovo.maandroid.Login.LoginActivity;
import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Utils.Data;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Set extends AppCompatActivity {
    private ImageButton return5;
    private ImageButton gongneng;
    private ImageButton xieyi;
    private ImageButton banben;
    private ImageButton help;
    private TextView tuichu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.set);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8 );
        }

        findView();
        //初始化界面
        setAdapter();


    }

    private void setAdapter() {
        MyListener1 myListener = new MyListener1();
        return5.setOnClickListener( myListener);
        gongneng.setOnClickListener( myListener );
        xieyi.setOnClickListener( myListener);
        banben.setOnClickListener( myListener );
        help.setOnClickListener( myListener );
tuichu.setOnClickListener(myListener);
    }

    private void findView() {
        return5=findViewById( R.id.return0);
        gongneng=findViewById( R.id.gongneng );
        xieyi=findViewById( R.id.xieyi );
        banben=findViewById( R.id.banben);
        help=findViewById( R.id.help );
        tuichu=findViewById( R.id.tuichu);

    }


    private class MyListener1 implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.return0:
                    finish();
                    break;
                case R.id.gongneng:
                    Intent intent=new Intent(Set.this,MineSetgongneng.class);
                    startActivity( intent);
                    break;
                case R.id.xieyi:
                    Intent intent1=new Intent( Set.this,MineSetxieyi.class);
                    startActivity( intent1);
                    break;
                case R.id.banben:
                    Toast.makeText( Set.this,"当前版本1.0",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.help:
                    Intent intent2=new Intent( Set.this,MineSethelp.class);
                    startActivity( intent2);
                    break;
                case R.id.tuichu:
                    //清空SharedPreferences
                    Log.e("aaaaaaa","tuichu");
                 SharedPreferences   preferences = getSharedPreferences("parent",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.commit();
                    //结束本Fragment，Intent跳转到登录Activity
                    //finish();
                    Intent intent3 = new Intent(Set.this, LoginActivity.class);
                    intent3.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK );
                    startActivity(intent3);
                    break;
            }
        }
    }
}
