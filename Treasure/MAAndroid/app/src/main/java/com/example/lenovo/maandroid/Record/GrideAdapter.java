package com.example.lenovo.maandroid.Record;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.lenovo.maandroid.R;

import java.util.ArrayList;
import java.util.List;

public class GrideAdapter extends BaseAdapter {
    private List<String>  list=new ArrayList<>();
    private  int itemResId;
    private Context context;

    public GrideAdapter(Context context,List<String> list, int itemResId) {
        this.list = list;
        this.itemResId = itemResId;
        this.context = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(itemResId,null);
            // 获取屏幕宽度
            int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
            int width = (widthPixels - 20)/3;
            int height = width;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width,height);
            layoutParams.setMargins(15,15,15,15);
            convertView.setLayoutParams(layoutParams);
            holder=new ViewHolder();
            holder.image=convertView.findViewById(R.id.image);
            holder.frameLayout = convertView.findViewById(R.id.fm_image);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }

        Glide.with(context).load(list.get(position)).into(holder.image);
        return convertView;
    }

    private  class ViewHolder{
        public  ImageView image;
        public FrameLayout frameLayout;
    }
}
