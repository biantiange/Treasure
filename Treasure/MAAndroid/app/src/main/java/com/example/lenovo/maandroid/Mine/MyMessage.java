package com.example.lenovo.maandroid.Mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.lenovo.maandroid.Entity.Discuss;
import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Utils.Constant;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyMessage extends AppCompatActivity {
    List<Discuss> datasources=null;
    private OkHttpClient okHttpClient;
    private  MyMessageListViewAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.my_message );
        okHttpClient=new OkHttpClient();
        ListView listView=findViewById( R.id.list_my_message);
//        adapter=new MyMessageListViewAdapter(this,datasources, R.layout.list_my_message);
//        listView.setAdapter( adapter);
        init();
      //  adapter.notifyDataSetChanged();
        ImageButton return0=findViewById( R.id.return0);
        return0.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );
    }

    private void init() {
        Log.e("111","111");
        Request request=new Request.Builder().url(Constant.URL+"MessageServlet?ostId=123").build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("222","222");
                Log.e("插入结果",response.body().string());
//                String stuListStr= response.body().string();
//                Type type= new TypeToken<List<Discuss>>(){}.getType();
//                datasources.addAll((List<Discuss>)new Gson().fromJson(stuListStr,type));
               // adapter.notifyDataSetChanged();
            }
        });

    }
}
