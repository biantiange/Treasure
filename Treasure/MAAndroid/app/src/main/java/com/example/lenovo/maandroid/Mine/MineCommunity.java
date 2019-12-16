package com.example.lenovo.maandroid.Mine;


import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.example.lenovo.maandroid.Community.PostAdapter;

import com.example.lenovo.maandroid.Entity.Comment;
import com.example.lenovo.maandroid.Entity.Parent;
import com.example.lenovo.maandroid.Entity.Post;
import com.example.lenovo.maandroid.Entity.PostImg;
import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Utils.Data;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MineCommunity extends AppCompatActivity {
    private List<Post> posts = new ArrayList<>();
    private ListView listView;
    private MinePostAdapter postAdapter;
    private SharedPreferences sharedPreferences;
    private int parentId;
    private SmartRefreshLayout refreshLayout;
    private static final int REFRESH_FINISH = 1;
    private OkHttpClient okHttpClient;
    private String jsonStr;
    private ImageButton return2;
    private Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REFRESH_FINISH:
                    //结束刷新动画
                    initData();
                    setAdapter();
                    refreshLayout.finishRefresh();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.mine_community );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff00bffe );
        }
        okHttpClient=new OkHttpClient();
        sharedPreferences=getSharedPreferences( "parent",MODE_PRIVATE );
        parentId=sharedPreferences.getInt( "parentId",0 );
        listView =findViewById(R.id.listView_community);
        return2=findViewById( R.id.return2 );
        //返回按钮
        return2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );
        refreshLayout=findViewById( R.id.smart_layout );
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
        EventBus.getDefault().register( this );
        initData();
        setAdapter();
    }

    private void setAdapter() {
        Log.e( "数据",posts.toString());
        postAdapter = new MinePostAdapter(posts,MineCommunity.this, R.layout.community_item);
        listView.setAdapter(postAdapter);


    }
    private void jiexi(){
        posts.clear();
        try {
            JSONArray getArray = new JSONArray(jsonStr);
            for (int i = 0; i < getArray.length(); i++) {
                JSONObject object = getArray.getJSONObject(i);
                Post post = new Post();
                post.setId(object.getInt("id"));
                post.setContent(object.getString("content"));
                post.setTime( Timestamp.valueOf(object.getString("time")));
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
                        java.util.Date date = null;
                        try {
                            date = format1.parse(fillTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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

        }  catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void initData() {
        Log.e( "方法：","initdata");
        FormBody body = new FormBody.Builder().add( "praiserId",parentId+"" ).build();
        Request request = new Request.Builder().url( Data.ip+"MinePostListServlet" )
                .post( body ).build();
        final Call call = okHttpClient.newCall( request );
        call.enqueue( new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText( MineCommunity.this, "网络连接失败,无法获取数据。。。", Toast.LENGTH_SHORT ).show();
                finish();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                jsonStr = response.body().string();
                Log.e( "s",jsonStr );
                EventBus.getDefault().post( jsonStr );

            }
        } );



    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageReceived(String s) {
        Log.e( "方法：","onmessage");
        jiexi();
        setAdapter();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
