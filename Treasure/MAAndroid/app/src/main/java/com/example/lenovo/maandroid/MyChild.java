package com.example.lenovo.maandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;
import java.util.Map;
import java.util.Timer;

public class MyChild extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        getSupportActionBar().hide();
        setContentView( R.layout.add_child );
        //获取数据库数据---评论数据init();

       /* ListView listView=findViewById( R.id.list_my_child);
        MyChildListViewAdapter adapter=new MyChildListViewAdapter(this,Data.datasources,R.layout.mychild_item);
        listView.setAdapter( adapter);
       //给Item设置点击事件*/

//        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent=new Intent( MyChild.this,AddOrEditChild.class);
//        Log.e("是否打印","是");
//        startActivity( intent );
 //   }
//});
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
}
