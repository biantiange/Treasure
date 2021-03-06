package com.example.lenovo.maandroid.Community;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lenovo.maandroid.Entity.Comment;
import com.example.lenovo.maandroid.Entity.Parent;
import com.example.lenovo.maandroid.Entity.Post;
import com.example.lenovo.maandroid.Entity.PostImg;
import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Utils.Constant;
import com.example.lenovo.maandroid.Utils.Data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
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
    private SharedPreferences sharedPreferences;
    private int parentId;
    private RequestOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8 );
        }

        listView = findViewById(R.id.comment_listView);
        Intent intent = getIntent();
        post = (Post) intent.getSerializableExtra("post");
        isPraise = intent.getIntExtra("isPraise", 0);
        for (int i = 0; i < intent.getIntExtra("imgSize", 0); i++) {
            PostImg img = (PostImg) intent.getSerializableExtra("img" + i);
            imgs.add(img);
        }
        //返回
        ImageView back = findViewById(R.id.detail_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //parentId
        sharedPreferences = PostDetailActivity.this.getSharedPreferences("parent", Context.MODE_PRIVATE);
        parentId = sharedPreferences.getInt("parentId", 0);

        //头像
        ImageView header = findViewById(R.id.community_detail_parent_header);
        options = new RequestOptions()
                .circleCrop()
                .placeholder( R.drawable.aaa)
                .error( R.drawable.aaa)
                .fallback( R.drawable.aaa);
        Glide.with(PostDetailActivity.this).load( Data.ip+"childImg/"+post.getParent().getHeaderPath()).apply( options).into(header);

        //img
        ImageView img1 = findViewById(R.id.im1);
        ImageView img2 = findViewById(R.id.im2);
        ImageView img3 = findViewById(R.id.im3);
        ImageView img4 = findViewById(R.id.im4);
        ImageView img5 = findViewById(R.id.im5);
        ImageView img6 = findViewById(R.id.im6);


        if (imgs.size() == 0) {
            img1.setVisibility(View.GONE);
            img2.setVisibility(View.GONE);
            img3.setVisibility(View.GONE);
            img4.setVisibility(View.GONE);
            img5.setVisibility(View.GONE);
            img6.setVisibility(View.GONE);
        } else if (imgs.size() == 1) {
            img1.setVisibility(View.VISIBLE);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img1);
            img2.setVisibility(View.GONE);
            img3.setVisibility(View.GONE);
            img4.setVisibility(View.GONE);
            img5.setVisibility(View.GONE);
            img6.setVisibility(View.GONE);
        } else if (imgs.size() == 2) {
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.VISIBLE);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img1);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(1).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img2);
            img3.setVisibility(View.GONE);
            img4.setVisibility(View.GONE);
            img5.setVisibility(View.GONE);
            img6.setVisibility(View.GONE);
        } else if (imgs.size() == 3) {
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.VISIBLE);
            img3.setVisibility(View.VISIBLE);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img1);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(1).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img2);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(2).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img3);
            img4.setVisibility(View.GONE);
            img5.setVisibility(View.GONE);
            img6.setVisibility(View.GONE);
        } else if (imgs.size() == 4) {
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.VISIBLE);
            img3.setVisibility(View.VISIBLE);
            img4.setVisibility(View.VISIBLE);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img1);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(1).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img2);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(2).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img3);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(3).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img4);
            img5.setVisibility(View.GONE);
            img6.setVisibility(View.GONE);
        } else if (imgs.size() == 5) {
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.VISIBLE);
            img3.setVisibility(View.VISIBLE);
            img4.setVisibility(View.VISIBLE);
            img5.setVisibility(View.VISIBLE);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img1);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(1).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img2);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(2).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img3);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(3).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img4);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(4).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img5);
            img6.setVisibility(View.GONE);
        } else {
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.VISIBLE);
            img3.setVisibility(View.VISIBLE);
            img4.setVisibility(View.VISIBLE);
            img5.setVisibility(View.VISIBLE);
            img6.setVisibility(View.VISIBLE);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img1);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(1).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img2);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(2).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img3);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(3).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img4);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(4).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img5);
            Glide.with(this).load(Constant.POST_IMG_BASE_IP+imgs.get(5).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img6);
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
                        AddCommentTask task = new AddCommentTask(msg);
                        task.execute();
                    }
                });
            }
        });


        TextView name = findViewById(R.id.community_detail_parent_name);
        name.setText(post.getParent().getNickName());
        TextView content = findViewById(R.id.community_detail_content);
        content.setText(post.getContent());
        TextView time = findViewById(R.id.community_detail_time);
        String time1 = post.getTime().toString().substring( 0,16 );
        time.setText(time1);
        final TextView praiseNum = findViewById(R.id.community_detail_praiseNum);
        praiseNum.setText(post.getPraiseCount() + "");
        final ImageView praising = findViewById(R.id.community_detail_praising);//点赞
        if (isPraise > 0) {
            praising.setImageResource(R.drawable.dianzaned);
        }
        praising.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPraise == 0) {
                    praiseNum.setText(post.getPraiseCount() + 1 + "");
                    praising.setImageResource(R.drawable.dianzaned);
                    isPraise++;
                    //数据库
                    PraiseTask task2 = new PraiseTask();
                    task2.execute();
                }
            }
        });

        CommentTask task = new CommentTask();
        task.execute();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                InputTextMsgDialog inputTextMsgDialog1 = new InputTextMsgDialog(PostDetailActivity.this, R.style.dialog_center);
                inputTextMsgDialog1.setHint("回复 " + comments.get(position).getCommentator().getNickName() + " :");
                inputTextMsgDialog1.show();
                inputTextMsgDialog1.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                    @Override
                    public void onTextSend(String msg) {
                        //回复评论
                        ResCommentTask task1 = new ResCommentTask(position, msg);
                        task1.execute();

                    }
                });
            }
        });
    }

    private class PraiseTask extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] objects) {
            try{
                Log.e("praise","开始praising");
                URL url = new URL("http://"+ getString(R.string.ip) +":8080/Java/PraiseAddServlet");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();


                con.setRequestMethod("POST");
                JSONObject User_id = new JSONObject();

                User_id.put("praiserId",parentId);//发送登录者ID
                User_id.put("postId",post.getId());
                User_id.put("praiseCount",post.getPraiseCount()+1);

                OutputStream os = con.getOutputStream();
                os.write(User_id.toString().getBytes());
                InputStream is = con.getInputStream();
                is.close();
                os.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private class CommentTask extends AsyncTask {
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            adapter = new CommentAdapter(comments, PostDetailActivity.this, R.layout.comment_item);
            listView.setAdapter(adapter);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                comments.clear();
                URL url = new URL("http://"+getString(R.string.ip)+":8080/Java/CommentListServlet");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                JSONObject User_id = new JSONObject();
                User_id.put("postId", post.getId());

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
                    Comment comment = new Comment();
                    comment.setId(object.getInt("id"));
                    comment.setContent(object.getString("content"));
                    comment.setTime(Timestamp.valueOf(object.getString("time")));
                    //commentator
                    Parent commentator = new Parent();
                    commentator.setId(object.getInt("commentatorId"));
                    commentator.setNickName(object.optString("nickName_c"));
                    commentator.setHeaderPath(object.getString("headerPath_c"));
                    comment.setCommentator(commentator);
                    //responderId
                    Parent responder = new Parent();
//                    responder.setHeaderPath(object.getString("headerPath_r"));
                    responder.setNickName(object.optString("nickName_r"));
                    comment.setResponder(responder);
                    //resCom
                    Comment resCom = new Comment();
                    resCom.setContent(object.optString("resComment_content"));
                    comment.setResCom(resCom);
                    comments.add(comment);
                }

                is.close();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return comments;
        }
    }

    private class ResCommentTask extends AsyncTask {
        private int position;
        private String content;

        public ResCommentTask(int position, String content) {
            this.position = position;
            this.content = content;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Toast.makeText(PostDetailActivity.this, "回复成功！", Toast.LENGTH_SHORT).show();
            CommentTask task = new CommentTask();
            task.execute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                URL url = new URL("http://"+getString(R.string.ip)+":8080/Java/CommentResServlet");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("postId", post.getId());
                jsonObject.put("commentatorId", parentId);//登录者ID
                jsonObject.put("resComId", comments.get(position).getId());

                jsonObject.put("responderId", comments.get(position).getCommentator().getId());

                jsonObject.put("content", content);
                jsonObject.put("time", new Timestamp(System.currentTimeMillis()));

                OutputStream os = con.getOutputStream();
                os.write(jsonObject.toString().getBytes());

                InputStream is = con.getInputStream();

                is.close();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return comments;
        }
    }

    private class AddCommentTask extends AsyncTask {
        private String content;

        public AddCommentTask(String content) {
            this.content = content;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Toast.makeText(PostDetailActivity.this, "评论成功！", Toast.LENGTH_SHORT).show();
            CommentTask task = new CommentTask();
            task.execute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                URL url = new URL("http://"+getString(R.string.ip)+":8080/Java/CommentAddServlet");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("postId", post.getId());
                jsonObject.put("commentatorId", parentId);//登录者ID
                jsonObject.put("responderId",post.getParent().getId());
                jsonObject.put("content", content);
                jsonObject.put("time", new Timestamp(System.currentTimeMillis()));


                OutputStream os = con.getOutputStream();
                os.write(jsonObject.toString().getBytes());

                InputStream is = con.getInputStream();

                is.close();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
