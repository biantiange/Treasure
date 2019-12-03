package com.example.lenovo.maandroid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.library.AutoFlowLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;
import okhttp3.Callback;
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
    private  GrideAdapter grideAdapter;
    private  List<String> list=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_record);

        mFlowLayout =  findViewById(R.id.afl_cotent);
        for (int i = 0; i< 9; i ++ ){
            View item = LayoutInflater.from(this).inflate(R.layout.sub_item, null);
            final TextView tvAttrTag = item.findViewById(R.id.tv_attr_tag);
            tvAttrTag.setText(mData[i]);
            mFlowLayout.addView(item);

            tvAttrTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        tvAttrTag.setTextColor(Color.parseColor("#d71345"));
                }

            });
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        gridView=findViewById(R.id.add_grid);

        okHttpClient=new OkHttpClient();
        addUpload=findViewById(R.id.add_upload);
        addUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                ArrayList<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                grideAdapter=new GrideAdapter(this,mSelectPath,R.layout.list_gride);
                gridView.setAdapter(grideAdapter);
                for(int i=0;i<mSelectPath.size();i++){
                    Log.e("路径",mSelectPath.get(i).toString());
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
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
