package com.example.lenovo.maandroid.Community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lenovo.maandroid.R;

import java.util.List;

public class PostAdapter extends BaseAdapter {

    private List<Post> posts;
    private Context context;
    private int item_id;
    private List<Comment> comments;

    public PostAdapter(List<Post> posts, Context context, int item_id, List<Comment> comments) {
        this.posts = posts;
        this.context = context;
        this.item_id = item_id;
        this.comments = comments;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(item_id,null);
        }
        TextView nickName = convertView.findViewById(R.id.community_parent_name);
        nickName.setText(posts.get(position).getParent().getNickName());
        TextView time = convertView.findViewById(R.id.community_time);
        time.setText(posts.get(position).getTime().toString());
        TextView content = convertView.findViewById(R.id.community_content);
        content.setText(posts.get(position).getContent());
        TextView praiseCount = convertView.findViewById(R.id.community_praiseNum);
        praiseCount.setText(posts.get(position).getPraiseCount()+"");

        TextView comment1 = convertView.findViewById(R.id.community_comment1);
        comment1.setText(comments.get(0).getCommentator().getNickName()+":"+comments.get(0).getContent());
        TextView comment2 = convertView.findViewById(R.id.community_comment2);
        comment2.setText(comments.get(1).getCommentator().getNickName()+":"+comments.get(1).getContent());
        TextView comment3 = convertView.findViewById(R.id.community_comment3);
        comment3.setText(comments.get(2).getCommentator().getNickName()+":"+comments.get(2).getContent());

        return convertView;
    }
}
