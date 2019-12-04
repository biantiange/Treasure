package com.example.lenovo.maandroid.Mine;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lenovo.maandroid.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MyChildListViewAdapter extends BaseAdapter{

    //原始数据
    public static List<Map<String,Object>> dataSource=null;
    /* private List<cakes> dataSource;*/
    //上下文环境
    private Context context=null;
    //item对应的布局文件
    private int item_layout_id;

    /**
     *  构造器，完成初始化
     * @param context           上下文环境
     * @param dataSource        原始数据
     * @param item_layout_id    item对应的布局文件
     */
public MyChildListViewAdapter(Context context, List<Map<String,Object>>dataSource, int item_layout_id){

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
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder holder=null;
      /*  if(null==convertView){*/
            convertView= LayoutInflater.from(context).inflate( R.layout.mychild_item,null);
            //关联VIewHolder
            holder=new ViewHolder();
            holder.childImg=convertView.findViewById( R.id.child_img_a);
            holder.child_nicknam=convertView.findViewById( R.id.child_nickname );
            holder.child_years=convertView.findViewById( R.id.child_years);

            holder.xiugai=convertView.findViewById( R.id.child_xiugai);

     /*   }else{
            holder=(ViewHolder)convertView.getTag();
        }*/
        //给缓存的holder绑定数据对象

        //从数据库中获取信息
        Map<String,Object>map=dataSource.get( position);
        int url= (int) map.get("child_img_a");
        RequestOptions options=new RequestOptions().circleCrop();
        Glide.with(context)
                .load(url)
                .apply(options)
                .into( holder.childImg );
        holder.child_nicknam.setText(map.get("child_nickname").toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());

        Log.e("dangqiannian",simpleDateFormat.format(date));

        int t= Integer.parseInt( simpleDateFormat.format(date) );
        String old=map.get("child_old").toString();
        String[] olda =old.split( "," );
        int j= t-(Integer.parseInt( olda[0] ));
        holder.child_years.setText(j+"岁");

        /*holder.xiugai.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击进入mychild详情可编辑页面，待写
                Intent intent=new Intent(context,AddOrEditChild.class);
                intent.putExtra( "childposition",position);//孩子的位置是哪个
                startActivity(intent);

            }
        } );*/
        notifyDataSetChanged();
        return convertView;
    }

    private class ViewHolder {
        ImageView childImg;
        TextView child_nicknam;
        TextView child_years;
       ImageButton xiugai;
    }
}