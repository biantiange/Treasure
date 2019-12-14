package com.example.lenovo.maandroid.Mine;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.lenovo.maandroid.R;

public class MyTieZi extends AppCompatActivity {
    private ImageButton return_to_mine;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.mytiezi );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff00bffe );
        }



        findView();
        setAdapter();





    }

    private void setAdapter() {
        MyListener myListener=new MyListener();
        return_to_mine.setOnClickListener( myListener);
    }

    private void findView() {
        return_to_mine=findViewById( R.id.return_to_mine );
    }

    private class MyListener implements View.OnClickListener{


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.return_to_mine:
                    finish();
                    break;
            }
        }
    }
}
