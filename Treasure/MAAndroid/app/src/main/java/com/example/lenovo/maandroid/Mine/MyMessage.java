package com.example.lenovo.maandroid.Mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.lenovo.maandroid.R;

import java.util.List;
import java.util.Map;

public class MyMessage extends AppCompatActivity {
    List<Map<String, Object>> datasources = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.my_message );


        ListView listView = findViewById( R.id.list_my_message );
        final MyMessageListViewAdapter adapter = new MyMessageListViewAdapter( this, datasources, R.layout.list_my_message );
        listView.setAdapter( adapter );






        ImageButton return0 = findViewById( R.id.return0 );
        return0.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );
    }
}
