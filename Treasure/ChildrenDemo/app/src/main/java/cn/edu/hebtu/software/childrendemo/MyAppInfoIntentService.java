package cn.edu.hebtu.software.childrendemo;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyAppInfoIntentService extends IntentService {


    private static DataPicker mDataPicker;
    public static void setDataPicker(DataPicker dataPicker) {
        mDataPicker = dataPicker;
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyAppInfoIntentService(String name) {
        super(name);
    }

    //必须定义该默认构造方法，启动服务创建服务对象时会调用该方法
    public MyAppInfoIntentService() {
        super("mythread");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //会自动创建子线程，可以直接执行耗时操作
        List<AppInfo> appInfoList = mDataPicker.getAppInfo();
        JSONArray json = new JSONArray();
        try {
            for (AppInfo a : appInfoList) {
                JSONObject jo = new JSONObject();
                jo.put("appName", a.getAppName());
                jo.put("useTime", a.getUseTime());
                jo.put("lastTime",a.getLastTime() );
                jo.put("icon",a.getApplicationIcon() );
                json.put(jo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String cid=intent.getStringExtra("ci");
        OkHttpClient okHttpClient = new OkHttpClient();
        Log.e("ChildDemo的请求", Constant.BASE_URL + "toParent/appInfo?childId="+cid);
        //2、创建Request对象
        Type listType = new TypeToken<List<AppInfo>>() {}.getType();
        Log.e("ChildDemo的请求数据", json.toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        Request request = new Request.Builder() //创建Builder对象
                .url(Constant.BASE_URL + "toParent/appInfo?childId="+cid)//设置网络请求的UrL地址
                .post(requestBody)
                .build();
        //3、创建Call对象
        final Call call = okHttpClient.newCall(request);
        //4、发送请求
        call.enqueue(new Callback() {
            //请求失败之后回调
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            //请求成功之后回调
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e("请求", json);
            }
        });
    }
}
