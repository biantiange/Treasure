package com.example.lenovo.maandroid.Community;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.lenovo.maandroid.Entity.Comment;
import com.example.lenovo.maandroid.Entity.Parent;
import com.example.lenovo.maandroid.Entity.Post;
import com.example.lenovo.maandroid.Entity.PostImg;
import com.example.lenovo.maandroid.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment {
    private List<Post> posts = new ArrayList<>();
    private ListView listView;
    private PostAdapter postAdapter;
    private SmartRefreshLayout refreshLayout;
    private static final int REFRESH_FINISH = 1;
    private int pageNum = 1;

    private Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REFRESH_FINISH:

                    //修改数据源

                    PostTask task = new PostTask();
                    task.execute();
                    pageNum = 1;
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newView = inflater.inflate(R.layout.community_main, container, false);
        listView = newView.findViewById(R.id.listView_community);
        refreshLayout = newView.findViewById(R.id.smart_layout);

        PostTask task = new PostTask();
        task.execute();
        setListeners();
        return newView;

    }

    private void setListeners(){
        //监听下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //不能执行网络操作，需要使用多线程
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //向主线程发送消息，更新视图
                        Message msg = new Message();
                        msg.what = REFRESH_FINISH;
                        mainHandler.sendMessage(msg);
                    }
                }.start();

            }
        });

        //监听上拉加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                PostAddTask task = new PostAddTask(pageNum);
                pageNum++;
                task.execute();
            }
        });
    }

    private class PostTask extends AsyncTask {
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            postAdapter = new PostAdapter(posts, getActivity(), R.layout.community_item);
            listView.setAdapter(postAdapter);
            refreshLayout.finishRefresh();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.e("post", "开始"+getString(R.string.ip));
            try {
                URL url = new URL("http://"+getString(R.string.ip)+":8080/Java/PostListServlet");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                JSONObject User_id = new JSONObject();

                User_id.put("praiserId", 1);//发送登录者ID

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
                posts.clear();
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
                    poster.setId(object.getInt("Poster_id"));
                    poster.setHeaderPath(object.getString("headerPath"));
                    poster.setNickName(object.getString("nickName"));
                    post.setParent(poster);
                    //imgs
                    JSONArray imgs = object.getJSONArray("imgs");

                    List<PostImg> imges = new ArrayList<>();
                    for (int j = 0; j < imgs.length(); j++) {
                        JSONObject img = imgs.getJSONObject(j);
                        PostImg image = new PostImg();
                        image.setPostId(img.optInt("postId" + j));
                        String fillTime = img.getString("Pimg_time" + j);

                        if (fillTime != null && !(fillTime.equals(""))) {
                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            java.util.Date date = format1.parse(fillTime);
                            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String sdate = format2.format(date);
                            Timestamp fTimestamp = Timestamp.valueOf(sdate);
                            image.setTime(fTimestamp);
                        }
                        image.setId(img.optInt("Pimg_id" + j));
                        image.setPath(img.optString("path" + j));

                        imges.add(image);
                    }
                    post.setImgs(imges);
                    //3_comment
                    JSONArray comment_3 = object.getJSONArray("comments");
                    List<Comment> comments = new ArrayList<>();
                    for (int k = 0; k < comment_3.length(); k++) {
                        JSONObject comment = comment_3.getJSONObject(k);
                        Comment comment1 = new Comment();
                        comment1.setContent(comment.optString("commentContent" + k));
                        Parent commentator = new Parent();
                        commentator.setNickName(comment.optString("commentatorName" + k));
                        comment1.setCommentator(commentator);
                        comments.add(comment1);
                    }
                    post.setComments(comments);
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
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return posts;
        }
    }

    private class PostAddTask extends AsyncTask {
        private int page;

        public PostAddTask(int page) {
            this.page = page;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            postAdapter.notifyDataSetChanged();
            refreshLayout.finishLoadMore();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.e("post", "开始"+getString(R.string.ip));
            try {
                URL url = new URL("http://"+getString(R.string.ip)+":8080/Java/PostAddListServlet");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                JSONObject User_id = new JSONObject();

                User_id.put("praiserId", 1);//发送登录者ID
                User_id.put("page",page);

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
                    poster.setId(object.getInt("Poster_id"));
                    poster.setHeaderPath(object.getString("headerPath"));
                    poster.setNickName(object.getString("nickName"));
                    post.setParent(poster);
                    //imgs
                    JSONArray imgs = object.getJSONArray("imgs");

                    List<PostImg> imges = new ArrayList<>();
                    for (int j = 0; j < imgs.length(); j++) {
                        JSONObject img = imgs.getJSONObject(j);
                        PostImg image = new PostImg();
                        image.setPostId(img.optInt("postId" + j));
                        String fillTime = img.getString("Pimg_time" + j);

                        if (fillTime != null && !(fillTime.equals(""))) {
                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            java.util.Date date = format1.parse(fillTime);
                            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String sdate = format2.format(date);
                            Timestamp fTimestamp = Timestamp.valueOf(sdate);
                            image.setTime(fTimestamp);
                        }
                        image.setId(img.optInt("Pimg_id" + j));
                        image.setPath(img.optString("path" + j));

                        imges.add(image);
                    }
                    post.setImgs(imges);
                    //3_comment
                    JSONArray comment_3 = object.getJSONArray("comments");
                    List<Comment> comments = new ArrayList<>();
                    for (int k = 0; k < comment_3.length(); k++) {
                        JSONObject comment = comment_3.getJSONObject(k);
                        Comment comment1 = new Comment();
                        comment1.setContent(comment.optString("commentContent" + k));
                        Parent commentator = new Parent();
                        commentator.setNickName(comment.optString("commentatorName" + k));
                        comment1.setCommentator(commentator);
                        comments.add(comment1);
                    }
                    post.setComments(comments);
                    //isPraise
                    post.setIsPraise(object.getInt("isPraise"));
                    postAdapter.addPost(post);
                }

                is.close();
                os.close();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
