package com.example.lenovo.maandroid.Community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lenovo.maandroid.R;

import java.util.List;

public class CommentAdapter extends BaseAdapter {
    private List<Comment> comments;
    private Context context;
    private int item_id;

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
        time.setText(comments.get(position).getTime().toString());
        TextView content = convertView.findViewById(R.id.comment_content);
        content.setText(comments.get(position).getContent());

        return convertView;
    }
}
