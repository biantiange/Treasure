package com.example.lenovo.maandroid.Mine;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Utils.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MyChildListViewAdapter extends BaseAdapter {

    //原始数据
    public static List<Map<String, Object>> dataSource = null;
    /* private List<cakes> dataSource;*/
    //上下文环境
    private Context context;
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
    public MyChildListViewAdapter(Context context, List<Map<String, Object>> dataSource, int item_layout_id) {

        this.context = context;
        this.dataSource = dataSource;
        this.item_layout_id = item_layout_id;


    }

    @Override
    public int getCount() {
        if (dataSource == null) {
            return 0;
        } else {
            return dataSource.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (dataSource != null) {
            return dataSource.get( position );
        } else return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder holder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from( context ).inflate( R.layout.mychild_item, null );
            //关联VIewHolder
            holder = new ViewHolder();
            holder.childImg = convertView.findViewById( R.id.child_img_a );
            holder.child_nicknam = convertView.findViewById( R.id.child_nickname );
            holder.child_years = convertView.findViewById( R.id.child_years );
            holder.xiugai = convertView.findViewById( R.id.child_xiugai );
            holder.child_item_i = convertView.findViewById( R.id.child_item_i );
            convertView.setTag( holder );

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //给缓存的holder绑定数据对象

        //从数据库中获取信息
        Map<String, Object> map = dataSource.get( position );
        final Object ur = map.get( "imgPath" );
        final String url = ur.toString();
        final Object ow = map.get( "id" );
        final String id = ow.toString();
        final Object pid = map.get( "parentId" );
        final String parentId = pid.toString();
        options = new RequestOptions()
                .circleCrop()
                .placeholder( R.drawable.ertong )
                .error( R.drawable.ertong )
                .fallback( R.drawable.ertong );
        Glide.with( context )
                .load( Data.ip+url )
                .apply( options )
                .into( holder.childImg );
        final String name = (String) map.get( "nickName" );
        holder.child_nicknam.setText( name );
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy" );// HH:mm:ss
        Date date = new Date( System.currentTimeMillis() );

        int t = Integer.parseInt( simpleDateFormat.format( date ) );
        final Object ol = map.get( "birthday" );
        final String old = ol.toString();
        final String olda = old.substring( 0, 4 );
        final String mon = old.substring( 4, 6 );
        final String day = old.substring( 6, 8 );
        Log.e( "olda", olda );
        int y = Integer.parseInt( olda );
        int j = t - y;
        holder.child_years.setText( j + "岁" );
        if (holder.xiugai == null) {
            Log.e( "xiugai", "kongzhi" );

        } else {
            Log.e( "xiugai", holder.xiugai.toString() );
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView childImg;
        TextView child_nicknam;
        TextView child_years;
        ImageButton xiugai;
        LinearLayout child_item_i;
    }
}