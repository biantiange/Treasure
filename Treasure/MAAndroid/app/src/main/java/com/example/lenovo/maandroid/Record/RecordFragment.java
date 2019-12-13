package com.example.lenovo.maandroid.Record;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.lenovo.maandroid.Entity.Dates;
import com.example.lenovo.maandroid.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class RecordFragment extends Fragment {
    List<Dates> datas = new ArrayList<Dates>();
    private HorizontalListView mlv;
    private Banner banner;

    private List<String> mTitleList = new ArrayList<>();
    private List<Integer> mImgList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newView=inflater.inflate(R.layout.record_main,container,false);
        mlv = newView.findViewById(R.id.mlv);
        initData();
//        TimeComparator comparator = new TimeComparator();
//        Collections.sort(datas, comparator);

        TimeAdapter adapter = new TimeAdapter((ArrayList<Dates>) datas, getContext());
        mlv.setAdapter(adapter);
        banner =newView.findViewById(R.id.home_play_banner);
        // 设置轮播图
        BannerSet();
        ImageView look=newView.findViewById(R.id.look);
        ImageView add=newView.findViewById(R.id.add);
        look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),LookActivity.class);
                startActivity(intent);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),AddActivity.class);
                startActivity(intent);
            }
        });

        return newView;
    }

    /**
     * 这里用虚拟数据实现，仅供参考
     */
    private void initData() {
        Dates item1 = new Dates();
        //  item1.setTitle("提交订单");
        item1.setTime("2017-03-14");
        item1.setStatu(1);              //设置状态标记1 ，0
        Dates item2 = new Dates();
        //  item2.setTitle("已支付");
        item2.setTime("2016-03-19");
        item2.setStatu(1);
        Dates item3 = new Dates();
        //  item3.setTitle("商品出库");
        item3.setTime("2018-03-15 00:33");
        item3.setStatu(0);
        Dates item4 = new Dates();
        //   item4.setTitle("已签收");
        item4.setTime("2020-03-15 15:55");
        item4.setStatu(0);

        datas.add(item1);
        datas.add(item2);
        datas.add(item3);
        datas.add(item4);
    }

    private void BannerSet() {
        mImgList.add(R.drawable.bw);
        mImgList.add(R.drawable.ly);
        mImgList.add(R.drawable.lz);
        mImgList.add(R.drawable.money);

        mTitleList.clear();
        for (int i = 0; i < mImgList.size(); i++) {
            mTitleList.add("第" + i + "张图片");
        }

        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE); // 显示圆形指示器和标题（水平显示
        //设置图片加载器
        banner.setImageLoader(new MyLoader());
        //设置图片集合
        banner.setImages(mImgList);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(mTitleList);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        // setOnBannerClickListener  1.4.9 以后就废弃了 。  setOnBannerListener 是1.4.9以后使用。
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Log.e("tt","第" + position + "张轮播图点击了！");
                //Toast.makeText(this,"",Toast.LENGTH_LONG).show();
                //UIUtils.showToast("第" + position + "张轮播图点击了！");
            }
        });


    }

    // 图片加载器
    public class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }
}
