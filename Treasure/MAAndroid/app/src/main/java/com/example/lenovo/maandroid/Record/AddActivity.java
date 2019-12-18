package com.example.lenovo.maandroid.Record;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lenovo.maandroid.Entity.Grimg;
import com.example.lenovo.maandroid.Entity.GrowthRecord;
import com.example.lenovo.maandroid.Host.MainActivity;
import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Utils.Constant;
import com.example.lenovo.maandroid.Utils.MyGridView;
import com.example.lenovo.maandroid.Utils.PhotoLoader;
import com.example.lenovo.maandroid.Utils.ViewDataUtils;
import com.example.library.AutoFlowLayout;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import indi.liyi.viewer.ImageViewer;
import indi.liyi.viewer.ViewData;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddActivity extends AppCompatActivity {
    private static final int READ_OK = 20005;
    private static final int REQUEST_IMAGE = 20006;
    private AutoFlowLayout mFlowLayout;
    private String[] mData = {"生日", "比赛", "游戏", "旅游", "互动", "成长", "捣蛋", "陪伴", "日常"};
    private LayoutInflater mLayoutInflater;
    private int count = 6;
    private ImageViewer iver;
    private OkHttpClient okHttpClient;
    private Button addUpload;
    private ImageView addPicture;
    private MyGridView gridView;
    private GrideAdapter grideAdapter;
    private EditText etContent;
    private List<String> list = new ArrayList<>();
    private List<ViewData> vdList = new ArrayList<>();
    private GrowthRecord growthRecord;
    private Grimg grimg;
    private ArrayList<String> mSelectPath;  //从相册选择的图片路径
    private String tags;     //标签数组
    private static final int UPLOAD_OK = 100;
    //返回按钮
    private ImageView ivReturn;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPLOAD_OK:
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_record);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8 );
        }
        // 自定义图片加载器
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
        //初始化控件
        initView();
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


            }
        });

        mFlowLayout =  findViewById(R.id.afl_cotent);
        for (int i = 0; i< 9; i ++ ){
            View item = LayoutInflater.from(this).inflate(R.layout.sub_item, null);
            final TextView tvAttrTag = item.findViewById(R.id.tv_attr_tag);
            tvAttrTag.setText(mData[i]);
            mFlowLayout.addView(item);
            tvAttrTag.setTag(false);
            tvAttrTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvAttrTag.getTag().equals(false)) {
                        Log.e("aa", "没点过");
                        tvAttrTag.setBackgroundResource(R.drawable.textview_border2);
                        Log.e("11",tvAttrTag.getBackground()+"");
                        if (tags.equals("")) {
                            tags = tags + tvAttrTag.getText().toString();
                        } else {
                            if (!tags.contains(tvAttrTag.getText())) {
                                tags = tags + "-" + tvAttrTag.getText().toString();
                            }
                        }
                        tvAttrTag.setTag(true);
                    } else {
                        Log.e("aa", "点过");
                        tvAttrTag.setBackgroundResource(R.drawable.textview_border);
                        tvAttrTag.setTag(false);
                        int position = tags.indexOf(tvAttrTag.getText().toString());
                        Log.e("aa", position + "");
                        Log.e("len", tags.length() + "");
                        if (position > 2) {
                            tags = tags.replace("-" + tvAttrTag.getText(), "");
                        } else if (position == 0 && tags.length() != 2) {

                            tags = tags.replace(tvAttrTag.getText() + "-", "");
                        } else {
                            tags = tags.replace(tvAttrTag.getText(), "");
                        }
                    }
                    Log.e("AddActivity成长记录的字符串", tags);
                }
            });
        }


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        gridView = findViewById(R.id.add_grid);

        okHttpClient = new OkHttpClient();
        addUpload = findViewById(R.id.add_upload);
        //上传按钮
        addUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先上传到growthRecord表
                initData();
                Log.e("AddActivity", growthRecord.toString());
                FormBody body = new FormBody.Builder()
                        .add("parentId", growthRecord.getParentId() + "")
                        .add("upTime", growthRecord.getUpTime())
                        .add("content", growthRecord.getContent())
                        /*.add("imgPath",grimg.getImgPath())*/
                        //.add("tag",grimg.getTag())
                        .build();
                final Request request = new Request.Builder().url(Constant.BASE_IP + "AddGrowthRecordServlet").post(body).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        Log.e("插入的成长记录的id值", str);
                        if (str != null && !str.equals("")) {
                            //返回的是插入的growthRecord的id值
                            growthRecord.setId(Integer.parseInt(str));
                            grimg.setGrowthRecordId(Integer.parseInt(str));
                            //在根据这个growthRecordID插入grimg表
                            addRecordImgPath();
                            Message message = new Message();
                            message.what = UPLOAD_OK;
                            handler.sendMessage(message);
                        }
                    }
                });
            }
        });
    }

    //初始化视图
    public void initView() {
        etContent = findViewById(R.id.et_content);
        tags = "";
        ivReturn = findViewById(R.id.iv_return);
        iver = findViewById(R.id.iver_add_record);
    }

    //初始化数据
    public void initData() {
        growthRecord = new GrowthRecord();
        grimg = new Grimg();
        //获得控件值

        String content = "";
        content = etContent.getText().toString();
        int parentId = getSharedPreferences("parent",MODE_PRIVATE).getInt("parentId",-1);

        //String tag = "";
        //String imgPath = "";
        //初始化GrowthRecord
        growthRecord.setContent(content);
        growthRecord.setParentId(parentId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        growthRecord.setUpTime(simpleDateFormat.format(date));
        //初始化Grimg
        //grimg.setImgPath(imgPath);
        grimg.setTag(tags);
        grimg.setUpTime(simpleDateFormat.format(date));
    }

    public void addRecordImgPath() {
        Log.e("AddActivity","添加记录图片");
        for (int i = 0; i < mSelectPath.size(); i++) {
            Log.e("路径", mSelectPath.get(i).toString());
            //上传至服务器
            File file = new File(mSelectPath.get(i).toString());
            //Log.e("图片名字",file.getName());
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            Request request = new Request.Builder().url(Constant.BASE_IP + "UploadServlet").post(body).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    int id = Integer.parseInt(response.body().string());
                    if (id != -1) { //说明插入图片成功，返回的是插入图片的id
                        //根据图片id插入其他值
                        addRecordImgOther(id);
                    }
                }
            });
        }
    }

    public void addRecordImgOther(int imgId) {
        //修改数据库

        Log.e("AddActivity","添加记录其他");
        Log.e("AddActivity",grimg.toString());

        FormBody body = new FormBody.Builder()
                .add("id", imgId + "")
                .add("growthRecordId", grimg.getGrowthRecordId() + "")
                .add("upTime", grimg.getUpTime())
                .add("tag", grimg.getTag())
                .build();
        final Request request = new Request.Builder().url(Constant.BASE_IP + "GrimgServlet").post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                //Log.e("插入的成长图片的id值", str);
                if (str.equals("OK")) {
                    Looper.prepare();
                    finish();
                   Toast.makeText(AddActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
//                    Intent intent=new Intent(AddActivity.this, MainActivity.class);
//                    intent.setAction( "inter" );
//                    intent.putExtra( "flag",3 );
//                    startActivity(intent);
                    Looper.loop();
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                finish();
                break;
            case R.id.add_picture:
                if (ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA}, READ_OK);
                } else {
                    ISListConfig config = new ISListConfig.Builder()
                            // 是否多选, 默认true
                            .multiSelect(true)
                            // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                            .rememberSelected(true)
                            // “确定”按钮背景色
                            .btnBgColor(Color.parseColor("#36E60F"))
                            // “确定”按钮文字颜色
                            .btnTextColor(Color.WHITE)
                            // 使用沉浸式状态栏
                            .statusBarColor(Color.parseColor("#555555"))
                            // 返回图标ResId
                            .backResId(android.support.v7.appcompat.R.drawable.abc_action_bar_item_background_material)
                            // 标题
                            .title("图片")
                            // 标题文字颜色
                            .titleColor(Color.WHITE)
                            // TitleBar背景色
                            .titleBgColor(Color.parseColor("#555555"))
                            // 裁剪大小。needCrop为true的时候配置
                            .cropSize(1, 1, 200, 200)
                            .needCrop(true)
                            // 第一个是否显示相机，默认true
                            .needCamera(true)
                            // 最大选择图片数量，默认9
                            .maxNum(6)
                            .build();
                    // 跳转到图片选择器
                    ISNav.getInstance().toListActivity(AddActivity.this, config, REQUEST_IMAGE);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_OK) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ISListConfig config = new ISListConfig.Builder()
                        // 是否多选, 默认true
                        .multiSelect(true)
                        // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                        .rememberSelected(true)
                        // “确定”按钮背景色
                        .btnBgColor(Color.parseColor("#36E60F"))
                        // “确定”按钮文字颜色
                        .btnTextColor(Color.WHITE)
                        // 使用沉浸式状态栏
                        .statusBarColor(Color.parseColor("#555555"))
                        // 返回图标ResId
                        .backResId(android.support.v7.appcompat.R.drawable.abc_action_bar_item_background_material)
                        // 标题
                        .title("图片")
                        // 标题文字颜色
                        .titleColor(Color.WHITE)
                        // TitleBar背景色
                        .titleBgColor(Color.parseColor("#555555"))
                        // 裁剪大小。needCrop为true的时候配置
                        .cropSize(1, 1, 200, 200)
                        .needCrop(true)
                        // 第一个是否显示相机，默认true
                        .needCamera(true)
                        // 最大选择图片数量，默认9
                        .maxNum(6)
                        .build();
                // 跳转到图片选择器
                ISNav.getInstance().toListActivity(this, config, REQUEST_IMAGE);
            }
        } else {
            Toast.makeText(AddActivity.this, "请手动打开存储权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {//从相册选择完图片s
            //压缩图片
            mSelectPath = data.getStringArrayListExtra("result");
            grideAdapter = new GrideAdapter(this, mSelectPath, R.layout.list_gride);
            gridView.setAdapter(grideAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Point mScreenSize = ViewDataUtils.getScreenSize(getApplicationContext());
                    vdList.clear();
                    for (int i = 0, len = mSelectPath.size(); i < len; i++) {
                        ViewData viewData = new ViewData();
                        viewData.setImageSrc(mSelectPath.get(i));
                        viewData.setTargetX(0);
                        viewData.setTargetY(0);
                        viewData.setTargetWidth(mScreenSize.x);
                        viewData.setTargetHeight(ViewDataUtils.dp2px(getApplicationContext(), 200));
                        vdList.add(viewData);
                    }
                    iver.overlayStatusBar(false) // ImageViewer 是否会占据 StatusBar 的空间
                            .viewData(vdList) // 数据源
                            .imageLoader(new PhotoLoader()) // 设置图片加载方式
                            .playEnterAnim(true) // 是否开启进场动画，默认为true
                            .playExitAnim(true) // 是否开启退场动画，默认为true
                            .duration(300) // 设置进退场动画时间，默认300
                            .showIndex(false) // 是否显示图片索引，默认为true
//                            .loadIndexUI(indexUI) // 自定义索引样式，内置默认样式
//                            .loadProgressUI(progressUI) // 自定义图片加载进度样式，内置默认样式
                            .watch(position);
                }
            });
        }
    }
}
