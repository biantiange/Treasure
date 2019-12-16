package com.example.lenovo.maandroid.Record;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.lenovo.maandroid.Entity.Dates;
import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Utils.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecordFragment extends Fragment {
    List<Dates> datas = new ArrayList<Dates>();
    String [] timeArray=null;
    List<Map<String,Object>> contentArray=new ArrayList<>();
    private HorizontalListView mlv;
    private Banner banner;
    private OkHttpClient okHttpClient =new OkHttpClient();
    private List<String> mTitleList = new ArrayList<>();
    private List<String > mImgList = new ArrayList<>();
    private Handler mainHandler=null;
    private View newView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        newView=inflater.inflate(R.layout.record_main,container,false);
        mlv = newView.findViewById(R.id.mlv);
        contentArray.clear();
        datas.clear();
        mTitleList.clear();
        mImgList.clear();
        //查找
        ImageView look=newView.findViewById(R.id.look);
        ImageView add=newView.findViewById(R.id.add);
        look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),LookActivity.class);
                startActivity(intent);
            }
        });
        //添加
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),AddActivity.class);
                startActivity(intent);
            }
        });
        mainHandler=new Handler(){
            //当handler在消息队列中
            @Override
            public void handleMessage(Message msg) {
                Log.e("1",Thread.currentThread().getName()+"发送数据"+msg);
                switch (msg.what){
                    case 1:
                        TimeAdapter adapter = new TimeAdapter((ArrayList<Dates>) datas, getContext(),datas.size()-1);
                        mlv.setAdapter(adapter);
                        mlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                TimeAdapter adapter = new TimeAdapter((ArrayList<Dates>) datas, getContext(),position);
                                mlv.setAdapter(adapter);
                                String time=datas.get(position).getTime();
                                lookContent(time);

                            }
                        });
                        break;
                    case 2:
                        banner =newView.findViewById(R.id.home_play_banner);
                        // 设置轮播图
                        BannerSet();
                        break;
                }

            }
        };
        lookTime();
        return newView;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        contentArray.clear();
//        mTitleList.clear();
//        mImgList.clear();
//        datas.clear();
//        //查找
//        ImageView look=newView.findViewById(R.id.look);
//        ImageView add=newView.findViewById(R.id.add);
//        look.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getContext(),LookActivity.class);
//                startActivity(intent);
//            }
//        });
//        //添加
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getContext(),AddActivity.class);
//                startActivity(intent);
//            }
//        });
//        mainHandler=new Handler(){
//            //当handler在消息队列中
//            @Override
//            public void handleMessage(Message msg) {
//                Log.e("1",Thread.currentThread().getName()+"发送数据"+msg);
//                switch (msg.what){
//                    case 1:
//                        TimeAdapter adapter = new TimeAdapter((ArrayList<Dates>) datas, getContext(),datas.size()-1);
//                        mlv.setAdapter(adapter);
//                        mlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                TimeAdapter adapter = new TimeAdapter((ArrayList<Dates>) datas, getContext(),position);
//                                mlv.setAdapter(adapter);
//                                String time=datas.get(position).getTime();
//                                lookContent(time);
//
//                            }
//                        });
//                        break;
//                    case 2:
//                        banner =newView.findViewById(R.id.home_play_banner);
//                        // 设置轮播图
//                        BannerSet();
//                        break;
//                }
//
//            }
//        };
//        lookTime();
//    }

    private void lookTime() {
        Request request=new Request.Builder().url(Constant.BASE_IP +"LookTimeServlet").build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String timeList= response.body().string();
                Log.e("时间列表",timeList);
                Gson gson=new Gson();
                timeArray= gson.fromJson(timeList,String[].class);
                initData();
                if (timeArray.length>0){
                    lookContent(timeArray[timeArray.length-1]);
                }

                Message msg=new Message();
                msg.what=1;
                mainHandler.sendMessage(msg);


            }
        });
    }

  //时间轴初始化
    private void initData() {
        Log.e("initData","时间轴初始化");
        for(int i=0;i<timeArray.length;i++){
            Dates item = new Dates();
            item.setTime(timeArray[i]);
            datas.add(item);
        }
    }

    private void lookContent(String time) {
        Request request=new Request.Builder().url(Constant.BASE_IP +"LookContentServlet?time="+time).build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String contentList= response.body().string();
                Log.e("最后一天的图片列表",contentList);
                Gson gson=new Gson();
                Type listType=new TypeToken<List<Map<String,Object>>>(){}.getType();
                 contentArray=gson.fromJson(contentList,listType);
                Message msg=new Message();
                msg.what=2;
                mainHandler.sendMessage(msg);
            }
        });
    }
//轮播图
    private void BannerSet() {
        Log.e("轮播图","轮播图");
        mImgList.clear();
        for(int i=0;i<contentArray.size();i++){
            Log.e("轮播图 图片",contentArray.get(i).get("path").toString());
            Log.e("轮播图 内容",contentArray.get(i).get("cont").toString());
            mImgList.add(Constant.BASE_IP +contentArray.get(i).get("path").toString());
        }

//        mImgList.add(R.drawable.ly);
//        mImgList.add(R.drawable.lz);
        mTitleList.clear();
        Log.e("tt2",contentArray.size()+":"+mImgList.size());
        for (int k = 0; k < contentArray.size(); k++) {
            String str=contentArray.get(k).get("cont").toString();
            mTitleList.add(str);
        }
        // 显示圆形指示器和标题（水平显示
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        banner.setImageLoader(new MyLoader());
        //设置图片集合
        banner.setImages(mImgList);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(mTitleList);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    // 图片加载器
    public class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }
}
