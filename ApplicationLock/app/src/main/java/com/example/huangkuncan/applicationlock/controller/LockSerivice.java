package com.example.huangkuncan.applicationlock.controller;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.transition.Explode;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.huangkuncan.applicationlock.R;
import com.example.huangkuncan.applicationlock.View.View.DivideEditText;
import com.example.huangkuncan.applicationlock.View.View.LockView;
import com.example.huangkuncan.applicationlock.View.View.TopView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by huangkuncan on 2016/4/11.
 * 邮箱：673391138@qq.com
 * 功能： 后台服务，监测软件的启动和安装
 */
public class LockSerivice extends Service {
	private Handler handler = new Handler();
	// 轮询的runnable
	private Runnable pollingRunnable;
	//轮询的间隔
	private static final long interval = 1000;
	private ActivityManager mActivityManager;
	//栈顶的acitivity
	private ComponentName topActivity;
	private String topPackageName;
	private static final String TAG = "hkc";
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mLayoutParams;
	//是否已经弹出了悬浮窗
	private boolean mHaveShowWindow;
	//将自己的app设置为白名单
	private static final String WhiteApp = "com.example.huangkuncan.applicationlock";
	//正在被解锁的app
	private String unLockingApp;
	//解锁界面
	LockView mLockView;
	//是否进入系统解锁
	private boolean mForceUnlock = false;
	//5.0之后获取新打开的app需要
	long time;
	List<UsageStats> stats;
	UsageStats latestUsageStaus;
	UsageStatsManager mUsageStatsManager;

	@Override
	public void onCreate() {
		super.onCreate();
		init();
		initPopView();
		handler.post(pollingRunnable);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	public static void startSerivice(Context context) {
		Intent intent = new Intent(context, LockSerivice.class);
		context.startService(intent);
	}

	public static void stopSerivice(Context context) {
		Intent intent = new Intent(context, LockSerivice.class);
		context.stopService(intent);
	}

	private void init() {
		pollingRunnable = new Runnable() {
			@Override
			public void run() {
				getActivity();
				handler.postDelayed(this, interval);
			}
		};
	}

	private void initPopView() {
		mLockView = new LockView(this);
		mLockView.addTextInputConpleteListener(new LockView.LockViewListener() {
			@Override
			public void textInputComplete(String text) {
				if (text.equals(LockStore.getInstance().getPassword())) {
					removePopView();
				}
				//系统解锁，防止手机被锁死
				else if (text.equals("6102")) {
					if (mForceUnlock) {
						removePopView();
					} else {
						mLockView.clearEditText();
						mForceUnlock = true;
					}
				} else {
					mLockView.clearEditText();
					mLockView.setTopText(getString(R.string.lock_view_remind_input_again));
				}
			}
		});
		mLockView.setTopText(getString(R.string.input_password_before));
		mLayoutParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
				2002, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSPARENT);
		mLockView.setFocusableInTouchMode(true);
		mLockView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				return false;
			}
		});
		mLayoutParams.gravity = Gravity.CENTER;
	}

	/**
	 * 关闭输入密码的悬浮窗
	 */
	private void removePopView() {
		if (mLockView != null && mHaveShowWindow) {
			mWindowManager.removeView(mLockView);
			mHaveShowWindow = false;
//			LockStore.getInstance().clearPackageNameLocked();
		}
	}

	/**
	 * 弹出输入密码的悬浮窗
	 */
	private void showPopView() {
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		}
		if (!mHaveShowWindow) {
			mLockView.setTopText(getString(R.string.input_password_before));
			mLockView.clearEditText();
			mWindowManager.addView(mLockView, mLayoutParams);
			mHaveShowWindow = true;
		}

	}

	/**
	 * 设置弹出的文案与图标
	 */
	private void setViewText() {
		List<PackageInfo> list;
		list = this.getPackageManager().getInstalledPackages(0);
		int length = list.size();
		for (int i = 0; i < length; i++) {
			if (list.get(i).packageName.equals(topPackageName)) {
				//图标
				mLockView.setAppDrawable(list.get(i).applicationInfo.loadIcon(getPackageManager()));
				Log.d(TAG, "setViewText: " + list.get(i).applicationInfo.loadLabel(getPackageManager()));
				//app的名字
				mLockView.setAppName(list.get(i).applicationInfo.loadLabel(getPackageManager()) + "");
			}
		}
	}


	/**
	 * 获取栈顶的activity
	 */
	private void getActivity() {

		//版本大于等于5.0
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			topPackageName = getTopActivtyFromLolipopOnwards();
		} else {
			mActivityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
			topActivity = mActivityManager.getRunningTasks(1).get(0).topActivity;
			topPackageName = topActivity.getPackageName();
		}
		Log.d(TAG, "getActivity: " + topPackageName);
		if (topPackageName == null) {
			return;
		}
		//跳过白名单
		if (!topPackageName.equals(WhiteApp)) {
			//该包名已经被选择加锁
			if (LockAppManager.getInstance().isChoosed(topPackageName)) {
				Log.d(TAG, "unLockingApp   " + unLockingApp);
				if (!TextUtils.equals(unLockingApp,topPackageName)) {
					unLockingApp = topPackageName;
					setViewText();
					//弹出
					showPopView();
				} else {
					//已经弹出悬浮窗，不需要再弹。
				}
			} else if (unLockingApp != null && !topPackageName.equals(unLockingApp)) {
				//用户按下了home键或者返回键
				//待解app已经关闭，关闭解锁界面
//				if(mHaveShowWindow){
					removePopView();
					unLockingApp=null;
//				}

			}
		}
	}

	/**
	 * 如果版本大于等于android5.0，以前获取最新打开的app方法已经失效（google为了安全考虑），所以需要采用新方法获取
	 * 但是这个方法也要5.0起才有效果
	 */
	public String getTopActivtyFromLolipopOnwards() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			if (mUsageStatsManager == null) {
				mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
			}
			time = System.currentTimeMillis();
			//拿到所有的app信息
			stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
			if (stats != null) {
				//根据最晚使用时间找到新打开的app
				latestUsageStaus = null;
				for (UsageStats usageStats : stats) {
					if (latestUsageStaus == null) {
						latestUsageStaus = usageStats;
					} else {
						if (usageStats.getLastTimeUsed() > latestUsageStaus.getLastTimeUsed()) {
							latestUsageStaus = usageStats;
						}
					}
				}
				if (latestUsageStaus == null) return null;
				return latestUsageStaus.getPackageName();
			}
		}
		return null;
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


}
