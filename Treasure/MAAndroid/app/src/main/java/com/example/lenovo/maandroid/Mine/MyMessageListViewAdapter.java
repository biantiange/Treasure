package com.example.lenovo.maandroid.Mine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.maandroid.Entity.Discuss;
import com.example.lenovo.maandroid.R;

import java.util.List;

public class MyMessageListViewAdapter extends BaseAdapter{
    public  List<Discuss> dataSource;
    private Context context;
    private int item_layout_id;

    public MyMessageListViewAdapter(Context context,List<Discuss>dataSource,int item_layout_id){

        this.context=context;
        this.dataSource=dataSource;
        this.item_layout_id=item_layout_id;
    }
    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate( R.layout.list_my_message,null);
            holder=new ViewHolder();
            holder.userImg=convertView.findViewById( R.id.comments_user_img);
            holder.comment_user_name=convertView.findViewById( R.id.comment_user_name );
            holder.comment_time=convertView.findViewById( R.id.comment_time);
            holder.my_comments=convertView.findViewById( R.id.my_comments);
            holder.comment_to_me=convertView.findViewById( R.id.comment_to_me);
            convertView.setTag(holder);
       }else{
            holder=(ViewHolder)convertView.getTag();
        }
//        Glide.with(context).load(R.drawable.aaa).circleCrop().into( holder.userImg );
//        holder.comment_user_name.setText(dataSource.get(position).getCommentatorId().toString());
//        holder.comment_time.setText( dataSource.get(position).getTime().toString());
//        holder.comment_to_me.setText( "回复我： "+ dataSource.get(position).getResponderId().toString());
//        holder.my_comments.setText( "我的评论： "+ dataSource.get(position).getResComId().toString() );
        //notifyDataSetChanged();
        return convertView;
    }
    private class ViewHolder {
       public ImageView userImg;
        public TextView comment_user_name;
        public TextView comment_time;
        public TextView my_comments;
        public TextView comment_to_me;
    }
}