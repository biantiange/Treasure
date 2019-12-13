package com.example.lenovo.maandroid;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;

import com.example.lenovo.maandroid.Community.Comment;
import com.example.lenovo.maandroid.Community.Parent;
import com.example.lenovo.maandroid.Community.Post;
import com.example.lenovo.maandroid.Community.PostAdapter;
import com.example.lenovo.maandroid.Community.PostImg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment {
    private List<Post> posts = new ArrayList<>();
    private ListView listView;
    private PostAdapter postAdapter;
    private int isPraise = 0;
    private List<Comment> comments = new ArrayList<>();
    private List<PostImg> imges = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newView = inflater.inflate(R.layout.community_main, container, false);
        listView = newView.findViewById(R.id.listView_community);


//        //假数据
//        Parent parent = new Parent(1, "13513028117", "尼古拉斯", "12138", "afas/rgerg/143/");
//        Post post = new Post(1, "我于杀戮中绽放，亦如黎明中的花朵", new Timestamp(System.currentTimeMillis()), 250, parent);
//
//
//        Parent parentC = new Parent(1, "13513028117", "大不列颠", "12138", "afas/rgerg/143/");
//        Parent parentR = new Parent(1, "13513028117", "美利坚", "12138", "afas/rgerg/143/");
//        Comment comment = new Comment();
//        comment.setCommentator(parentC);
//        comment.setContent("家里水电费看电视机房的减肥啦里看到什么弗兰克江东父老绝地反击hiu儿女菲拉好多年覅回调符换了卡芙兰朵露");
//        comment.setId(3);
//        comment.setPostId(1);
//        comment.setResComId(0);
//        comment.setResponder(parentR);
//        comment.setTime(new Timestamp(System.currentTimeMillis()));
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//        //img
//        PostImg img1 = new PostImg();
//        img1.setId(1);
//        img1.setPath("");//图片URL
//        img1.setPostId(0);
//        img1.setTime(new Timestamp(System.currentTimeMillis()));
//        imges.add(img1);
//        imges.add(img1);
//        imges.add(img1);
//        imges.add(img1);
//        post.setIsPraise(1);
//        post.setComments(comments);
//        post.setImgs(imges);
//        posts.add(post);
//        posts.add(post);
//
//        postAdapter = new PostAdapter( posts, getActivity(), R.layout.community_item);
//
//        listView.setAdapter(postAdapter);
        PostTask task = new PostTask();
        task.execute();
        return newView;

    }

    private class PostTask extends AsyncTask {
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            postAdapter = new PostAdapter(posts, getActivity(), R.layout.community_item);
            listView.setAdapter(postAdapter);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.e("post", "开始");
            try {
                URL url = new URL("http://10.7.88.125:8080/Java/PostListServlet");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                JSONObject User_id = new JSONObject();

                User_id.put("praiserId",1);//发送登录者ID

                OutputStream os = con.getOutputStream();
                os.write(User_id.toString().getBytes());

                InputStream is = con.getInputStream();
                byte[] bs = new byte[255];
                int len;
                StringBuffer sb = new StringBuffer();
                while ((len = is.read(bs)) != -1) {
                    sb.append(new String(bs, 0, len));
                }
                String get = new String(sb);
                JSONArray getArray = new JSONArray(get);
                for (int i = 0; i < getArray.length(); i++) {
                    JSONObject object = getArray.getJSONObject(i);
                    Post post = new Post();
                    post.setId(object.getInt("id"));
                    post.setContent(object.getString("content"));
                    post.setTime(Timestamp.valueOf(object.getString("time")));
                    post.setPraiseCount(object.getInt("praiseCount"));
                    //发帖人
                    Parent poster = new Parent();
                    poster.setHeaderPath(object.getString("headerPath"));
                    poster.setNickName(object.getString("nickName"));
                    post.setParent(poster);
                    //imgs
                    JSONArray imgs = object.getJSONArray("imgs");
                    for (int j = 0; j < imgs.length(); j++) {
                            JSONObject img = getArray.getJSONObject(j);
                            PostImg image = new PostImg();

                            image.setPostId(img.getInt("postId"));
                            image.setTime(Timestamp.valueOf(img.getString("Pimg_time")));
                            image.setId(img.getInt("Pimg_id"));
                            image.setPath(img.getString("path"));
                        imges.add(image);
                    }
                    post.setImgs(imges);
                    //3_comment
                    JSONArray comment_3 = object.getJSONArray("comments");
                    for (int k = 0; k < comment_3.length(); k++) {
                        JSONObject comment = getArray.getJSONObject(k);
                        Comment comment1 = new Comment();
                        comment1.setContent(comment.getString("commentContent"));
                        Parent commentator = new Parent();
                        commentator.setNickName(comment.getString("commentatorName"));
                        comment1.setCommentator(commentator);
                        comments.add(comment1);
                    }
                    //isPraise
                    post.setIsPraise(object.getInt("isPraise"));
                    posts.add(post);

                }

                is.close();
                os.close();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return posts;
        }
    }


}
