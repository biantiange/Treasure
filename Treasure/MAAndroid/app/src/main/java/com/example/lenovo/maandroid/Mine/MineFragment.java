package com.example.lenovo.maandroid.Mine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Utils.Data;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class MineFragment extends Fragment {
    private ImageView user_img;
    private TextView username;
    private TextView user_id;
    private ImageButton edit;
    private LinearLayout message_t;
    private LinearLayout my_tiezi;
    private LinearLayout mychild;
    private SharedPreferences sharedPreferences;
    private List<Map<String, Object>> data;
    private Map<String,Object> map;
    private String url;
    private String name;
    private Handler mHandler=new Handler();
    private OkHttpClient okHttpClient;
    private String phoneNumber;
    private RequestOptions options;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newView = inflater.inflate( R.layout.mine_fragment, container, false );
        return newView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        options=new RequestOptions().circleCrop().placeholder( R.drawable.aaa ).error( R.drawable.aaa ).fallback( R.drawable.aaa );
        findView();
        init();
        initData();
        setAdapter();
        //在主页的class中从数据库中获取并创建本地文件夹将信息存入其中
        //在这里获取，头像，username，userimg等

        //提交到数据库



    }

    private void initData() {
        url=sharedPreferences.getString( "headerPath","" );
        name=sharedPreferences.getString( "nickName","" );
        user_id.setText( "乖号："+phoneNumber );
        username.setText(name);
        Glide.with( this ).load( url ).apply( options ).into( user_img);


    }

    private void init() {
        sharedPreferences =getContext().getSharedPreferences("parent",MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("phoneNumber","");
        okHttpClient=new OkHttpClient();

        if(phoneNumber.length()>0&&sharedPreferences!=null){
            FormBody body=new FormBody.Builder().add( "phoneNumber",phoneNumber ).build();
            Request request=new Request.Builder().url( Data.ip+"/mychild/FindUserServlet" ).post( body ).build();
            Call call=okHttpClient.newCall( request );
            call.enqueue( new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonStr = response.body().string();
                    data = new Gson().fromJson( jsonStr, new TypeToken<List>() {
                    }.getType() );
                        sharedPreferences.edit()
                                .putString( "headerPath", data.get( 0 ).get( "headerPath" ).toString() )
                                .putString( "nickName", data.get( 0 ).get( "nickName" ).toString() ).apply();

                }
            } );

        }



    }


    private void setAdapter() {
        MyListener myListener = new MyListener();
        message_t.setOnClickListener( myListener );
        edit.setOnClickListener( myListener );
        mychild.setOnClickListener( myListener );

    }
    //圆形头像
    private void glide_w() {
        RequestOptions requestOptions = new RequestOptions().circleCrop().placeholder( R.drawable.aaa).error( R.drawable.aaa ).fallback( R.drawable.aaa );
        Glide.with( this ).load(url).apply( requestOptions ).into( user_img );
    }
    //点击事件
    private void findView() {
        user_img = getActivity().findViewById( R.id.user_img );
        username = getActivity().findViewById( R.id.username );
        user_id = getActivity().findViewById( R.id.user_id );
        edit = getActivity().findViewById( R.id.edit_m );
        message_t = getActivity().findViewById( R.id.message_t );
        my_tiezi = getActivity().findViewById( R.id.my_tiezi );
        mychild = getActivity().findViewById( R.id.add_child1 );



    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.message_t:
                    Intent intent = new Intent( getActivity(), MyMessage.class );
                    startActivity( intent );
                    break;
                case R.id.edit_m:
                    Intent intent1 = new Intent( getActivity(), Edit.class );
                    startActivity( intent1 );
                    break;
                case R.id.add_child1:
                    Intent intent2=new Intent( getActivity(),MyChild.class);
                    startActivity( intent2 );
                    break;



            }

        }
    }
}