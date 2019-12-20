package com.example.lenovo.maandroid.Mine;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Utils.Constant;
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
        holder.comment_user_name = convertView.findViewById( R.id.comment_user_n);
        holder.comment_time_day = convertView.findViewById( R.id.comment_time_d );
        holder.my_comments = convertView.findViewById( R.id.my_comments );
        holder.comment_to_me = convertView.findViewById( R.id.comment_to_m );
        holder.comment_time_hour=convertView.findViewById( R.id.comment_time_h);
        holder.post_name=convertView.findViewById( R.id.post_n );
        holder.post_day=convertView.findViewById( R.id.post_time_d );
        holder.post_hour=convertView.findViewById( R.id.post_time_h );
        holder.post_img=convertView.findViewById( R.id.post_i );
        holder.post_content=convertView.findViewById( R.id.post_c);
        holder.post=convertView.findViewById( R.id.pos);
        convertView.setTag( holder);
       }else{
            holder=(ViewHolder)convertView.getTag();
        }
        //给缓存的holder绑定数据对象
        //从数据库中获取信息
        Map<String, Object> map = dataSource.get( position );
        String url = (String)map.get( "userImg" );//评论人的imgPath
        String commenterName= (String) map.get( "nickName" );//评论人的名字
        holder.my_comments.setVisibility( View.VISIBLE );

        if(map.get( "mycontent" )!=null) {

            String mycommentcontent = (String) map.get( "mycontent" );//我的评论
            if(mycommentcontent.length()>0) {
                holder.my_comments.setText( "我的评论： " + mycommentcontent );
            }else{
                Log.e( "kong?","kogggaggaggaggagagagag" );
                holder.my_comments.setVisibility( View.GONE );
            }
        }else{
            Log.e( "kong?","kogggaggaggaggagagagag" );
            holder.my_comments.setVisibility( View.GONE );
        }
        String commentercontent= (String) map.get( "tomycontent" );//别人对我的评论
        String time= (String) map.get( "time" );
        String day=time.substring( 0,10);
        String hour=time.substring( 11,16 );
        Glide.with( context )
                .load( Data.ip+"childImg/"+url)
                .apply( options )
                .into( holder.userImg );
        holder.comment_user_name.setText(commenterName );
        holder.comment_time_day.setText(day);
        holder.comment_time_hour.setText( hour);
        holder.comment_to_me.setText(commentercontent );

        Log.e( "tomy",commentercontent );

            Log.e("posterName", map.get( "posterName" ).toString()+":");
        holder.post_name.setText( map.get( "posterName" ).toString()+":" );
        String postContent=map.get( "postContent" ).toString();
        if(postContent==null || postContent.length()==0){
            holder.post_content.setText( "" );
        }else {
            holder.post_content.setText( map.get( "postContent" ).toString() );
        }
        String imgnum=map.get( "postImgNum").toString();
        Log.e( "num",imgnum );
        holder.post_img.setVisibility( View.VISIBLE );
       if(imgnum.equals( "0" )){
         holder.post_img.setVisibility( View.GONE);
       }else {
           Glide.with( context ).load( Constant.POST_IMG_BASE_IP+ map.get( "postImgPath" ).toString() ).into( holder.post_img );
       }
        String post_time= (String) map.get( "postTime" );
       Log.e( "tome",post_time );
        String post_t_day=post_time.substring( 0,10);
        String post_t_hour=post_time.substring( 11,16 );
        holder.post_day.setText( post_t_day );
        holder.post_hour.setText( post_t_hour );
        holder.post.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入相应的详情页面：明天写
            }
        } );
        return convertView;
    }
    private class ViewHolder {
        LinearLayout post;
        ImageView userImg;
        TextView comment_user_name;
        TextView comment_time_day;
        TextView comment_time_hour;
        TextView my_comments;
        TextView comment_to_me;
        TextView post_name;
        TextView post_day;
        TextView post_hour;
        ImageView post_img;
        TextView post_content;
    }
}