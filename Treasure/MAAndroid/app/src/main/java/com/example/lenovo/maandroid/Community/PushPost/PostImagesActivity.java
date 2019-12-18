package com.example.lenovo.maandroid.Community.PushPost;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lenovo.maandroid.Community.CommunityFragment;
import com.example.lenovo.maandroid.Entity.Comment;
import com.example.lenovo.maandroid.Entity.Parent;
import com.example.lenovo.maandroid.Entity.Post;
import com.example.lenovo.maandroid.Entity.PostImg;
import com.example.lenovo.maandroid.Host.MainActivity;
import com.example.lenovo.maandroid.Jpush.MyApplication;
import com.example.lenovo.maandroid.R;
import com.example.lenovo.maandroid.Utils.Constant;
import com.example.lenovo.maandroid.Utils.PhotoLoader;
import com.example.lenovo.maandroid.Utils.ViewDataUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hys.utils.AppUtils;
import com.hys.utils.DensityUtils;
import com.hys.utils.ImageUtils;
import com.hys.utils.InitCacheFileUtils;
import com.hys.utils.SdcardUtils;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import indi.liyi.viewer.ImageViewer;
import indi.liyi.viewer.ViewData;
import indi.liyi.viewer.ViewerStatus;
import indi.liyi.viewer.listener.OnBrowseStatusListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PostImagesActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int READ_OK = 100;
    private static final int POST_ACHIEVE = 2019;
    private ImageViewer iver;
    private Dialog dialog;
    public static final String FILE_DIR_NAME = "com.example.lenovo.maandroid";//应用缓存地址(包名)
    public static final String FILE_IMG_NAME = "images";//放置图片缓存
    public static final int IMAGE_SIZE = 6;//可添加图片最大数
    private static final int REQUEST_IMAGE = 1002;
    private Button btnPush;
    private ImageView ivExit;
    private RelativeLayout rlHeader;
    private ArrayList<String> originImages;//原始图片
    private ArrayList<String> dragImages;//压缩长宽后图片
    private Context mContext;
    private PostArticleImgAdapter postArticleImgAdapter;
    private ItemTouchHelper itemTouchHelper;
    private RecyclerView rcvImg;
    private EditText etContent;
    private TextView tv;//删除区域提示
    private LinearLayout mLinearLayout;
    private RelativeLayout rlLocal;
    private RelativeLayout rlOpen;
    private SharedPreferences sp;
    private TextView tvCancel;
    private TextView tvKeep;
    private TextView tvDontKeep;
    private List<ViewData> vdList;
    private Gson gson = new Gson();
    private String keepContent;
    private OkHttpClient client = new OkHttpClient();
    private SharedPreferences spPost;
    public static void startPostActivity(Context context, ArrayList<String> images) {
        Intent intent = new Intent(context, PostImagesActivity.class);
        intent.putStringArrayListExtra("img", images);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_images);
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

        initData();
        initView();
        setListener();
    }

    private void initData() {
        sp = getSharedPreferences(Constant.COMMUNITY_POST_KEEP_SP_NAME, MODE_PRIVATE);
        String oriKeepStr = sp.getString(Constant.COMMUNITY_POST_KEEP_IMGS_KEY, "[]");
        // 获取原始资源数据
        Type type = new TypeToken<List<String>>() {
        }.getType();
        originImages = gson.fromJson(oriKeepStr, type);
        keepContent = sp.getString(Constant.COMMUNITY_POST_KEEP_CONTENT_KEY, "");
//        originImages = getIntent().getStringArrayListExtra("img");
        mContext = getApplicationContext();
        if (ContextCompat.checkSelfPermission(PostImagesActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(PostImagesActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            InitCacheFileUtils.initImgDir(FILE_DIR_NAME, FILE_IMG_NAME);//清除图片缓存
        }
        //添加按钮图片资源
        String plusPath = getString(R.string.glide_plus_icon_string) + AppUtils.getPackageInfo(mContext).packageName + "/mipmap/" + R.mipmap.mine_btn_plus;
        dragImages = new ArrayList<>();
        originImages.add(plusPath);//添加按键，超过6 张时在adapter中隐藏
        dragImages.addAll(originImages);
        new Thread(new MyRunnable(dragImages, originImages, dragImages, myHandler, false)).start();//开启线程，在新线程中去压缩图片
    }

    private void initView() {
        iver = findViewById(R.id.iver_push_speak);
        ivExit = findViewById(R.id.iv_push_speak_exit);
        rlLocal = findViewById(R.id.rl_push_speak_local);
        rlOpen = findViewById(R.id.rl_push_speak_open_type);
        btnPush = findViewById(R.id.btn_push_speak_push);
        rcvImg = findViewById(R.id.rcv_img);
        rlHeader = findViewById(R.id.rl_push_speak_header);
        etContent = findViewById(R.id.et_content);
        tv = findViewById(R.id.tv);
        etContent.setText(keepContent);
        initRcv();
        mLinearLayout = findViewById(R.id.ll);
        if (!keepContent.equals("") || originImages.size() > 1) {
            btnPush.setEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                btnPush.setBackground(getResources().getDrawable(R.drawable.mybuttonborder));
            }
        }
    }

    private void setListener() {
        ivExit.setOnClickListener(this);
        btnPush.setOnClickListener(this);
        rlLocal.setOnClickListener(this);
        rlOpen.setOnClickListener(this);
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btnPush.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnPush.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etContent.getText().toString().length() > 0) {
                    btnPush.setEnabled(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        btnPush.setBackground(getResources().getDrawable(R.drawable.mybuttonborder));
                    }
                } else {
                    btnPush.setEnabled(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        btnPush.setBackground(getResources().getDrawable(R.drawable.mybuttonborder_no));
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_push_speak_exit:
                if (!etContent.getText().toString().equals("") || originImages.size() != 1) {
                    showExitDialog();
                } else {
                    sp.edit().clear().commit();
                    finish();
                }
                break;
            case R.id.btn_push_speak_push:
                spPost = getSharedPreferences("parent",MODE_PRIVATE );
                int id = spPost.getInt( "parentId",-1 );
                MediaType MutilPart_Form_Data = MediaType.parse("application/octet-stream;charset=utf-8");
                MultipartBody.Builder requestBodyBuilder = null;
                try {
                    requestBodyBuilder = new MultipartBody.Builder()
                            .setType(MediaType.parse("multipart/form-data;charset=utf-8"))
                            .addFormDataPart("id", id+"")// 传递表单数据
                            .setType(MediaType.parse("multipart/form-data;charset=utf-8"))
                            .addFormDataPart("content", URLEncoder.encode(etContent.getText().toString(), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // 可使用for循环添加img file
                if (originImages.size() > 1) {
                    for (int i = 0; i < originImages.size() - 1; i++) {
                        File file = new File(originImages.get(i));
                        requestBodyBuilder.addFormDataPart("files", file.getName(), RequestBody.create(MutilPart_Form_Data, file));
                    }
                }
                // 3.3 其余一致
                RequestBody requestBody = requestBodyBuilder.build();

                Request request = new Request.Builder().url(Constant.CON_POST_SPEAK_IP)
                        .post(requestBody)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Message message = new Message();
                        message.what = POST_ACHIEVE;
                        message.obj = response.body().string();
                        myHandler.sendMessage(message);
                    }
                });
                break;
            case R.id.rl_push_speak_local:
                Toast.makeText(getBaseContext(), "选择地址", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_push_speak_open_type:
                Toast.makeText(getBaseContext(), "选择公开", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_push_speak_cancel:
                dialog.dismiss();
                break;
            case R.id.tv_push_speak_dont_keep:
                dialog.dismiss();
                sp.edit().clear().commit();
                finish();
                break;
            case R.id.tv_push_speak_keep:
                dialog.dismiss();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Constant.COMMUNITY_POST_KEEP_CONTENT_KEY, etContent.getText().toString());
                List<String> keepList = new ArrayList<>();
                keepList.addAll(originImages);
                keepList.remove(keepList.size() - 1);
                editor.putString(Constant.COMMUNITY_POST_KEEP_IMGS_KEY, gson.toJson(keepList));
                editor.commit();
                finish();
                break;
        }
    }

    private void initRcv() {
        postArticleImgAdapter = new PostArticleImgAdapter(mContext, dragImages);
        rcvImg.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rcvImg.setAdapter(postArticleImgAdapter);
        MyCallBack myCallBack = new MyCallBack(postArticleImgAdapter, dragImages, originImages);
        itemTouchHelper = new ItemTouchHelper(myCallBack);
        itemTouchHelper.attachToRecyclerView(rcvImg);//绑定RecyclerView

        //事件监听
        rcvImg.addOnItemTouchListener(new OnRecyclerItemClickListener(rcvImg) {

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                if (originImages.size() <= IMAGE_SIZE && originImages.get(vh.getAdapterPosition()).contains(getString(R.string.glide_plus_icon_string))) {//打开相册
                    if (ContextCompat.checkSelfPermission(PostImagesActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(PostImagesActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(PostImagesActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PostImagesActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.CAMERA}, READ_OK);
                    } else {
                        ISListConfig config = new ISListConfig.Builder()
                                // 是否多选, 默认true
                                .multiSelect(true)
                                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                                .rememberSelected(false)
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
                                .maxNum(IMAGE_SIZE - originImages.size() + 1)
                                .build();
                        // 跳转到图片选择器
                        ISNav.getInstance().toListActivity(PostImagesActivity.this, config, REQUEST_IMAGE);
                    }
                } else if (originImages.size() > IMAGE_SIZE && originImages.get(vh.getAdapterPosition()).contains(getString(R.string.glide_plus_icon_string))) {

                } else {
                    rlHeader.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    showBigImgs(vh.getAdapterPosition());
                }
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                //如果item不是最后一个，则执行拖拽
                if (vh.getLayoutPosition() != dragImages.size() - 1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rcvImg.setElevation(1.0f);
                    }
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    itemTouchHelper.startDrag(vh);
                }
            }
        });

        myCallBack.setDragListener(new MyCallBack.DragListener() {
            @Override
            public void deleteState(boolean delete) {
                if (delete) {
                    tv.setBackgroundResource(R.color.holo_red_dark);
                    tv.setText(getResources().getString(R.string.post_delete_tv_s));
                } else {
                    tv.setText(getResources().getString(R.string.post_delete_tv_d));
                    tv.setBackgroundResource(R.color.holo_red_light);
                }
            }

            @Override
            public void dragState(boolean start) {
                if (start) {
                    tv.setVisibility(View.VISIBLE);
                } else {
                    tv.setVisibility(View.GONE);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void clearView() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rcvImg.setElevation(0.0f);
                }
                if (etContent.getText().toString().length() > 0 || originImages.size() > 1) {
                    btnPush.setEnabled(true);
                    btnPush.setBackground(getResources().getDrawable(R.drawable.mybuttonborder));
                } else {
                    btnPush.setEnabled(false);
                    btnPush.setBackground(getResources().getDrawable(R.drawable.mybuttonborder_no));
                }
                refreshLayout();
            }
        });
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
                        .rememberSelected(false)
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
            Toast.makeText(PostImagesActivity.this, "请手动打开存储权限", Toast.LENGTH_SHORT).show();
        }
    }
    //------------------图片相关-----------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {//从相册选择完图片
            //压缩图片
            new Thread(new MyRunnable(data.getStringArrayListExtra("result"),
                    originImages, dragImages, myHandler, true)).start();
        }
    }

    // 展示图片
    private void showBigImgs(int position) {
        iver.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iver.setElevation(3.0f);
        }
        vdList = new ArrayList<>();
        Point mScreenSize = ViewDataUtils.getScreenSize(getApplicationContext());
        for (int i = 0, len = originImages.size() - 1; i < len; i++) {
            ViewData viewData = new ViewData();
            viewData.setImageSrc(originImages.get(i));
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

        iver.setOnBrowseStatusListener(new OnBrowseStatusListener() {
            @Override
            public void onBrowseStatus(int status) {
                if (status == ViewerStatus.STATUS_BEGIN_OPEN) {
                    // 正在开启启动预览图片
                    rlHeader.setVisibility(View.GONE);
                } else if (status == ViewerStatus.STATUS_SILENCE) {
                    // 此时未开启预览图片
                    rlHeader.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 另起线程压缩图片
     */
    static class MyRunnable implements Runnable {

        ArrayList<String> images;
        ArrayList<String> originImages;
        ArrayList<String> dragImages;
        Handler handler;
        boolean add;//是否为添加图片

        public MyRunnable(ArrayList<String> images, ArrayList<String> originImages, ArrayList<String> dragImages, Handler handler, boolean add) {
            this.images = images;
            this.originImages = originImages;
            this.dragImages = dragImages;
            this.handler = handler;
            this.add = add;
        }

        @Override
        public void run() {
            SdcardUtils sdcardUtils = new SdcardUtils();
            String filePath;
            Bitmap newBitmap;
            int addIndex = originImages.size() - 1;
            for (int i = 0; i < images.size(); i++) {
                if (images.get(i).contains(MyApplication.getInstance().getString(R.string.glide_plus_icon_string))) {//说明是添加图片按钮
                    continue;
                }
                //压缩
                newBitmap = ImageUtils.compressScaleByWH(images.get(i),
                        DensityUtils.dp2px(MyApplication.getInstance().getContext(), 100),
                        DensityUtils.dp2px(MyApplication.getInstance().getContext(), 100));
                //文件地址
                filePath = sdcardUtils.getSDPATH() + FILE_DIR_NAME + "/"
                        + FILE_IMG_NAME + "/" + String.format("img_%d.jpg", System.currentTimeMillis());
                //保存图片
                ImageUtils.save(newBitmap, filePath, Bitmap.CompressFormat.JPEG, true);
                //设置值
                if (!add) {
                    images.set(i, filePath);
                } else {//添加图片，要更新
                    dragImages.add(addIndex, filePath);
                    originImages.add(addIndex++, images.get(i));
                }
            }
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    }

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<Activity> reference;

        public MyHandler(Activity activity) {
            reference = new WeakReference<>(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            PostImagesActivity activity = (PostImagesActivity) reference.get();
            activity.setListener();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        activity.postArticleImgAdapter.notifyDataSetChanged();
                        activity.refreshLayout();
                        if (activity.etContent.getText().toString().length() > 0 || activity.originImages.size() > 1) {
                            activity.btnPush.setEnabled(true);
                            activity.btnPush.setBackground(activity.getResources().getDrawable(R.drawable.mybuttonborder));
                        } else {
                            activity.btnPush.setEnabled(false);
                            activity.btnPush.setBackground(activity.getResources().getDrawable(R.drawable.mybuttonborder_no));
                        }
                        break;
                    case POST_ACHIEVE:
                        Toast.makeText(activity, "发布成功", Toast.LENGTH_SHORT).show();
                        activity.sp.edit().clear().commit();
                        Intent intent = new Intent( activity , CommunityFragment.class );
                        Post post = new Gson().fromJson( msg.obj.toString(),Post.class );
                        post.setTime( Timestamp.valueOf((new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( new Date(System.currentTimeMillis()) ) ) ));
                        post.setPraiseCount( 0 );
                        //图片
                        List<PostImg> imgs = new ArrayList<>(  );
                        for (int i=0;i<activity.originImages.size()-1;i++){
                            PostImg image = new PostImg();
                            image.setPath( activity.originImages.get( i ) );
                        }
                        post.setImgs( imgs );
                        //评论
                        List<Comment> comments = new ArrayList<>();
                        post.setComments(comments);
                        post.setIsPraise(0);
                        //发帖人
                        Parent parent = new Parent( );
                        activity.spPost = activity.getSharedPreferences( "parent",MODE_PRIVATE );
                        parent.setId( activity.spPost.getInt( "parentId",-1 ) );
                        parent.setHeaderPath( activity.spPost.getString( "headerPath" ,"") );
                        parent.setNickName( activity.spPost.getString( "nickName","" ) );
                        post.setParent(parent);
                        intent.putExtra( "myPost",post);
                        activity.setResult( 2019,intent );
                        activity.finish();
                        break;
                }
            }
        }
    }

    /**
     * 刷新地理位置等布局
     */
    private void refreshLayout() {
        //判断提醒谁布局看是否需要下移
        int row = postArticleImgAdapter.getItemCount() / 3;
        row = 0 == postArticleImgAdapter.getItemCount() % 3 ? row : row + 1;
//        row = 4 == row ? 3 : row;//row最多为三行   // maxImg = 9 时使用
        row = 3 == row ? 2 : row;//row最多为二行
        // 获取屏幕宽度 --》获取行高
        int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
        int width = (widthPixels - 30) / 3 - 20;
        int marginTop = (width + 5)
                * row
                + getResources().getDimensionPixelSize(R.dimen.article_post_rec_h)
                + 40;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLinearLayout.getLayoutParams();
        params.setMargins(15, marginTop, 15, 0);
        mLinearLayout.setLayoutParams(params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myHandler.removeCallbacksAndMessages(null);
    }

    private void showExitDialog() {
        //1、使用Dialog、设置style
        dialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this, R.layout.custom_dialog_choose_keep, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        tvCancel = dialog.findViewById(R.id.tv_push_speak_cancel);
        tvKeep = dialog.findViewById(R.id.tv_push_speak_keep);
        tvDontKeep = dialog.findViewById(R.id.tv_push_speak_dont_keep);

        tvCancel.setOnClickListener(this);
        tvKeep.setOnClickListener(this);
        tvDontKeep.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (iver.onKeyDown(keyCode, event)) {

            return iver.onKeyDown(keyCode, event);
        } else if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (!etContent.getText().toString().equals("") || originImages.size() != 1) {
                showExitDialog();
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
