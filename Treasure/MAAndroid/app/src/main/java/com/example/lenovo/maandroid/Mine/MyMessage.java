package com.example.lenovo.maandroid.Mine;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Utils.Data;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyMessage extends AppCompatActivity {
    List<Map<String, Object>> message=new ArrayList<>();
    private OkHttpClient okHttpClient;
    private SharedPreferences sharedPreferences;
    private int parentId;
    private ListView listView;
    private MyMessageListViewAdapter adapter;
    private ImageButton return0;
    private SmartRefreshLayout refreshLayout;
    private static final int REFRESH_FINISH = 1;
    private Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REFRESH_FINISH:
                    //结束刷新动画
                    initData();
                    refreshLayout.finishRefresh();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.my_message );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8 );
        }
        okHttpClient=new OkHttpClient();
        sharedPreferences=getSharedPreferences( "parent",MODE_PRIVATE );
        parentId=sharedPreferences.getInt( "parentId",0 );
        EventBus.getDefault().register( this );
        findView();
        init();
        initData();
        setAdapter();
    }
    private void findView() {
        Log.e( "方法：","findview");
        return0 = findViewById( R.id.return0 );
        listView = findViewById( R.id.list_my_message );
        refreshLayout = findViewById(R.id.smart_layout);
        refreshLayout=findViewById( R.id.smart_layout );
    }
    private void init() {
        Log.e( "方法：","init");
        adapter = new MyMessageListViewAdapter( this, message, R.layout.list_my_message );
        listView.setAdapter( adapter );
    }
    private void setAdapter() {
        Log.e( "方法：","setadapter");
        //监听下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //不能执行网络操作，需要使用多线程
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //向主线程发送消息，更新视图
                        Message msg = new Message();
                        msg.what = REFRESH_FINISH;
                        mainHandler.sendMessage(msg);
                    }
                }.start();

            }
        });

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        } );
        return0.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );


    }



    private void initData() {
        Log.e( "方法：","initdata");
        FormBody body = new FormBody.Builder().add( "userId",parentId+"" ).build();
        Request request = new Request.Builder().url( Data.ip + "FindMyMessageServlet" )
                .post( body ).build();
        final Call call = okHttpClient.newCall( request );
        call.enqueue( new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText( MyMessage.this, "网络连接失败,无法获取数据。。。", Toast.LENGTH_SHORT ).show();
                finish();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                message = new Gson().fromJson( jsonStr, new TypeToken<List>() {
                }.getType() );
                Log.e( "方法：",message.toString());
                EventBus.getDefault().post( message );
            }
        } );




    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageReceived(List<Map<String, Object>> data) {
        Log.e( "方法：","onmessage");
        init();


    }

    @Override
    protected void onDestroy() {
        Log.e( "方法：", "onDestroy" );
        EventBus.getDefault().unregister( this );
        super.onDestroy();
    }
}
