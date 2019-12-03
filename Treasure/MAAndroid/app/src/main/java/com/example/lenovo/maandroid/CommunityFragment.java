package com.example.lenovo.maandroid;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;

import com.example.lenovo.maandroid.Community.Comment;
import com.example.lenovo.maandroid.Community.Parent;
import com.example.lenovo.maandroid.Community.Post;
import com.example.lenovo.maandroid.Community.PostAdapter;
import com.example.lenovo.maandroid.Community.PostImg;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment {
    private List<Post> posts = new ArrayList<>();
    private ListView listView;
    private PostAdapter postAdapter;
    private int isPraise = 0;
    private List<Comment> comments = new ArrayList<>();
    private List<PostImg> imgs = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newView = inflater.inflate(R.layout.community_main, container, false);
        listView = newView.findViewById(R.id.listView_community);

        //假数据
        Parent parent = new Parent(1, "13513028117", "尼古拉斯", "12138", "afas/rgerg/143/");
        Post post = new Post(1, "我于杀戮中绽放，亦如黎明中的花朵", new Timestamp(System.currentTimeMillis()), 250, parent);


        Parent parentC = new Parent(1, "13513028117", "大不列颠", "12138", "afas/rgerg/143/");
        Parent parentR = new Parent(1, "13513028117", "美利坚", "12138", "afas/rgerg/143/");
        Comment comment = new Comment();
        comment.setCommentator(parentC);
        comment.setContent("家里水电费看电视机房的减肥啦里看到什么弗兰克江东父老绝地反击hiu儿女菲拉好多年覅回调符换了卡芙兰朵露");
        comment.setId(3);
        comment.setPostId(1);
        comment.setResComId(0);
        comment.setResponder(parentR);
        comment.setTime(new Timestamp(System.currentTimeMillis()));
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
        //img
        PostImg img1 = new PostImg();
        img1.setId(1);
        img1.setPath("");//图片URL
        img1.setPostId(0);
        img1.setTime(new Timestamp(System.currentTimeMillis()));
        imgs.add(img1);
        imgs.add(img1);
        imgs.add(img1);
        imgs.add(img1);
        post.setIsPraise(1);
        post.setComments(comments);
        post.setImgs(imgs);
        posts.add(post);
        posts.add(post);

        postAdapter = new PostAdapter( posts, getActivity(), R.layout.community_item);

        listView.setAdapter(postAdapter);

        return newView;

    }

}
