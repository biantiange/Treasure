package com.example.lenovo.maandroid;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.maandroid.Login.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LookAdapter extends BaseAdapter {
    //private HashMap<Integer,LookByTagEntity> dataSource;
    private List<Map<String,List<String>>> dataSource;
    //private List<Map<String,Object>> dataSource;
    private Context context;
    private int itemId;
    private GrideAdapter adapter;
    private List<String> imgPaths = new ArrayList<>();

    public LookAdapter(List<Map<String,List<String>>> dataSource, Context context, int itemId) {
        this.dataSource = dataSource;
        this.context = context;
        this.itemId = itemId;
    }

    @Override
    public int getCount() {
        if(dataSource!=null){
            return dataSource.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return dataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LookAdapter.ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(itemId,null);
            holder=new LookAdapter.ViewHolder();
            holder.textView=convertView.findViewById(R.id.tv_look);
            holder.gridView=convertView.findViewById(R.id.add_grid);
            convertView.setTag(holder);
        }else{
            holder=(LookAdapter.ViewHolder) convertView.getTag();
        }
        //Map<String,Object> map = dataSource.get(position);

        Map<String,List<String>> map = dataSource.get(position);
        List<String> paths = new ArrayList<>();
        Log.e("position",map.toString());
        for (String key : map.keySet()){
            List<String> value = map.get(key);
            for(int i=0;i<value.size();i++){
                String str = value.get(i);
                str = Constant.BASE_IP+str;
                paths.add(str);
            }
            holder.textView.setText(key);
            adapter=new GrideAdapter(context,paths,R.layout.list_gride);
            holder.gridView.setAdapter(adapter);
        }
           /* Iterator<String> iter = map.keySet().iterator();
        while(iter.hasNext()) {
            String key = iter.next();
            List<String> value = map.get(key);
            holder.textView.setText(key);
            adapter=new GrideAdapter(context,value,R.layout.list_gride);
            holder.gridView.setAdapter(adapter);
        }*/

        //holder.textView.setText(dataSource.get(position).get("upTime").toString());
       /*for(int i=0;i<dataSource.get(position).getImgPaths().length;i++){
            imgPaths.add(Constant.BASE_IP+dataSource.get(position).get("imgPath").toString());
        }*/
        /*holder.textView.setText(dataSource.get(position).getUpTime());
        for(int i=0;i<dataSource.get(position).getImgPaths().length;i++){
            imgPaths.add(Constant.BASE_IP+dataSource.get(position).getImgPaths());
        }
        adapter=new GrideAdapter(context,imgPaths,R.layout.list_gride);
        holder.gridView.setAdapter(adapter);*/
        return convertView;
    }

    private  class ViewHolder{
        public TextView textView;  //显示的时间
        public GridView gridView;  //显示的图片
    }
}
