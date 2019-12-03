package com.example.lenovo.maandroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyMessage extends AppCompatActivity {
    List<Map<String,Object>> datasources=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        getSupportActionBar().hide();
        setContentView( R.layout.my_message );
        //获取数据库数据---评论数据init();
        //静态数据
        int[] url={R.drawable.aaa,R.drawable.aaa};
        String[] username={"羊羊","哈哈"};
        String[] usertome={"asda","???啊实打实大苏打微软微软微软感到快乐建立，处女膜婢女明白你，从v那边，名称v吗，不能"};
        String[] time={"昨天12:23","前天21:43"};
        String[] commen={"as啊十大十大科技宣布辞职女女秩序册按月给直接回车v只能下次v那种阿松大结婚后让他们联发科讨好婆婆皮卡吧","a阿松大威威认为v"};
      datasources=new ArrayList<>(  );
       for(int i=0;i<username.length;i++){
           Map<String,Object> map=new HashMap<>(  );
           map.put( "comments_user_img",url[i] );
           map.put("comment_user_name",username[i]);
           map.put("comment_time",time[i]);
           map.put("comment_to_me",usertome[i]);
           map.put( "my_comments",commen[i] );
           datasources.add(map);

       }
        ListView listView=findViewById( R.id.list_my_message);
       final MyMessageListViewAdapter adapter=new MyMessageListViewAdapter(this,datasources,R.layout.list_my_message);
listView.setAdapter( adapter);

     ImageButton return0=findViewById( R.id.return0);
        return0.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );
    }
}
