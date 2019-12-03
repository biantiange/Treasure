package com.example.lenovo.maandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class MineFragment extends Fragment {
    private ImageView user_img;
    private TextView username;
    private TextView user_id;
    private ImageButton edit;
    private LinearLayout message_t;
    private LinearLayout my_tiezi;
    private LinearLayout mychild;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newView = inflater.inflate( R.layout.mine_fragment, container, false );
        return newView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        findView();
        glide_w();
        setAdapter();

    }

    private void setAdapter() {
        MyListener myListener = new MyListener();
        message_t.setOnClickListener( myListener );
        edit.setOnClickListener( myListener );
        mychild.setOnClickListener( myListener );

    }

    //圆形头像
    private void glide_w() {
        RequestOptions requestOptions = new RequestOptions().circleCrop();
        Glide.with( this ).load( R.drawable.aaa ).apply( requestOptions ).into( user_img );
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
                    Intent intent1 = new Intent( getActivity(), Edit_m.class );
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