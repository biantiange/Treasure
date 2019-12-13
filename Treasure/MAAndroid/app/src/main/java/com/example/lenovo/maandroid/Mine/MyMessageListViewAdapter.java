package com.example.lenovo.maandroid.Mine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Utils.Data;

import java.util.List;
import java.util.Map;

public class MyMessageListViewAdapter extends BaseAdapter {

    //原始数据
    public static List<Map<String, Object>> dataSource;
    /* private List<cakes> dataSource;*/
    //上下文环境
    private Context context = null;
    //item对应的布局文件
    private int item_layout_id;
    private RequestOptions options;

    /**
     * 构造器，完成初始化
     *
     * @param context        上下文环境
     * @param dataSource     原始数据
     * @param item_layout_id item对应的布局文件
     */
    public MyMessageListViewAdapter(Context context, List<Map<String, Object>> dataSource, int item_layout_id) {

        this.context = context;
        this.dataSource = dataSource;
        this.item_layout_id = item_layout_id;
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

        options = new RequestOptions().circleCrop().placeholder( R.drawable.aaa ).error( R.drawable.aaa ).fallback( R.drawable.aaa );
        ViewHolder holder = null;
        if(null==convertView){
        convertView = LayoutInflater.from( context ).inflate( R.layout.list_my_message, null );
        //关联VIewHolder
        holder = new ViewHolder();
        holder.userImg = convertView.findViewById( R.id.comments_user_img );
        holder.comment_user_name = convertView.findViewById( R.id.comment_user_name );
        holder.comment_time = convertView.findViewById( R.id.comment_time );
        holder.my_comments = convertView.findViewById( R.id.my_comments );
        holder.comment_to_me = convertView.findViewById( R.id.comment_to_me );
        convertView.setTag( holder);
       }else{
            holder=(ViewHolder)convertView.getTag();
        }
        //给缓存的holder绑定数据对象

        //从数据库中获取信息
        Map<String, Object> map = dataSource.get( position );
        String url = (String)map.get( "userImg" );//评论人的imgPath
        String commenterName= (String) map.get( "nickName" );//评论人的名字
        String mycommentcontent= (String) map.get( "mycontent" );//我的评论
        String commentercontent= (String) map.get( "tomycontent" );//别人对我的评论
        String time= (String) map.get( "time" );
        Glide.with( context )
                .load(Data.url+url)
                .apply( options )
                .into( holder.userImg );
        holder.comment_user_name.setText(commenterName );
        holder.comment_time.setText( time );
        holder.comment_to_me.setText( "回复我： " + commentercontent );
        holder.my_comments.setText( "我的评论： " + mycommentcontent );
        notifyDataSetChanged();
        return convertView;
    }
    private class ViewHolder {
        ImageView userImg;
        TextView comment_user_name;
        TextView comment_time;
        TextView my_comments;
        TextView comment_to_me;
    }
}