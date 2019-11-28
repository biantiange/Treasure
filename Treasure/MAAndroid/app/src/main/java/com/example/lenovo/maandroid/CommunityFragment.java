package com.example.lenovo.maandroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.lenovo.maandroid.Community.Parent;
import com.example.lenovo.maandroid.Community.Post;
import com.example.lenovo.maandroid.Community.postAdapter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommunityFragment extends Fragment {
    private List<Post> posts = new ArrayList<>();
    private ListView listView;
    private postAdapter postAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newView = inflater.inflate(R.layout.community_main,container,false);
        listView  = newView.findViewById(R.id.listView_community);

        Parent parent = new Parent(1,"13513028117","尼古拉斯","12138","afas/rgerg/143/");
        Post post = new Post(1,"我于杀戮中绽放，亦如黎明中的花朵",new Timestamp(System.currentTimeMillis()),250,parent);
        posts.add(post);
        postAdapter = new postAdapter(posts,getActivity(),R.layout.community_item);
        listView.setAdapter(postAdapter);

        return newView;

    }
}
