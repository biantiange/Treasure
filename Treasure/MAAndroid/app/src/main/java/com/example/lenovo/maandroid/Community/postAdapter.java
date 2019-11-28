package com.example.lenovo.maandroid.Community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lenovo.maandroid.R;

import java.util.List;

public class postAdapter extends BaseAdapter {
    List<Post> posts;
    Context context;
    int item_id;

    public postAdapter(List<Post> posts, Context context, int itemid) {
        this.posts = posts;
        this.context = context;
        this.item_id = itemid;
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
        return convertView;
    }
}
