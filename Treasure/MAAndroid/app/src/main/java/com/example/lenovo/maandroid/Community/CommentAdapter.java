package com.example.lenovo.maandroid.Community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lenovo.maandroid.Entity.Comment;
import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Utils.Data;

import java.util.List;

public class CommentAdapter extends BaseAdapter {
    private List<Comment> comments;
    private Context context;
    private int item_id;
    private RequestOptions options;

    public CommentAdapter(List<Comment> comments, Context context, int item_id) {
        this.comments = comments;
        this.context = context;
        this.item_id = item_id;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(item_id,null);
        }
        TextView name = convertView.findViewById(R.id.comment_parentName);
        name.setText(comments.get(position).getCommentator().getNickName());
        TextView time = convertView.findViewById(R.id.comment_time);
        String time1 = comments.get(position).getTime().toString().substring( 0,16 );
        time.setText(time1);
        TextView content = convertView.findViewById(R.id.comment_content);
        content.setText(comments.get(position).getContent());
        //头像
        ImageView header = convertView.findViewById(R.id.comment_parentHeader);
        options = new RequestOptions()
                .circleCrop()
                .placeholder( R.drawable.aaa)
                .error( R.drawable.aaa)
                .fallback( R.drawable.aaa);
        Glide.with(context).load( Data.ip+"childImg/"+comments.get( position ).getCommentator().getHeaderPath()).apply( options).into(header);

        //回复
        LinearLayout L = convertView.findViewById(R.id.comment_response_L);
        if (comments.get(position).getResponder().getNickName().equals("null")){
            L.setVisibility(View.GONE);
        }else{
            L.setVisibility(View.VISIBLE);
            TextView responderName = convertView.findViewById(R.id.responder_name);
            responderName.setText("回复 @"+comments.get(position).getResponder().getNickName()+"");
            TextView resContent = convertView.findViewById(R.id.responder_content);
            resContent.setText(comments.get(position).getResCom().getContent()+"");
        }
        return convertView;
    }
}
