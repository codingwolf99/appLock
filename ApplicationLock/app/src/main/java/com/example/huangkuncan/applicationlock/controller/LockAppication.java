package com.example.huangkuncan.applicationlock.controller;

import android.app.Application;
import android.content.Context;

import com.bugtags.library.Bugtags;
import com.example.huangkuncan.applicationlock.database.LockDataBase;

/**
 * Created by huangkuncan on 2016/4/11.
 * 邮箱：673391138@qq.com
 * 功能：
 */
public class LockAppication extends Application {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化配置
        LockDataBase.initConfig(getApplicationContext());
        LockAppManager.initConfig(getApplicationContext());
        context=getApplicationContext();
        //在这里初始化
        Bugtags.start("c03a65a02efa7ab5fc80be3340327bae", this, Bugtags.BTGInvocationEventBubble);
    }
}
