package com.example.lenovo.maandroid.Record;

import android.content.SharedPreferences;
import android.graphics.Color;
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_record);

        findView();

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

            tvAttrTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvAttrTag.setBackgroundColor(Color.parseColor("#d71345"));
                    if (tags.equals("")){
                        tags = tags+tvAttrTag.getText().toString();
                    }else{
                        tags=tags+"-"+tvAttrTag.getText().toString();
                    }
                    Log.e("LookActivity搜索的字符串",tags);
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
                        Log.e("LookActivity",str);
                        if(str!=null){
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
                            EventBus.getDefault().post("展示");
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
        parentId = 1;
        listView = findViewById(R.id.lv_records);
        ivReturn = findViewById(R.id.iv_return);
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
        if(event.equals("展示")){
            Log.e("LookActivity","展示");
            lookAdapter = new LookAdapter(data,LookActivity.this,R.layout.item_lookbytag);
            listView.setAdapter(lookAdapter);
        }
    }
}
