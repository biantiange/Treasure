package com.example.lenovo.maandroid.Mine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
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

import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;

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
    private Map<String, Object> map;
    private String url;
    private String name;
    private OkHttpClient okHttpClient;
    private String phoneNumber;
    private RequestOptions options;
    private int parentId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newView = inflater.inflate( R.layout.mine_fragment, container, false );
        return newView;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        options = new RequestOptions().circleCrop().placeholder( R.drawable.aaa ).error( R.drawable.aaa ).fallback( R.drawable.aaa );
        sharedPreferences=getContext().getSharedPreferences( "parent",MODE_PRIVATE);
        parentId=sharedPreferences.getInt( "parentId",0 );
        findView();
        init();
        initData();
        setAdapter();
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance( getActivity() );
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction( "android.intent.action.CART_BROADCAST" );
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra( "data" );
                if ("refresh".equals( msg )) {
                    refresh();
                }
            }

            private void refresh() {
                Log.e("方法：","refresh");
                findView();
                init();
                initData();
            }
        };
        broadcastManager.registerReceiver( mItemViewListClickReceiver, intentFilter );

    }

    private void initData() {
        url = sharedPreferences.getString( "imgPath", "" );
        name = sharedPreferences.getString( "nickName", "" );
        user_id.setText( "乖号：" + phoneNumber );
        username.setText( name );
        Log.e( "nickname", name );
        Glide.with( this ).load( Data.url+url ).apply( options ).into( user_img );
    }

    private void init() {

        phoneNumber = sharedPreferences.getString( "phoneNumber", "" );
    }


    private void setAdapter() {
        MyListener myListener = new MyListener();
        message_t.setOnClickListener( myListener );
        edit.setOnClickListener( myListener );
        mychild.setOnClickListener( myListener );
        my_tiezi.setOnClickListener( myListener );

    }

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
                    Intent intent2 = new Intent( getActivity(), MyChild.class );
                    startActivity( intent2 );
                    break;
                case R.id.my_tiezi:
                    Intent intent3 = new Intent( getActivity(), MyTieZi.class );
                    startActivity( intent3 );
                    break;



            }

        }
    }
}