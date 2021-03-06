package com.example.lenovo.maandroid.Mine;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.example.lenovo.maandroid.Community.InputTextMsgDialog;

import com.example.lenovo.maandroid.Community.PostDetailActivity;

import com.example.lenovo.maandroid.Entity.Comment;
import com.example.lenovo.maandroid.Entity.Post;
import com.example.lenovo.maandroid.Entity.PostImg;
import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Utils.Data;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;


public class MinePostAdapter extends BaseAdapter {
    private List<Post> posts;
    private Context context;
    private int item_id;
    private List<PostImg> imgs;
    private List<Comment> comments;


    public MinePostAdapter(List<Post> posts, Context context, int item_id) {
        this.posts = posts;
        this.context = context;
        this.item_id = item_id;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(item_id,null);
        }
//img
        imgs = posts.get(position).getImgs();
        ImageView img1 = convertView.findViewById(R.id.iv1);
        ImageView img2 = convertView.findViewById(R.id.iv2);
        ImageView img3 = convertView.findViewById(R.id.iv3);
        ImageView img4 = convertView.findViewById(R.id.iv4);
        ImageView img5 = convertView.findViewById(R.id.iv5);
        ImageView img6 = convertView.findViewById(R.id.iv6);
        ImageView headerPath=convertView.findViewById( R.id.community_parent_header);

        Log.e("images.size",""+imgs.size());
        if (imgs.size() == 0){
            img1.setVisibility(View.GONE);
            img2.setVisibility(View.GONE);
            img3.setVisibility(View.GONE);
            img4.setVisibility(View.GONE);
            img5.setVisibility(View.GONE);
            img6.setVisibility(View.GONE);
        }else if(imgs.size() == 1){
            Glide.with(context).load(Data.url+imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img1);
            img2.setVisibility(View.GONE);
            img3.setVisibility(View.GONE);
            img4.setVisibility(View.GONE);
            img5.setVisibility(View.GONE);
            img6.setVisibility(View.GONE);
        }else  if (imgs.size() == 2){
            Glide.with(context).load(Data.url+imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img1);
            Glide.with(context).load(Data.url+imgs.get(1).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img2);
            img3.setVisibility(View.GONE);
            img4.setVisibility(View.GONE);
            img5.setVisibility(View.GONE);
            img6.setVisibility(View.GONE);
        }else if(imgs.size() == 3){
            Glide.with(context).load(Data.url+imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img1);
            Glide.with(context).load(Data.url+imgs.get(1).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img2);
            Glide.with(context).load(Data.url+imgs.get(2).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img3);
            img4.setVisibility(View.GONE);
            img5.setVisibility(View.GONE);
            img6.setVisibility(View.GONE);
        }else if(imgs.size() == 4){
            Glide.with(context).load(Data.url+imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img1);
            Glide.with(context).load(Data.url+imgs.get(1).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img2);
            Glide.with(context).load(Data.url+imgs.get(2).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img3);
            Glide.with(context).load(Data.url+imgs.get(3).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img4);
            img5.setVisibility(View.GONE);
            img6.setVisibility(View.GONE);
        }else if(imgs.size() == 5){
            Glide.with(context).load(Data.url+imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img1);
            Glide.with(context).load(Data.url+imgs.get(1).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img2);
            Glide.with(context).load(Data.url+imgs.get(2).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img3);
            Glide.with(context).load(Data.url+imgs.get(3).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img4);
            Glide.with(context).load(Data.url+imgs.get(4).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img5);
            img6.setVisibility(View.GONE);
        }else {
            Glide.with(context).load(Data.url+imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img1);
            Glide.with(context).load(Data.url+imgs.get(1).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img2);
            Glide.with(context).load(Data.url+imgs.get(2).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img3);
            Glide.with(context).load(Data.url+imgs.get(3).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img4);
            Glide.with(context).load(Data.url+imgs.get(4).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img5);
            Glide.with(context).load(Data.url+imgs.get(5).getPath()).fallback(R.drawable.left).placeholder(R.drawable.dengdai).error(R.drawable.imgcuowu).into(img6);
        }

//评论

