package com.example.lenovo.maandroid.Community;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lenovo.maandroid.R;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {
    private int isPraise = 0;
    private ListView listView;
    private CommentAdapter adapter;
    private List<Comment> comments = new ArrayList<>();
    private List<PostImg> imgs = new ArrayList<>();
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        listView = findViewById(R.id.comment_listView);
        Intent intent = getIntent();
        post = (Post)intent.getSerializableExtra("post");
        isPraise = intent.getIntExtra("isPraise",0);
        for (int i = 0;i<intent.getIntExtra("imgSize",0);i++){
            PostImg img = (PostImg) intent.getSerializableExtra("img"+i);
            imgs.add(img);
        }



        //img
        ImageView img1 = findViewById(R.id.im1);
        ImageView img2 = findViewById(R.id.im2);
        ImageView img3 = findViewById(R.id.im3);
        ImageView img4 = findViewById(R.id.im4);
        ImageView img5 = findViewById(R.id.im5);
        ImageView img6 = findViewById(R.id.im6);

        Log.e("images.size",""+imgs.size());

        if (imgs.size() == 0){
            img1.setVisibility(View.GONE);
            img2.setVisibility(View.GONE);
            img3.setVisibility(View.GONE);
            img4.setVisibility(View.GONE);
            img5.setVisibility(View.GONE);
            img6.setVisibility(View.GONE);
        }else if(imgs.size() == 1){
            Glide.with(this).load(imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img1);
            img2.setVisibility(View.GONE);
            img3.setVisibility(View.GONE);
            img4.setVisibility(View.GONE);
            img5.setVisibility(View.GONE);
            img6.setVisibility(View.GONE);
        }else  if (imgs.size() == 2){
            Glide.with(this).load(imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img1);
            Glide.with(this).load(imgs.get(1).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img2);
            img3.setVisibility(View.GONE);
            img4.setVisibility(View.GONE);
            img5.setVisibility(View.GONE);
            img6.setVisibility(View.GONE);
        }else if(imgs.size() == 3){
            Glide.with(this).load(imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img1);
            Glide.with(this).load(imgs.get(1).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img2);
            Glide.with(this).load(imgs.get(2).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img3);
            img4.setVisibility(View.GONE);
            img5.setVisibility(View.GONE);
            img6.setVisibility(View.GONE);
        }else if(imgs.size() == 4){
            Glide.with(this).load(imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img1);
            Glide.with(this).load(imgs.get(1).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img2);
            Glide.with(this).load(imgs.get(2).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img3);
            Glide.with(this).load(imgs.get(3).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img4);
            img5.setVisibility(View.GONE);
            img6.setVisibility(View.GONE);
        }else if(imgs.size() == 5){
            Glide.with(this).load(imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img1);
            Glide.with(this).load(imgs.get(1).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img2);
            Glide.with(this).load(imgs.get(2).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img3);
            Glide.with(this).load(imgs.get(3).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img4);
            Glide.with(this).load(imgs.get(4).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img5);
            img6.setVisibility(View.GONE);
        }else {
            Glide.with(this).load(imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img1);
            Glide.with(this).load(imgs.get(1).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img2);
            Glide.with(this).load(imgs.get(2).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img3);
            Glide.with(this).load(imgs.get(3).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img4);
            Glide.with(this).load(imgs.get(4).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img5);
            Glide.with(this).load(imgs.get(5).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img6);
        }

//评论
        Button button = findViewById(R.id.community_commenting);
        final InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(PostDetailActivity.this, R.style.dialog_center);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputTextMsgDialog.show();
                inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                    @Override
                    public void onTextSend(String msg) {
                        //获得输入框中的文字（点击发送之后回调）

                    }
                });
            }
        });


        TextView name = findViewById(R.id.community_detail_parent_name);
        name.setText(post.getParent().getNickName());
        TextView content = findViewById(R.id.community_detail_content);
        content.setText(post.getContent());
        TextView time = findViewById(R.id.community_detail_time);
        time.setText(post.getTime().toString());
        final TextView praiseNum = findViewById(R.id.community_detail_praiseNum);
        praiseNum.setText(post.getPraiseCount()+"");
        final ImageView praising = findViewById(R.id.community_detail_praising);//点赞
        if (isPraise > 0){
            praising.setImageResource(R.drawable.dianzaned);
        }
        praising.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPraise == 0) {
                    praiseNum.setText(post.getPraiseCount()+1 + "");
                    praising.setImageResource(R.drawable.dianzaned);
                    isPraise++;
                    //数据库
                    PraiseTask task = new PraiseTask();
                    task.execute();
                }
            }
        });

        //假数据
        Parent parentC = new Parent(1,"13513028117","大不列颠","12138","afas/rgerg/143/");
        Parent parentR = new Parent(1,"13513028117","美利坚","12138","afas/rgerg/143/");
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
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);

        adapter = new CommentAdapter(comments,this,R.layout.comment_item);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputTextMsgDialog inputTextMsgDialog1 = new InputTextMsgDialog(PostDetailActivity.this,R.style.dialog_center);
                inputTextMsgDialog1.setHint("回复 "+comments.get(position).getCommentator().getNickName()+" :");
                inputTextMsgDialog1.show();
                inputTextMsgDialog1.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                    @Override
                    public void onTextSend(String msg) {
                        Toast.makeText(PostDetailActivity.this,msg,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private class PraiseTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try{
                URL url = new URL("http://10.7.88.125:8080/Java/PraiseAddServlet");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                JSONObject User_id = new JSONObject();

                User_id.put("praiserId",1);//发送登录者ID
                User_id.put("postId",post.getId());

                OutputStream os = con.getOutputStream();
                os.write(User_id.toString().getBytes());
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
