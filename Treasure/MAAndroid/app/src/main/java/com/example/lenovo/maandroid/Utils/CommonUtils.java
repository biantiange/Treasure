package com.example.lenovo.maandroid.Utils;


import com.example.lenovo.maandroid.Jpush.MyApplication;


public class CommonUtils {

    /**
     * 获取dimens定义的大小
     *
     * @param dimensionId
     * @return
     */
    public static int getPixelById(int dimensionId) {
        return MyApplication.getInstance().getResources().getDimensionPixelSize(dimensionId);
    }

}
