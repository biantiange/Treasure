package com.example.lenovo.maandroid;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
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
            holder=new ViewHolder();
            holder.image=convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        Log.e("position",list.get(position));
        Glide.with(context).load(list.get(position).toString()).into(holder.image);
        return convertView;
    }

    private  class ViewHolder{
        public  ImageView image;
    }
}
