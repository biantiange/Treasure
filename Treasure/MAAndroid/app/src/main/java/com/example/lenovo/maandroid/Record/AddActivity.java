package com.example.lenovo.maandroid.Record;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.maandroid.Entity.Grimg;
import com.example.lenovo.maandroid.Entity.GrowthRecord;
import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Utils.Constant;
import com.example.library.AutoFlowLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddActivity extends AppCompatActivity {
    private AutoFlowLayout mFlowLayout;
    private String[] mData = {"生日","比赛","游戏","旅游","互动","成长","捣蛋","陪伴","日常"};
    private LayoutInflater mLayoutInflater;
    private int count = 9;

    private OkHttpClient okHttpClient;
    private Button addUpload;
    private ImageView addPicture;
    private GridView gridView;
    private GrideAdapter grideAdapter;
    private EditText etContent;
    private  List<String> list=new ArrayList<>();

    private GrowthRecord growthRecord;
    private Grimg grimg;
    private ArrayList<String> mSelectPath;  //从相册选择的图片路径
    private String tags;     //标签数组
    //返回按钮
    private ImageView ivReturn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_record);
        //初始化控件
        initView();
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mFlowLayout =  findViewById(R.id.afl_cotent);
        for (int i = 0; i< 9; i ++ ){
            View item = LayoutInflater.from(this).inflate(R.layout.sub_item, null);
            final TextView tvAttrTag = item.findViewById(R.id.tv_attr_tag);
            tvAttrTag.setText(mData[i]);
            mFlowLayout.addView(item);

            tvAttrTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        tvAttrTag.setBackgroundColor(Color.parseColor("#d71345"));
                        if (tags.equals("")){
                            tags = tags+tvAttrTag.getText().toString();
                        }else{
                            tags=tags+"-"+tvAttrTag.getText().toString();
                        }
                        Log.e("AddActivity成长记录的字符串",tags);
                }

            });
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        gridView=findViewById(R.id.add_grid);

        okHttpClient=new OkHttpClient();
        addUpload=findViewById(R.id.add_upload);
        //上传按钮
        addUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先上传到growthRecord表
                initData();
                Log.e("AddActivity",growthRecord.toString());
                FormBody body = new FormBody.Builder()
                        .add("parentId", growthRecord.getParentId() + "")
                        .add("upTime", growthRecord.getUpTime())
                        .add("content", growthRecord.getContent())
                        /*.add("imgPath",grimg.getImgPath())*/
                        //.add("tag",grimg.getTag())
                        .build();
                final Request request = new Request.Builder().url(Constant.BASE_IP + "AddGrowthRecordServlet").post(body).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        Log.e("插入的成长记录的id值", str);
                        if (str != null && !str.equals("")) {
                            //返回的是插入的growthRecord的id值
                            growthRecord.setId(Integer.parseInt(str));
                            grimg.setGrowthRecordId(Integer.parseInt(str));
                            //在根据这个growthRecordID插入grimg表
                            addRecordImgPath();
                        }
                    }
                });
            }
        });
    }
    //初始化视图
    public void initView(){
        etContent = findViewById(R.id.et_content);
        tags="";
        ivReturn = findViewById(R.id.iv_return);
    }
    //初始化数据
    public void initData() {
        growthRecord = new GrowthRecord();
        grimg = new Grimg();
        //获得控件值
        String content = etContent.getText().toString();
        int parentId = getSharedPreferences("parent",MODE_PRIVATE).getInt("parentId",-1);
        //String tag = "";
        //String imgPath = "";
        //初始化GrowthRecord
        growthRecord.setContent(content);
        growthRecord.setParentId(parentId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        growthRecord.setUpTime(simpleDateFormat.format(date));
        //初始化Grimg
        //grimg.setImgPath(imgPath);
        grimg.setTag(tags);
        grimg.setUpTime(simpleDateFormat.format(date));
    }
    public void addRecordImgPath() {
        for (int i = 0; i < mSelectPath.size(); i++) {
            Log.e("路径", mSelectPath.get(i).toString());
            //上传至服务器
            File file = new File(mSelectPath.get(i).toString());
            //Log.e("图片名字",file.getName());
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            Request request = new Request.Builder().url(Constant.BASE_IP + "UploadServlet").post(body).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    int id = Integer.parseInt(response.body().string());
                    if (id != -1) { //说明插入图片成功，返回的是插入图片的id
                        //根据图片id插入其他值
                        addRecordImgOther(id);
                    }
                }
            });
        }
    }

    public void addRecordImgOther(int imgId){
        //修改数据库
        Log.e("AddActivity",grimg.toString());
        FormBody body = new FormBody.Builder()
                .add("id",imgId+"")
                .add("growthRecordId", grimg.getGrowthRecordId() + "")
                .add("upTime", grimg.getUpTime())
                .add("tag", grimg.getTag())
                .build();
        final Request request = new Request.Builder().url(Constant.BASE_IP + "GrimgServlet").post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                //Log.e("插入的成长图片的id值", str);
                if (str.equals("OK")) {
                    Looper.prepare();
                    Toast.makeText(AddActivity.this,"上传成功", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_picture:

                ActivityCompat.requestPermissions(AddActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},1);
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1 ){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "权限申请成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MultiImageSelectorActivity.class);
                // 是否显示调用相机拍照
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                //设置获取最大图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 6);
                // 设置模式（单选 MODE_SINGLE /多选MODE_MULTI)
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                startActivityForResult(intent, 2);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==2&&resultCode==RESULT_OK){
            if (data != null) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                grideAdapter=new GrideAdapter(this,mSelectPath,R.layout.list_gride);
                gridView.setAdapter(grideAdapter);
                }
            }
//            Uri uri=data.getData();
//            Cursor cursor=getContentResolver().query(uri,null,null,null,null);
//            if(cursor.moveToFirst()){
//                String imgPath=cursor.getString(cursor.getColumnIndex("_data"));
//                list.add(imgPath);
//                grideAdapter=new GrideAdapter(this,list,R.layout.list_gride);
//                gridView.setAdapter(grideAdapter);
//            }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
