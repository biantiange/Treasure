package com.example.lenovo.maandroid.Record;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Record.LookAdapter;
import com.example.lenovo.maandroid.Utils.Constant;
import com.example.library.AutoFlowLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LookActivity extends AppCompatActivity {
    private AutoFlowLayout mFlowLayout;
    private String[] mData = {"生日","比赛","游戏","旅游","互动","成长","捣蛋","陪伴","日常"};
    private LayoutInflater mLayoutInflater;
    private int count = 9;
    private String tags;
    //搜索按钮
    private Button btnSearch;
    private OkHttpClient okHttpClient;
    private int parentId;
    //private HashMap<String,String[]> lists;  //数据库返回来的数据
    private List<Map<String,Object>> lists;
    //private Map<Integer,Map<String,Object>> data;
    private List<Map<String,List<String>>> data;  //用于展示的数据
    private ListView listView;   //展示记录的
    private LookAdapter lookAdapter;

    private ImageView ivReturn;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_record);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8 );
        }
        findView();

        if (lists.isEmpty()){
            linearLayout.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        //给返回按钮添加监听器
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        for (int i = 0; i< 9; i ++ ){
            View item = LayoutInflater.from(this).inflate(R.layout.sub_item, null);
            final TextView tvAttrTag = item.findViewById(R.id.tv_attr_tag);
            tvAttrTag.setText(mData[i]);
            mFlowLayout.addView(item);
            tvAttrTag.setTag(false);
            tvAttrTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvAttrTag.getTag().equals(false)) {
                        Log.e("aa", "没点过");
                        tvAttrTag.setBackgroundResource(R.drawable.textview_border2);
                        Log.e("11",tvAttrTag.getBackground()+"");
                        if (tags.equals("")) {
                            tags = tags + tvAttrTag.getText().toString();
                        } else {
                            if (!tags.contains(tvAttrTag.getText())) {
                                tags = tags + "-" + tvAttrTag.getText().toString();
                            }
                        }
                        tvAttrTag.setTag(true);
                    } else {
                        Log.e("aa", "点过");
                        tvAttrTag.setBackgroundResource(R.drawable.textview_border);
                        tvAttrTag.setTag(false);
                        int position = tags.indexOf(tvAttrTag.getText().toString());
                        Log.e("aa", position + "");
                        Log.e("len", tags.length() + "");
                        if (position > 2) {
                            tags = tags.replace("-" + tvAttrTag.getText(), "");
                        } else if (position == 0 && tags.length() != 2) {

                            tags = tags.replace(tvAttrTag.getText() + "-", "");
                        } else {
                            tags = tags.replace(tvAttrTag.getText(), "");
                        }
                    }
                    Log.e("AddActivity成长记录的字符串", tags);
                }
            });
        }

        //搜索按钮
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormBody body =new FormBody.Builder()
                        .add("str",tags)
                        .add("parentId",parentId+"")
                        .build();
                Request request = new Request.Builder().url(Constant.BASE_IP+"LookByTagServlet").post(body).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        lists = new Gson().fromJson(str,new TypeToken<List<Map<String,Object>>>(){}.getType());
                        Log.e("LookActivity",str);
                        if(!lists.isEmpty()){
                            Log.e("aa","有数据");
                            //lists中有grimg的id，内容content，更新时间upTime，图片路径imgPath
                            lists = new Gson().fromJson(str,new TypeToken<List<Map<String,Object>>>(){}.getType());
                            Log.e("LookActivity",lists.toString());
                            //按时间分成一组
                            Map<String,List<String>> map = new HashMap<>();
                            List<String> strs = new ArrayList<>();
                            strs.add(lists.get(0).get("imgPath").toString());
                            map.put(lists.get(0).get("upTime").toString(),strs);
                            data.add(map);
                            Log.e("",data.size()+"");
                            Log.e("",lists.size()+"");
                            int size = data.size();
                            for(int i=0;i<size;i++){
                                for(int j=1;j<lists.size();j++){
                                    Map<String,List<String>> map1 = data.get(i);
                                    for (String key : map1.keySet()) {
                                        Log.e("aa",key);
                                        if(key.equals(lists.get(j).get("upTime").toString())){
                                            Log.e("LookActivity","相等了");
                                            List<String> str1 = map1.get(key);
                                            str1.add(lists.get(j).get("imgPath").toString());
                                            map1.put(key,str1);
                                            //data.add(map1);
                                        }else{
                                            Log.e("LookActivity","不相等");
                                            Map<String,List<String>> map2 = new HashMap<>();
                                            List<String> str2 = new ArrayList<>();
                                            str2.add(lists.get(j).get("imgPath").toString());
                                            map2.put(lists.get(j).get("upTime").toString(),str2);
                                            data.add(map2);
                                        }
                                    }

                                }
                            }
                            Log.e("LookActivity展示的数据",data.toString());
                            List<Map<String,Object>> list=new ArrayList<>();
                            Map<String,Object> mapp=new HashMap<>();
                            Map<String,Object> mapp1=new HashMap<>();
                            mapp1.put("show","展示");
                            mapp.put("tag",mapp1);
                            mapp.put("content",data);
                            list.add(mapp);
                            EventBus.getDefault().post(new Gson().toJson(list));
                        }else {
                            Log.e("aa","无数据");
                            List<Map<String,Object>> list=new ArrayList<>();
                            Map<String,Object> mapp=new HashMap<>();
                            Map<String,Object> mapp1=new HashMap<>();
                            mapp1.put("show","隐藏");
                            mapp.put("tag",mapp1);
                            mapp.put("content",data);
                            list.add(mapp);
                            EventBus.getDefault().post(new Gson().toJson(list));
                        }

                    }

                });
            }
        });
    }

    private void findView() {
        data = new ArrayList<>();
        lists = new ArrayList<>();
        okHttpClient = new OkHttpClient();
        mFlowLayout =  findViewById(R.id.afl_cotent);
        tags="";
        btnSearch = findViewById(R.id.btn_search);
        //parentId = 1;
        parentId = getSharedPreferences( "parent",MODE_PRIVATE ).getInt( "parentId",-1 );
        listView = findViewById(R.id.lv_records);
        ivReturn = findViewById(R.id.iv_return);
        linearLayout = findViewById(R.id.ll);
       // Log.e("aa",lists.isEmpty()+"");
        /*SharedPreferences sharedPreferences = getSharedPreferences("parent",MODE_PRIVATE);
        parentId = sharedPreferences.getInt("parentId",-1);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String event){
        List<Map<String, Object>> list = new Gson().fromJson(event, new TypeToken<List<Map<String, Object>>>() {}.getType());
        String str=list.get(0).get("tag").toString();
        Map<String, Object> map = new HashMap<String, Object>();
        map = new Gson().fromJson(str, map.getClass());
        String tag1=null;
        Iterator<String> iter = map.keySet().iterator();
        while(iter.hasNext()){
            String key=iter.next();
            tag1 = map.get(key).toString();
        }
        if(tag1.equals("展示")){
            Log.e("LookActivity","展示");
            linearLayout.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            lookAdapter = new LookAdapter(data,LookActivity.this,R.layout.item_lookbytag);
            listView.setAdapter(lookAdapter);
        }
        if(tag1.equals("隐藏")){
            Log.e("LookActivity","隐藏");
            listView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }
}
