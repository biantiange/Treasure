package com.example.lenovo.maandroid.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.lenovo.maandroid.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

public class MyChild extends AppCompatActivity {
    private  List<Map<String, Object>> childs=new ArrayList<>(  );
    private int i=0;
   private ListView listView;
   private MyChildListViewAdapter myChildListViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.add_child );
        EventBus.getDefault().register(this);

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody body = new FormBody.Builder(  ).add( "parentId",Data.parent ).build();
        Request request = new Request.Builder().url( Data.ip + "/mychild/ChildServlet" )
                .post( body ).build();
        final Call call = okHttpClient.newCall( request );
        listView=findViewById( R.id.list_my_child);
        myChildListViewAdapter=new MyChildListViewAdapter(MyChild.this,childs,R.layout.mychild_item);
        listView.setAdapter( myChildListViewAdapter);

        call.enqueue( new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e( "shi","访问失败" );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                i=i+21;
              /*  List<Child> childs = new ArrayList<>();
                childs = new Gson().fromJson( jsonStr,new TypeToken<Child>(){}.getType());*/
                List<Map<String, Object>> data= new Gson().fromJson( jsonStr, new TypeToken<List>() {}.getType());
                childs.addAll( data );
                Log.e( "222",childs.toString() );
                EventBus.getDefault().post(childs);
            }
        } );

       //给Item设置点击事件*/
     ImageButton return0=findViewById( R.id.return0);
        return0.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );
        LinearLayout add_child_a=findViewById( R.id.add_child_a);
        add_child_a.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyChild.this,AddOrEditChild.class);
                startActivity(intent);
            }
        } );
    }
    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onMessageEvent( List<Map<String, Object>> childs){
        myChildListViewAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister( this );
    }

}

