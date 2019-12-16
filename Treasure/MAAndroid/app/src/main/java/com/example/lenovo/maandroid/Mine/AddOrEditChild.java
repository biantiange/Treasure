package com.example.lenovo.maandroid.Mine;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.lenovo.maandroid.Utils.Data;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddOrEditChild extends AppCompatActivity {
    private ImageButton child_back;//返回按钮
    private Button child_submit;//修改完成后提交
    private ImageView child_img;//用户头像
    private EditText child_nickname;//孩子名字
    private EditText child_years;//年a
    private EditText child_months;//月
    private EditText child_day;//日
    private ImageView btn_delete;
    private SharedPreferences sharedPreferences;
    private int po;//判断是增加还是更改数据
    private OkHttpClient okHttpClient;
    private Time time;
    private String imgPath;
    private String id;
    private int parentId;
    private boolean ye;//年正确否
    private boolean mo;//月正确否
    private boolean da;//日正确否
    private RequestOptions options;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.addoreditchild );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8 );
        }
        po = getIntent().getIntExtra( "position", -1 );//设置默认值-1；如果不是-1；则说明接受到数据
        findView();
        //关联设备：
        initData();
        setAdapter();
    }

    private void findView() {
        btn_delete=findViewById(R.id.btn_de);
        child_submit = findViewById( R.id.child_submit );
        child_img = findViewById( R.id.child_img );
        child_nickname = findViewById( R.id.child_nickname );
        child_years = findViewById( R.id.child_years );
        child_back = findViewById( R.id.child_fanhui );
        child_months = findViewById( R.id.child_month );
        child_day = findViewById( R.id.child_day );
    }

    private void initData() {
        sharedPreferences = getSharedPreferences( "parent", MODE_PRIVATE );
        parentId = sharedPreferences.getInt( "parentId", 0 );
        okHttpClient = new OkHttpClient();
        options = new RequestOptions()
                .circleCrop()
                .placeholder( R.drawable.ertong )
                .error( R.drawable.ertong )
                .fallback( R.drawable.ertong );
        if (po != -1) {
            btn_delete.setVisibility( View.VISIBLE);
            imgPath = getIntent().getStringExtra( "imgPath" );
            String name = getIntent().getStringExtra( "nickname" );
            id = getIntent().getStringExtra( "id" );
            Log.e( "id",id);
            String y = getIntent().getStringExtra( "years" );
            String m = getIntent().getStringExtra( "mon" );
            String d = getIntent().getStringExtra( "day" );
            Glide.with( this )
                    .load( Data.url+imgPath )
                    .apply( options )
                    .into( child_img );
            child_nickname.setText( name );
            child_years.setText( y );
            child_months.setText( m );
            child_day.setText( d );
        } else {
            btn_delete.setVisibility( View.INVISIBLE);
            Glide.with( this )
                    .load( Data.url+imgPath )
                    .apply( options )
                    .into( child_img );
        }

    }

    private void setAdapter() {
        MyListener myListener = new MyListener();
        child_back.setOnClickListener( myListener );
        child_submit.setOnClickListener( myListener );
        btn_delete.setOnClickListener( myListener );
        child_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions( AddOrEditChild.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100 );
            }
        } );
    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.child_fanhui:
                    finish();
                    break;

                case R.id.btn_de:
                         showdialog();
                        break;
                case R.id.child_submit:
                    //获取信息,提交到数据库
                    String child_name = child_nickname.getText().toString();
                    String years = child_years.getText().toString();
                    String mon = child_months.getText().toString();
                    String day = child_day.getText().toString();
                    //获取当前时间，判断输入的时间是否正确，
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyyMMdd" );// HH:mm:ss
                    Date date = new Date( System.currentTimeMillis() );
                    String data0 = simpleDateFormat.format( date );
                    String t = data0.substring( 0, 4 );
                    String t2 = data0.substring( 4, 6 );
                    String t3 = data0.substring( 6, 8 );
                    if (child_name.length() > 0 && child_name.length() < 10) {
                        if (years != null && (years.length() == 4) && mon != null && day != null) {
                            int t1 = Integer.parseInt( t );
                            int t7 = Integer.parseInt( t2 );
                            int t8 = Integer.parseInt( t3 );
                            int yea = Integer.parseInt( years );//2001
                            int months = Integer.parseInt( mon );
                            int days0 = Integer.parseInt( day );
                            if (yea == t1) {//判断是否超过当前日期
                                if (months <= t7 && days0 <= t8) {
                                    ye = true;
                                    mo = true;
                                    da = true;
                                }
                            } else {
                                ye = (yea < t1);
                                mo = (months <= 12 && months > 0);
                                da = (days0 > 0 && days0 <= 31);
                            }

                            if (mon.length() != 2) {
                                mon = "0" + mon;
                            }
                            if (day.length() != 2) {
                                day = "0" + day;
                            }
                            String oldage = years +"-"+ mon+"-"+ day;
                            //判断输入的日期是否正确
                            if (ye && mo && da) {
                                //数据库添加或者更改
                                //sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
                                //String phoneNumber = sharedPreferences.getString("phoneNumber","");
                                OkHttpClient okHttpClient = new OkHttpClient();
                                /*id,parentId,birthday,imgPath,name*/
                                Log.e( "position", po + "" );
                                if (po != -1) {
                                    FormBody body2 = new FormBody.Builder()
                                            .add( "id", id )
                                            .add( "parentId", parentId+"")
                                            .add( "birthday", oldage )
                                            .add( "imgPath", imgPath )
                                            .add( "name", child_name )
                                            .build();
                                    //数据库修改
                                    Log.e( "进入修改：", child_name );
                                    Request request2 = new Request.Builder().url(Data.ip + "EditChildServlet" ).post( body2 ).build();
                                    Call call2 = okHttpClient.newCall( request2 );
                                    Log.e( "name", child_name + "2" );
                                    call2.enqueue( new okhttp3.Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Looper.prepare();
                                            Toast.makeText( AddOrEditChild.this, "网络连接失败。。。", Toast.LENGTH_SHORT ).show();
                                            Looper.loop();
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String str = response.body().string();
                                            Log.e( "s", str );
                                            setResult( 3 );
                                            finish();
                                        }
                                    } );
                                } else {
                                    if (imgPath == null) {
                                        imgPath = "aaa.jpj";
                                    }
                                    FormBody body3 = new FormBody.Builder()
                                            .add( "parentId", parentId+"" )
                                            .add( "birthday", oldage )
                                            .add( "imgPath", imgPath )
                                            .add( "name", child_name )
                                            .build();
                                    Request request3 = new Request.Builder().url(Data.ip + "AddChildServlet" ).post( body3 ).build();
                                    Call call3 = okHttpClient.newCall( request3);
                                    call3.enqueue( new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Looper.prepare();
                                            Toast.makeText( AddOrEditChild.this, "网络连接失败。。。", Toast.LENGTH_SHORT ).show();
                                            Looper.loop();
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String str = response.body().string();
                                            Log.e( "s", str );
                                            setResult( 4 );
                                            finish();
                                        }
                                    } );
                                }
                            } else {
                                Toast.makeText( AddOrEditChild.this,
                                        "请输入正确的日期",
                                        Toast.LENGTH_SHORT ).show();
                            }
                        } else {
                            Toast.makeText( AddOrEditChild.this,
                                    "不能为空值，输入正确的日期格式",
                                    Toast.LENGTH_SHORT ).show();
                        }
                    } else {
                        Toast.makeText( AddOrEditChild.this,
                                "昵称长度是1~9，请重新输入",
                                Toast.LENGTH_SHORT ).show();
                    }

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
                RequestOptions options = new RequestOptions().circleCrop();
                Glide.with( this ).load( imgPath ).apply( options ).into( child_img );
                //上传头像到服务器端
                File file = new File( imgPath );
                RequestBody body = RequestBody.create( MediaType.parse( "image/*" ), file );
                //设置图片的名字
                String p = "MineUpLoadServlet?" + "name=" + parentId;
                Request request = new Request.Builder().url(Data.ip + p ).post( body ).build();
                Call call = okHttpClient.newCall( request );
                call.enqueue( new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
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

    public void showdialog(){

        AlertDialog.Builder alertdialogbuilder=new AlertDialog.Builder(this);
    alertdialogbuilder.setMessage("您确认删除吗");
    alertdialogbuilder.setPositiveButton("确定", click1);
    alertdialogbuilder.setNegativeButton("取消", click2);
    AlertDialog alertdialog1=alertdialogbuilder.create();
    alertdialog1.show();


    }
    private DialogInterface.OnClickListener click1=new DialogInterface.OnClickListener()
{
@Override
public void onClick(DialogInterface arg0,int arg1)
{

    Log.e( "s","删除进行时。。。" );

    FormBody body = new FormBody.Builder()
            .add( "id", id )
            .build();
    //数据库修改
    Request request = new Request.Builder().url( Data.ip + "DeleteChildServlet" ).post( body ).build();
    Call call = okHttpClient.newCall( request );
    call.enqueue( new okhttp3.Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Looper.prepare();
            Toast.makeText( AddOrEditChild.this, "网络连接失败。。。", Toast.LENGTH_SHORT ).show();
            Looper.loop();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String str = response.body().string();
            Log.e( "s", str );
            setResult( 3);
            finish();
        }
    } );

}
};
private DialogInterface.OnClickListener click2=new DialogInterface.OnClickListener()
{
@Override
public void onClick(DialogInterface arg0,int arg1)
{
arg0.cancel();
}
};
}


