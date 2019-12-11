package com.example.lenovo.maandroid.Community;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.bumptech.glide.Glide;
import com.example.lenovo.maandroid.R;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class PostAdapter extends BaseAdapter {
    private List<Post> posts;
    private Context context;
    private int item_id;
    private List<PostImg> imgs;
    private List<Comment> comments;


    public PostAdapter( List<Post> posts, Context context, int item_id) {
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

        Log.e("images.size",""+imgs.size());

        if (imgs.size() == 0){
            img1.setVisibility(View.GONE);
            img2.setVisibility(View.GONE);
            img3.setVisibility(View.GONE);
            img4.setVisibility(View.GONE);
            img5.setVisibility(View.GONE);
            img6.setVisibility(View.GONE);
        }else if(imgs.size() == 1){
            Glide.with(context).load(imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img1);
            img2.setVisibility(View.GONE);
            img3.setVisibility(View.GONE);
            img4.setVisibility(View.GONE);
            img5.setVisibility(View.GONE);
            img6.setVisibility(View.GONE);
        }else  if (imgs.size() == 2){
            Glide.with(context).load(imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img1);
            Glide.with(context).load(imgs.get(1).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img2);
            img3.setVisibility(View.GONE);
            img4.setVisibility(View.GONE);
            img5.setVisibility(View.GONE);
            img6.setVisibility(View.GONE);
        }else if(imgs.size() == 3){
            Glide.with(context).load(imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img1);
            Glide.with(context).load(imgs.get(1).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img2);
            Glide.with(context).load(imgs.get(2).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img3);
            img4.setVisibility(View.GONE);
            img5.setVisibility(View.GONE);
            img6.setVisibility(View.GONE);
        }else if(imgs.size() == 4){
            Glide.with(context).load(imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img1);
            Glide.with(context).load(imgs.get(1).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img2);
            Glide.with(context).load(imgs.get(2).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img3);
            Glide.with(context).load(imgs.get(3).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img4);
            img5.setVisibility(View.GONE);
            img6.setVisibility(View.GONE);
        }else if(imgs.size() == 5){
            Glide.with(context).load(imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img1);
            Glide.with(context).load(imgs.get(1).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img2);
            Glide.with(context).load(imgs.get(2).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img3);
            Glide.with(context).load(imgs.get(3).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img4);
            Glide.with(context).load(imgs.get(4).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img5);
            img6.setVisibility(View.GONE);
        }else {
            Glide.with(context).load(imgs.get(0).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img1);
            Glide.with(context).load(imgs.get(1).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img2);
            Glide.with(context).load(imgs.get(2).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img3);
            Glide.with(context).load(imgs.get(3).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img4);
            Glide.with(context).load(imgs.get(4).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img5);
            Glide.with(context).load(imgs.get(5).getPath()).fallback(R.drawable.left).placeholder(R.drawable.add).error(R.drawable.check).into(img6);
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

                    }
                });
            }
        });

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
        if (comments.size()==3){
            TextView comment1 = convertView.findViewById(R.id.community_comment1);
            comment1.setText(comments.get(0).getCommentator().getNickName()+":"+comments.get(0).getContent());
            TextView comment2 = convertView.findViewById(R.id.community_comment2);
            comment2.setText(comments.get(1).getCommentator().getNickName()+":"+comments.get(1).getContent());
            TextView comment3 = convertView.findViewById(R.id.community_comment3);
            comment3.setText(comments.get(2).getCommentator().getNickName()+":"+comments.get(2).getContent());
        }else if(comments.size()==2){
            TextView comment1 = convertView.findViewById(R.id.community_comment1);
            comment1.setText(comments.get(0).getCommentator().getNickName()+":"+comments.get(0).getContent());
            TextView comment2 = convertView.findViewById(R.id.community_comment2);
            comment2.setText(comments.get(1).getCommentator().getNickName()+":"+comments.get(1).getContent());
        }else if (comments.size()==1){
            TextView comment1 = convertView.findViewById(R.id.community_comment1);
            comment1.setText(comments.get(0).getCommentator().getNickName()+":"+comments.get(0).getContent());
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
                URL url = new URL("http://10.7.88.125:8080/Java/PraiseAddServlet");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                JSONObject User_id = new JSONObject();

                User_id.put("praiserId",1);//发送登录者ID
                User_id.put("postId",posts.get(position).getId());

                OutputStream os = con.getOutputStream();
                os.write(User_id.toString().getBytes());
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
