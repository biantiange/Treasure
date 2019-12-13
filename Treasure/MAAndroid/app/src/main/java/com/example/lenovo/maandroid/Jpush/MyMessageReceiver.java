package com.example.lenovo.maandroid.Jpush;

import android.content.Context;
import android.util.Log;

import com.baidu.mapapi.map.MapView;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class MyMessageReceiver extends JPushMessageReceiver {
    //处理消息
    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageArrived(context, notificationMessage);
        //通知消息到达时回调
        //通知内容都在notificationMessage中,附加字段以JSON格式传送
        Log.e("MAAndroid接收到通知","通知标题："+notificationMessage.notificationTitle
            +"通知内容："+notificationMessage.notificationContent+"附加字段："+notificationMessage.notificationExtras);
        List<Map<String,Object>> list=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        map.put("tag",notificationMessage.notificationExtras);
        map.put("content",notificationMessage.notificationContent);
        list.add(map);
        if(notificationMessage.notificationContent!=null){
            EventBus.getDefault().postSticky(new Gson().toJson(list));
        }
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage);
    }

    //程序接收到自定义消息时回调
    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);
    }
}
