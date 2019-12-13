package com.example.lenovo.maandroid.Mine;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
    private SharedPreferences sharedPreferences;
    private int po;//判断是增加还是更改数据
    private OkHttpClient okHttpClient;
    private Time time;
    private  SharedPreferences usermessage;
    private String imgPath;
    private  String id;
    private String parentId;
    private boolean ye;//年正确否
    private   boolean mo;//月正确否
    private   boolean da;//日正确否
    private RequestOptions options;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.addoreditchild);
        findView();
        //关联设备：
        setAdapter();
        initData();
        po=getIntent().getIntExtra("position",-1);//设置默认值-1；如果不是-1；则说明接受到数据


    }

    private void findView() {
        child_submit=findViewById( R.id.child_submit);
        child_img=findViewById( R.id.child_img);
        child_nickname=findViewById( R.id.child_nickname);
        child_years=findViewById( R.id.child_years);
        child_back=findViewById( R.id.child_fanhui);
        child_months=findViewById( R.id.child_month);
        child_day=findViewById( R.id.child_day);
    }
    private void initData() {
        sharedPreferences=getSharedPreferences( "parent",MODE_PRIVATE );
        okHttpClient=new OkHttpClient();
        options=new RequestOptions()
                .circleCrop()
                .placeholder( R.drawable.ertong )
                .error( R.drawable.ertong )
                .fallback( R.drawable.ertong );
        if(po!=-1){
            int url=getIntent().getIntExtra("url",0);
            imgPath=getIntent().getStringExtra( "imgPath" );
            String name=getIntent().getStringExtra( "nickname" );
            id=getIntent().getStringExtra( "id" );
            String y=getIntent().getStringExtra( "years" );
            String m=getIntent().getStringExtra( "mon" );
            String d=getIntent().getStringExtra( "day" );
            parentId=getIntent().getStringExtra( "parentId" );

            RequestOptions options=new RequestOptions().circleCrop();
            Glide.with(this)
                    .load(imgPath)
                    .apply(options)
                    .into(child_img);
            child_nickname.setText(name);
            child_years.setText( y );
            child_months.setText( m );
            child_day.setText( d );
        }else {
            Glide.with(this)
                    .load(imgPath)
                    .apply(options)
                    .into(child_img);
        }

    }

    private void setAdapter() {
        MyListener myListener=new MyListener();
        child_back.setOnClickListener( myListener);
        child_submit.setOnClickListener( myListener );
        child_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions( AddOrEditChild.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100 );
            }
        } );
    }

    private class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.child_fanhui:
                    finish();
                    break;
                case R.id.child_submit:
                    //获取信息,提交到数据库
                    String child_name = child_nickname.getText().toString();
                    String years = child_years.getText().toString();
                    String mon = child_months.getText().toString();
                    String day = child_day.getText().toString();
                    Log.e("childname",child_name);
                    Log.e( "year",years );
                    Log.e( "mon",mon );
                    Log.e( "day",day );
                    //获取当前时间，判断输入的时间是否正确，
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");// HH:mm:ss
                    Date date = new Date(System.currentTimeMillis());
                    String data0=simpleDateFormat.format( date );
                    String t=data0.substring( 0,4 );
                    String t2=data0.substring( 4,6 );
                    String t3=data0.substring( 6,8 );
                    int t1= Integer.parseInt( t );
                    int t7=Integer.parseInt( t2 );
                    int t8=Integer.parseInt( t3 );
                    final int yea=Integer.parseInt( years );//2001
                    int months=Integer.parseInt( mon );
                    int days0=Integer.parseInt( day );

                    if(yea==t1){
                        if(months<=t7&&days0<=t8){
                            ye=true;
                            mo=true;
                            da=true;
                        }
                    }else {
                        ye = (yea < t1);
                        mo = (months<= 12 && months > 0);
                        da = (days0> 0 && days0 <= 31);
                    }
                    //------------------------------------------------------判断时间格式结束
                    if (years != null &&(years.length()==4) &&mon != null && day != null && (child_name.length() > 0 && child_name.length() < 13)) {
                        if(mon.length()!=2){
                            mon="0"+mon;
                        }
                        if(day.length()!=2){
                            day="0"+day;
                        }
                        Log.e( "id","1" );
                        String oldage=years+mon+day;
                        //判断输入的日期是否正确
                        Log.e( "ye","1"+ye );
                        Log.e( "mo","1"+mo );
                        Log.e( "da","1"+da );
                        if (ye && mo && da) {
                            //数据库添加或者更改
                            //sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
                            //String phoneNumber = sharedPreferences.getString("phoneNumber","");
                            OkHttpClient okHttpClient = new OkHttpClient();
                            /*id,parentId,birthday,imgPath,name*/
                            Log.e( "position",po+"" );
                            if (po != -1) {
                                FormBody body = new FormBody.Builder(  )
                                        .add( "id",id)
                                        .add( "parentId", Data.parent)
                                        .add( "birthday",oldage )
                                        .add( "imgPath",imgPath)
                                        .add( "name",child_name)
                                        .build();
                                //数据库修改
                                Log.e( "进入修改：",child_name);
                                Request request = new Request.Builder().url(Data.ip+"/mychild/EditChildServlet" ).post( body ).build();
                                Call call = okHttpClient.newCall( request );
                                Log.e( "name",child_name+"2");
                                call.enqueue( new okhttp3.Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Log.e( "shibai","修改连接服务器失败" );
                                    }
                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String str = response.body().string();
                                        Log.e( "s",str );
                                    }
                                } );
                           } else {
                                FormBody body = new FormBody.Builder(  )
                                        .add( "id",Data.parent+"0")
                                        .add( "parentId",Data.parent)
                                        .add( "birthday",oldage )
                                        .add( "imgPath",imgPath)
                                        .add( "name",child_name)
                                        .build();

                                Request request = new Request.Builder().url(Data.ip+"/mychild/AddChildServlet").post( body ).build();
                                Call call = okHttpClient.newCall( request );
                                Log.e( "name",child_name+"1");
                                call.enqueue( new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Log.e( "name","次失败2");
                                    }
                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String str = response.body().string();
                                        Log.e( "s",str );
                                    }
                                } );
                                }
                            }
                        }else {
                            Toast.makeText( AddOrEditChild.this,
                                    "不能为空值，输入正确的日期格式",
                                    Toast.LENGTH_SHORT ).show();
                        }
                    Intent intent=new Intent( AddOrEditChild.this,MyChild.class );
                    startActivity( intent );
                    }
                   // Drawable drawable=child_img.getDrawable();
            }
        }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==100){
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,200);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==200&&resultCode==RESULT_OK){
            Uri uri=data.getData();
            Cursor cursor=getContentResolver().query(uri,null,null,null,null);
            if(cursor.moveToFirst()){
                imgPath=cursor.getString(cursor.getColumnIndex("_data"));
                RequestOptions options=new RequestOptions().circleCrop();
                Glide.with(this).load(imgPath).apply(options).into(child_img);
                //上传头像到服务器端
                File file=new File(imgPath);
                RequestBody body=RequestBody.create( MediaType.parse("image/*"),file);
                String p="/mychild/UploadServlet?"+"name="+"phoneNumber";
                Request request=new Request.Builder().url(Data.ip+p).post(body).build();
                Call call=okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        imgPath=response.body().string();
                        Log.e("上传头像",imgPath);
                    }
                });
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    }


