package com.example.lenovo.maandroid.Mine;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lenovo.maandroid.R;

import java.io.File;
import java.io.IOException;
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

public class Edit extends AppCompatActivity {
    private ImageButton return1;//返回按钮
    private Button submit0;//修改完成后提交
    private Button selectphoto;//选择头像
    private ImageView user_img;//用户头像
    private EditText username;//用户更改的信息
    private String imgPath;
    private String puser_name;
    private int up = 0;
    private SharedPreferences sharedPreferences;
    private List<Map<String, Object>> user;
    private SharedPreferences usermessage;
    private OkHttpClient okHttpClient;
    private RequestOptions options;
    private String phoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.edit_m );
        sharedPreferences = getSharedPreferences( "parent", MODE_PRIVATE );
        okHttpClient = new OkHttpClient();
        options = new RequestOptions().circleCrop().placeholder( R.drawable.aaa ).error( R.drawable.aaa ).fallback( R.drawable.aaa );
        findView();
        //初始化界面
        init();
        //选择头像
        select();
        //设置监听
        setAdapter();

    }

    private void findView() {
        user_img = findViewById( R.id.edit_user_img );
        username = findViewById( R.id.edit_username );
        selectphoto = findViewById( R.id.edit_btn_img );
        return1 = findViewById( R.id.return0 );
        submit0 = findViewById( R.id.edit_submit );
    }

    private void init() {
        //查看是否已经存入数据如果已有，则使用
        imgPath = sharedPreferences.getString( "imgPath", "aaa.jpg" );
        Log.e( "int.......imgPath", imgPath );
        puser_name = sharedPreferences.getString( "nickName", "" );
        Glide.with( this ).load( Data.url+imgPath ).apply( options ).into( user_img );
        username.setText( puser_name );
        phoneNumber = sharedPreferences.getString( "phoneNumber", "" );
    }

    private void setAdapter() {
        MyListener myListener = new MyListener();
        return1.setOnClickListener( myListener );
        submit0.setOnClickListener( myListener );
    }

    private void select() {
        selectphoto.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions( Edit.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100 );
            }
        } );

    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.return0:
                    finish();
                    break;
                case R.id.edit_submit:
                    //获取更改的信息,提交到数据库
                    final String username0 = username.getText().toString();
                    if (username0.length() > 0 && username0.length() < 13)//数据可以进行更改
                    {
                        //提交到数据库
                        FormBody body = new FormBody.Builder().add( "phoneNumber", phoneNumber ).add( "imgPath", imgPath )
                                .add( "nickname", username0 )
                                .build();
                        Request request = new Request.Builder().url( Data.ip + "EditUserServlet" ).post( body ).build();
                        Call call = okHttpClient.newCall( request );
                        call.enqueue( new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Looper.prepare();
                                Toast.makeText( Edit.this, "网络连接失败。。。", Toast.LENGTH_SHORT ).show();
                                finish();
                                Looper.loop();
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String jsonStr = response.body().string();
                                if (jsonStr != "修改失败") {
                                    Looper.prepare();
                                    sharedPreferences.edit().putString( "imgPath", jsonStr ).putString( "nickName", username0 ).apply();
                                    Intent intent = new Intent( "android.intent.action.CART_BROADCAST" );
                                    intent.putExtra( "data", "refresh" );
                                    LocalBroadcastManager.getInstance( Edit.this ).sendBroadcast( intent );
                                    sendBroadcast( intent );
                                    finish();
                                    Looper.loop();
                                }
                            }
                        } );
                    } else {
                        Toast.makeText( Edit.this, "昵称格式错误,不能为空且小于13个字", Toast.LENGTH_SHORT ).show();
                        finish();
                    }
                    //广播刷新fragment
                    /*Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                    intent.putExtra("data","refresh");
                    LocalBroadcastManager.getInstance(Edit.this).sendBroadcast(intent);
                    sendBroadcast(intent);
                    finish();*/
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            Intent intent = new Intent();
            intent.setAction( Intent.ACTION_PICK );
            intent.setType( "image/*" );
            startActivityForResult( intent, 200 );
        }
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 200 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query( uri, null, null, null, null );
            if (cursor.moveToFirst()) {
                imgPath = cursor.getString( cursor.getColumnIndex( "_data" ) );
                Glide.with( this ).load( imgPath ).apply( options ).into( user_img );
                //上传头像到服务器端
                File file = new File( imgPath );
                RequestBody body = RequestBody.create( MediaType.parse( "image/*" ), file );
                String p = "MineUpLoadServlet?" + "name=" + phoneNumber;
                Request request = new Request.Builder().url( Data.ip + p ).post( body ).build();
                Call call = okHttpClient.newCall( request );
                call.enqueue( new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText( Edit.this, "网络连接失败。。。", Toast.LENGTH_SHORT ).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        imgPath = response.body().string();
                        Log.e( "上传头像", imgPath );
                    }
                } );
            }
        }
        super.onActivityResult( requestCode, resultCode, data );
    }
}