        Button button = convertView.findViewById(R.id.community_commenting);
        final InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(context, R.style.dialog_center);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputTextMsgDialog.show();
                inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                    @Override
                    public void onTextSend(String msg) {
                        //获得输入框中的文字（点击发送之后回调）
                        Log.e("comment",msg);
                        AddCommentTask task = new AddCommentTask(position,msg);
                        task.execute();
                    }
                });
            }
        });
        RequestOptions options = new RequestOptions().circleCrop();
        Log.e( "headerpath",posts.get( position ).getParent().getHeaderPath());
        Glide.with( context ).load( Data.ip+"childImg/"+posts.get(position).getParent().getHeaderPath()).apply(options).into( headerPath );
        TextView nickName = convertView.findViewById(R.id.community_parent_name);
        nickName.setText(posts.get(position).getParent().getNickName());
        TextView time = convertView.findViewById(R.id.community_time);
        time.setText(posts.get(position).getTime().toString());
        TextView content = convertView.findViewById(R.id.community_content);
        content.setText(posts.get(position).getContent());
        final TextView praiseCount = convertView.findViewById(R.id.community_praiseNum);
        praiseCount.setText(posts.get(position).getPraiseCount()+"");
        final ImageView praising = convertView.findViewById(R.id.community_praising);//点赞
        if (posts.get(position).getIsPraise() > 0){
            praising.setImageResource(R.drawable.dianzaned);
        }
        praising.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posts.get(position).getIsPraise() == 0) {
                    praiseCount.setText(posts.get(position).getPraiseCount()+1 + "");
                    posts.get(position).setPraiseCount(posts.get(position).getPraiseCount()+1);
                    praising.setImageResource(R.drawable.dianzaned);
                    posts.get(position).setIsPraise(posts.get(position).getIsPraise()+1);
                    //数据库(点赞)
                    PraiseTask task = new PraiseTask(position);
                    task.execute();
                }
            }
        });
        LinearLayout detail = convertView.findViewById(R.id.community_2_detail);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("isPraise",posts.get(position).getIsPraise());
                intent.putExtra("post",posts.get(position));
                intent.putExtra("imgSize",imgs.size());
                for (int i = 0;i<imgs.size();i++){
                    intent.putExtra("img"+i,imgs.get(i));
                }
                context.startActivity(intent);
            }
        });

        //三条评论
        comments = posts.get(position).getComments();
        TextView comment1 = convertView.findViewById(R.id.community_comment1);
        TextView comment2 = convertView.findViewById(R.id.community_comment2);
        TextView comment3 = convertView.findViewById(R.id.community_comment3);


        if (comments.size()==3){
            comment1.setText(comments.get(0).getCommentator().getNickName()+":"+comments.get(0).getContent());
            comment2.setText(comments.get(1).getCommentator().getNickName()+":"+comments.get(1).getContent());
            comment3.setText(comments.get(2).getCommentator().getNickName()+":"+comments.get(2).getContent());
        }else if(comments.size()==2){
            comment1.setText(comments.get(0).getCommentator().getNickName()+":"+comments.get(0).getContent());
            comment2.setText(comments.get(1).getCommentator().getNickName()+":"+comments.get(1).getContent());
            comment3.setText("");
        }else if (comments.size()==1){
            comment1.setText(comments.get(0).getCommentator().getNickName()+":"+comments.get(0).getContent());
            comment2.setText("");
            comment3.setText("");
        }else {
            comment1.setText("");
            comment2.setText("");
            comment3.setText("");
        }


        return convertView;
    }

    private class PraiseTask extends AsyncTask{
        private int position;

        public PraiseTask(int position) {
            this.position = position;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try{
                URL url = new URL("http://192.168.2.115:8080/Java/PraiseAddServlet");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                InputStream is = con.getInputStream();
                con.setRequestMethod("POST");
                JSONObject User_id = new JSONObject();

                User_id.put("praiserId",1);//发送登录者ID
                User_id.put("postId",posts.get(position).getId());
                OutputStream os = con.getOutputStream();
                os.write(User_id.toString().getBytes());

                is.close();
                os.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private class AddCommentTask extends AsyncTask {
        private String content;
        private int position;

        public AddCommentTask( int position, String content) {
            this.position = position;
            this.content = content;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Toast.makeText(context,"评论成功！",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try{
                URL url = new URL("http://192.168.2.115:8080/Java/CommentAddServlet");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("postId",posts.get(position).getId());
                jsonObject.put("commentatorId",1);//登录者ID

                jsonObject.put("content",content);
                jsonObject.put("time",new Timestamp(System.currentTimeMillis()));

                OutputStream os = con.getOutputStream();
                os.write(jsonObject.toString().getBytes());

                InputStream is = con.getInputStream();

                is.close();
                os.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
