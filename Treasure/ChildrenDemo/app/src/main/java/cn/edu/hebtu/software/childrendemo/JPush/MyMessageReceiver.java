package cn.edu.hebtu.software.childrendemo.JPush;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.childrendemo.StartServiceAppInfoActivity;
import cn.edu.hebtu.software.childrendemo.StartServicePositionActivity;
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
        Log.e("ChildDemo接收到通知","通知标题："+notificationMessage.notificationTitle +"通知内容："+notificationMessage.notificationContent+"附加字段："+notificationMessage.notificationExtras);
        Map<String, Object> map = new Gson().fromJson(notificationMessage.notificationExtras, new TypeToken<Map<String, Object>>() {}.getType());
        if(notificationMessage.notificationContent.equals("appInfo")){
           Intent intent = new Intent(context,StartServiceAppInfoActivity.class);
           intent.putExtra("childId",map.get("childId").toString());
           intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);   //修改入栈出栈操作
           context.startActivity(intent);
       }
        if(notificationMessage.notificationContent.equals("position")){
            Intent intent = new Intent(context,StartServicePositionActivity.class);
            intent.putExtra("childId",map.get("childId").toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);   //修改入栈出栈操作
            context.startActivity(intent);
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
