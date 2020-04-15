package com.example.lenovo.maandroid.Record;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    String contentList=null;
    List<Map<String,Object>> contentArray=new ArrayList<>();
    private HorizontalListView mlv;
    private Banner banner;
    private OkHttpClient okHttpClient;
    private List<String> mTitleList = new ArrayList<>();
    private List<String > mImgList = new ArrayList<>();
    private List<Integer > ImgList = new ArrayList<>();
    private Handler mainHandler=null;
    int parentId;
    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View newView=inflater.inflate(R.layout.record_main,container,false);
        mlv = newView.findViewById(R.id.mlv);
        okHttpClient=new OkHttpClient();
//        sharedPreferences=getContext().getSharedPreferences("parent",Context.MODE_PRIVATE);
//        parentId=sharedPreferences.getInt("parentId",-1);
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
                        final TimeAdapter adapter = new TimeAdapter((ArrayList<Dates>) datas, getContext());
                        mlv.setAdapter(adapter);
                        mlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                TimeAdapter adapter = new TimeAdapter((ArrayList<Dates>) datas, getContext(),position);
//                                mlv.setAdapter(adapter);
                                datas.get(position).setStatu(1);
                                adapter.notifyDataSetChanged();
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
      //  lookTime();
        return newView;
    }

    private void lookTime() {
        Request request=new Request.Builder().url(Constant.BASE_IP +"LookTimeServlet?parentId="+1).build();
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
               // Log.e("timeArray",timeArray.toString());
                if(timeArray==null||timeArray.length==0 ){
                    inData();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ");
                    Date date = new Date(System.currentTimeMillis());
                    lookContent(simpleDateFormat.format(date));
                    Message msg=new Message();
                    msg.what=1;
                    mainHandler.sendMessage(msg);
                }else{
//                    Gson gson=new Gson();
//                    timeArray= gson.fromJson(timeList,String[].class);
                    initData();
                    if(timeArray.length>0){
                        lookContent(timeArray[timeArray.length-1]);
                    }
                    Message msg=new Message();
                    msg.what=1;
                    mainHandler.sendMessage(msg);
                }

            }
        });
    }

    //时间轴初始化
    private void inData() {
        datas.clear();
        Log.e("inData","iniData");
        Dates item = new Dates();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        Date date = new Date(System.currentTimeMillis());
        item.setTime(simpleDateFormat.format(date));
        item.setStatu(1);
        Log.e("当前时间",simpleDateFormat.format(date));

        Dates item1 = new Dates();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        Date date1=calendar.getTime();
        item1.setTime(simpleDateFormat.format(date1));
        Log.e("前一天时间",simpleDateFormat.format(date1));

        Dates item2 = new Dates();
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        Date date2=calendar.getTime();
        item2.setTime(simpleDateFormat.format(date2));
        Log.e("前一天时间",simpleDateFormat.format(date2));

        datas.add(item2);
        datas.add(item1);
        datas.add(item);
    }


    //时间轴初始化
    private void initData() {
        datas.clear();
         Log.e("initData","时间轴初始化"+datas.size());
        for(int i=0;i<timeArray.length;i++){
            Dates item = new Dates();
            item.setTime(timeArray[i]);
            if(i==timeArray.length-1){
                item.setStatu(1);
            }
            datas.add(item);
        }
//        TimeAdapter adapter = new TimeAdapter((ArrayList<Dates>) datas, getContext());
//        mlv.setAdapter(adapter);
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
                 contentList= response.body().string();
                Log.e("最后一天的图片列表",contentList);
                Gson gson=new Gson();
                Type listType=new TypeToken<List<Map<String,Object>>>(){}.getType();
                 contentArray=gson.fromJson(contentList,listType);
                Message msg=new Message();
                msg.what=2;
                mainHandler.sendMessage(msg);
              //  BannerSet();
            }
        });
    }

    //轮播图
    private void BannerSet() {
        if(contentArray==null||contentArray.size()==0){
            Log.e("BanSet","BanSet");
            ImgList.clear();
            ImgList.add(R.drawable.chengzhang2);
            ImgList.add(R.drawable.chengzhang3);
            ImgList.add(R.drawable.chengzhang4);
            ImgList.add(R.drawable.chengzhang1);
            Log.e("ImgList",ImgList.size()+"");
            Log.e("mTitleList",mTitleList.size()+"");
            mTitleList.clear();
            for (int k = 1; k < ImgList.size()+1; k++) {
                mTitleList.add("第"+k+"张");
            }
            banner.setImages(ImgList);
        }
       else{
            Log.e("轮播图","轮播图");
            mImgList.clear();
            for(int i=0;i<contentArray.size();i++){
                Log.e("轮播图 图片",contentArray.get(i).get("path").toString());
                Log.e("轮播图 内容",contentArray.get(i).get("cont").toString());
                mImgList.add(Constant.BASE_IP +contentArray.get(i).get("path").toString());
            }
            mTitleList.clear();
            Log.e("tt2",contentArray.size()+":"+mImgList.size());
            for (int k = 0; k < contentArray.size(); k++) {
                String str=contentArray.get(k).get("cont").toString();
                mTitleList.add(str);
            }
            banner.setImages(mImgList);
        }

        // 显示圆形指示器和标题（水平显示
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        banner.setImageLoader(new MyLoader());
        //设置图片集合
       // banner.setImages(mImgList);
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

    @Override
    public void onStart() {
//        int id = getActivity().getIntent().getIntExtra("id", 0);
//        if(id==2){
//            lookTime();
//        }
        Log.e("start","start");
        lookTime();
        super.onStart();

    }
}
