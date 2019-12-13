package com.example.lenovo.maandroid.Mine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Utils.Data;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyChild extends AppCompatActivity {
    private OkHttpClient okHttpClient;
    public static List<Map<String, Object>> childs = new ArrayList<>();
    private int i = 0;
    private ListView listView;
    private ImageButton return0;
    private SharedPreferences sharedPreferences;
    private MyChildListViewAdapter myChildListViewAdapter;
    private LinearLayout add_child_a;
    private  int parentId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.add_child );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff00bffe );
        }
        EventBus.getDefault().register( this );
        okHttpClient = new OkHttpClient();
        sharedPreferences=getSharedPreferences( "parent",MODE_PRIVATE);
        parentId=sharedPreferences.getInt( "parentId",0 );
        findView();
        listViewAdpter();
        Log.e( "child", childs.size() + ".....1" );
        init();
        Log.e( "child", childs.size() + ".....3" );
        setAdapter();

    }
    private void listViewAdpter() {
        Log.e( "child", childs.size() + ".....2" );
        Log.e( "方法：", "listViewAdapter" );
        myChildListViewAdapter = new MyChildListViewAdapter( MyChild.this, childs, R.layout.mychild_item );
        listView.setAdapter( myChildListViewAdapter );
    }

    private void findView() {
        Log.e( "方法：", "findView" );
        listView = findViewById( R.id.list_my_child );
        return0 = findViewById( R.id.return0 );
        add_child_a = findViewById( R.id.add_child_a );
    }

    private void setAdapter() {
        Log.e( "方法：", "setAdapter" );
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = childs.get( position );
                Object ur = map.get( "imgPath" );
                String url = ur.toString();
                Object ow = map.get( "id" );
                String ids = ow.toString();
                Object pid = map.get( "parentId" );
                String parentId = pid.toString();
                String name = (String) map.get( "nickName" );
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy" );// HH:mm:ss
                Date date = new Date( System.currentTimeMillis() );
                int t = Integer.parseInt( simpleDateFormat.format( date ) );
                Object ol = map.get( "birthday" );
                String old = ol.toString();
                String olda = old.substring( 0, 4 );
                String mon = old.substring( 4, 6 );
                String day = old.substring( 6, 8 );
                Log.e( "jinxingtiaozhuan", "进行跳转" );
                Intent intent = new Intent( MyChild.this, AddOrEditChild.class );
                intent.putExtra( "position", position );
                intent.putExtra( "years", olda );
                intent.putExtra( "mon", mon );
                intent.putExtra( "day", day );
                intent.putExtra( "nickname", name );
                intent.putExtra( "id", ids );
                intent.putExtra( "imgPath", url );
                intent.putExtra( "url", R.drawable.aaa );
                intent.putExtra( "parentId", parentId );
                startActivityForResult( intent, 1 );

            }
        } );
        //给Item设置点击事件*/

        return0.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );

        add_child_a.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MyChild.this, AddOrEditChild.class );
                startActivityForResult( intent, 2 );
            }
        } );

    }

    private void init() {
        Log.e( "方法：", "init" );
        FormBody body = new FormBody.Builder().add( "parentId",parentId+"" ).build();
        Request request = new Request.Builder().url( Data.ip + "ChildServlet" )
                .post( body ).build();
        final Call call = okHttpClient.newCall( request );
        call.enqueue( new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText( MyChild.this, "网络连接失败,无法获取数据。。。", Toast.LENGTH_SHORT ).show();
                finish();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                List<Map<String, Object>> data = new Gson().fromJson( jsonStr, new TypeToken<List>() {
                }.getType() );
                childs = data;
                Log.e( "child", childs.toString() );
                EventBus.getDefault().post( data );
            }
        } );


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == 3) {

            Log.e( "方法：", "onActivityResult" );
            init();
            Log.e( "childs", childs.toString() );
            listViewAdpter();
        }
        if (requestCode == 2 && resultCode == 4) {

            Log.e( "方法：", "onActivityResult" );
            init();
            Log.e( "childs", childs.toString() );
            listViewAdpter();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageReceived(List<Map<String, Object>> data) {
        childs = data;
        int i = childs.size();
        sharedPreferences = getSharedPreferences( "parent", MODE_PRIVATE );
        sharedPreferences.edit().putInt( "childsum", i ).apply();
        listViewAdpter();
    }

    @Override
    protected void onDestroy() {
        Log.e( "方法：", "onDestroy" );
        EventBus.getDefault().unregister( this );
        super.onDestroy();

    }

}

