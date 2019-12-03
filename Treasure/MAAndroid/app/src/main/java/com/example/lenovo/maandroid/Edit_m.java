package com.example.lenovo.maandroid;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Edit_m extends AppCompatActivity {
    private ImageButton return1;//返回按钮
    private Button submit0;//修改完成后提交
    private Button selectphoto;//选择头像
    private ImageView user_img;//用户头像
    private EditText username;//用户更改的信息

    private  SharedPreferences usermessage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        getSupportActionBar().hide();
        setContentView( R.layout.edit_m);

        user_img=findViewById( R.id.edit_user_img);
        username=findViewById( R.id.edit_username);
        selectphoto=findViewById( R.id.edit_btn_img );
        return1=findViewById( R.id.return0 );
        submit0=findViewById( R.id.edit_submit);

        MyListener myListener=new MyListener();
        selectphoto.setOnClickListener( myListener );
        return1.setOnClickListener( myListener );
        submit0.setOnClickListener( myListener);


        //获取头像加载到user_img
        /*查找sharepeference
        *
        * */






    }

    private class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.return0:
                    finish();
                    break;
                case R.id.edit_submit:
                    //获取更改的信息,提交到数据库

                    String username0=username.getText().toString();
                    //先保存到SharedPreferences
                    if(username0.length()>0&&username0.length()<8)//数据可以进行更改
                    {
                       /* SharedPreferences.Editor editor = usermessage.edit();
                        editor.putString("username",username0);
                        editor.putInt( "userIma",R.drawable.aaa);*/
                       finish();


                    }
                    else{
                        Toast.makeText( Edit_m.this,"昵称格式错误,不能为空且小于8个字",Toast.LENGTH_SHORT ).show();
                    }
                    //提交到数据库



                    break;

                case R.id.edit_btn_img:
                    //选择照片

                    break;










            }



        }
    }
}
