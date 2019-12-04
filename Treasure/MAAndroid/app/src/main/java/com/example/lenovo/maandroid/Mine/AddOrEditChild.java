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
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
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
    private int po;
    private OkHttpClient okHttpClient;
    private Time time;
    private  SharedPreferences usermessage;
    private String imgPath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.addoreditchild);
        child_submit=findViewById( R.id.child_submit);
        child_img=findViewById( R.id.child_img);
        child_nickname=findViewById( R.id.child_nickname);
        child_years=findViewById( R.id.child_years);
        child_back=findViewById( R.id.child_fanhui);
        child_months=findViewById( R.id.child_month);
        child_day=findViewById( R.id.child_day);
        //关联设备：

        MyListener myListener=new MyListener();
        child_back.setOnClickListener( myListener);
        child_submit.setOnClickListener( myListener );

        child_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions( AddOrEditChild.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100 );
            }
        } );


/*
        po=getIntent().getIntExtra("position",-1);//设置默认值-1；如果不是-1；则说明接受到数据
        //获取头像加载到user_img
        *//*查找sharepeference
        *
        * *//*
        //根据po的值获取child 的各个属性值填入表中
        if(po!=-1){

            int url=getIntent().getIntExtra("url",0);
            String name=getIntent().getStringExtra( "nickname" );
            String[] ol=getIntent().getStringArrayExtra( "old" );
            int y= Integer.parseInt( ol[0] );
            int m= Integer.parseInt( ol[1] );
            int d= Integer.parseInt( ol[2] );
            RequestOptions options=new RequestOptions().circleCrop();
            Glide.with(this)
                    .load(url)
                    .apply(options)
                    .into(child_img);
            child_nickname.setText(name);
            child_years.setText( y );
            child_months.setText( m );
            child_day.setText( d );
        }
        */
    }

    private class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.child_fanhui:
                    finish();
                    break;

                case R.id.child_submit:
                    //获取更改的信息,提交到数据库
                    String childname = child_nickname.getText().toString();
                    String years = child_years.getText().toString();
                    String mon = child_months.getText().toString();
                    String day = child_day.getText().toString();
                    String bir=years+mon+day;
                    int birth= Integer.parseInt( bir );
                    //1进行数据库的更改操作
                    //2同时将sd的数据进行更改
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");// HH:mm:ss
                    Date date = new Date(System.currentTimeMillis());
                    Log.e("dangqiannian",simpleDateFormat.format(date));

                    int t1= Integer.parseInt( simpleDateFormat.format(date) );
                    boolean ye = Integer.parseInt( years ) <= t1;
                    boolean mo = Integer.parseInt( mon ) <= 12 && Integer.parseInt( mon ) > 0;
                    boolean da = Integer.parseInt( day ) > 0 && Integer.parseInt( day ) < 31;
                    if (years != null && mon != null && day != null && (childname.length() > 0 && childname.length() < 8)) {
                        Log.e( "position",po+"" );
                        if (ye && mo && da) {



                            if (po != -1) {
                                //数据库修改
                                OkHttpClient okHttpClient = new OkHttpClient();
                                FormBody body = new FormBody.Builder(  ).add( "","" ).add( "","" ).build();
                                Request request = new Request.Builder().url("" ).post( body ).build();
                                Call call = okHttpClient.newCall( request );
                                call.enqueue( new okhttp3.Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }
                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String str = response.body().string();
                                        Gson gson=new Gson();
                                      //  User user = gson.fromJson( str,String.class );
                                    }
                                } );






                              /*  Map<String, Object> map = new HashMap<>();
                                map.put( "child_nickname",childname);
                                map.put( "child_img_a",R.drawable.aaa );
                                map.put( "child_old",s);
                                Data.datasources.remove( po );
                                Data.datasources.add(po,map);*/
                            } else {
                                //数据库添加
                                sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
                                String phoneNumber = sharedPreferences.getString("phoneNumber","");
                                   Log.e( "phone",phoneNumber);
String id= String.valueOf( Integer.parseInt( phoneNumber )+1 );
                                OkHttpClient okHttpClient = new OkHttpClient();
                                /*id,parentId,birthday,imgPath,name*/
                                FormBody body = new FormBody.Builder(  ).add( "id",id).add( "parentId",phoneNumber )
                                        .add( "birthday", String.valueOf( birth ) )
                                        .add( "imgPath",imgPath)
                                        .add( "name",childname)
                                        .build();
                                Request request = new Request.Builder().url("htttl://127.0.0.1:8080/mychild/AddChildServlet").post( body ).build();
                                Call call = okHttpClient.newCall( request );
                                call.enqueue( new okhttp3.Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }
                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String str = response.body().string();
                                        Gson gson=new Gson();
                                        //  User user = gson.fromJson( str,String.class );
                                    }
                                } );


                                }
                               /* Log.e( "da",Data.datasources.size()+"" );
                                Map<String, Object> map = new HashMap<>();
                                map.put( "child_nickname",childname);
                                map.put( "child_img_a",R.drawable.aaa );
                                map.put( "child_old",s);
                                Data.datasources.add( map );
                                Log.e( "da",Data.datasources.size()+"" );
                                Log.e( "datasources",Data.datasources.toString() );*/


                            }

                        } else {
                            Toast.makeText( AddOrEditChild.this,
                                    "不能为空值，输入正确的日期格式",
                                    Toast.LENGTH_SHORT ).show();
                        }
                    }/* else {
                        Toast.makeText( AddOrEditChild.this,
                                "不能为空值，名字长度小于8",
                                Toast.LENGTH_SHORT ).show();
                    }*/



                   // Drawable drawable=child_img.getDrawable();

                finish();









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
                            Request request=new Request.Builder().url("upload").post(body).build();
                          /*  Call call=okHttpClient.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Log.e("上传头像",response.body().string());
                                }
                            });*/
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    }


