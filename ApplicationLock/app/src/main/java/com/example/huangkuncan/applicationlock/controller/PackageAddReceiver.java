package com.example.huangkuncan.applicationlock.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.huangkuncan.applicationlock.R;
import com.example.huangkuncan.applicationlock.controller.LockAppManager;
import com.example.huangkuncan.applicationlock.controller.LockAppication;

import java.util.List;

/**
 * Created by huangkuncan on 2016/4/14.
 * 邮箱：673391138@qq.com
 * 功能：监听软件的安装
 */
public class PackageAddReceiver extends BroadcastReceiver {
	private WindowManager mWindowManger;
	private WindowManager.LayoutParams mLayoutParams;
	private View mDialogView;
	private TextView mTextAdd;
	private TextView mTextCancel;
	private TextView mTextAppName;
	private static final String TAG = "hkc";
	//安装的app的包名
	private String mNewAddAppName;
	private LinearLayout.LayoutParams mDialogParams;

	public PackageAddReceiver() {
		super();
	}

	private int mSreenWidth;
	private int mSreenHeight;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("hkc", "onReceive: 得到的intent " + intent);
		switch (intent.getAction()){
			case "android.intent.action.PACKAGE_ADDED":
				String tmp = intent.getDataString();
//			Log.d(TAG, "onReceive:  mNewAddAppName  "+tmp.substring(8,tmp.length()));
				mNewAddAppName = tmp.substring(8, tmp.length()).trim();
				initView();
				setViewText();
				show();
				break;
			case "android.intent.action.BOOT_COMPLETED":
				Log.d(TAG, "onReceive: 开机完成");
				Intent intent1=new Intent(context,LockSerivice.class);
				context.startService(intent1);
			break;
		}



	}

	private void initView() {
		mWindowManger = (WindowManager) LockAppication.context.getSystemService(Context.WINDOW_SERVICE);
		mSreenHeight = mWindowManger.getDefaultDisplay().getHeight();
		mSreenWidth = mWindowManger.getDefaultDisplay().getWidth();
		mDialogView = LayoutInflater.from(LockAppication.context).inflate(R.layout.dialog_add_lock, null);
		mTextAdd = (TextView) mDialogView.findViewById(R.id.dialog_add_lock_add);
		mTextCancel = (TextView) mDialogView.findViewById(R.id.dialog_add_lock_cancel);
		mTextAppName = (TextView) mDialogView.findViewById(R.id.dialog_add_lock_appName);
		mLayoutParams = new WindowManager.LayoutParams(mSreenWidth - Utils.dip2px(LockAppication.context, 25), mSreenHeight / 3,
				2002, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSPARENT);

		mLayoutParams.gravity = Gravity.CENTER;
		setOnClickListener();
	}

	/**
	 * 设置弹出的文案与图标
	 */
	private void setViewText() {
		List<PackageInfo> list;
		list = LockAppication.context.getPackageManager().getInstalledPackages(0);
		int length = list.size();
		for (int i = 0; i < length; i++) {
			if (list.get(i).packageName.equals(mNewAddAppName)) {
				//app的名字
				String appName = list.get(i).applicationInfo.loadLabel(LockAppication.context.getPackageManager()).toString();
				if (appName == null || appName.length() == 0) {
					appName = "新安装的软件";
				}
				mTextAppName.setText("需要将" + appName + "上锁吗");
				Log.d(TAG, "setViewText: " + "拿到名字");
			}
		}
	}

	private void setOnClickListener() {
		mTextAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//将该应用加锁
				LockAppManager.getInstance().lock(mNewAddAppName);
				dismisss();
			}
		});
		mTextCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//不加锁
				dismisss();
			}
		});
	}

	private void show() {
		Log.d("hkc", "show: ");
		mWindowManger.addView(mDialogView, mLayoutParams);
	}

	private void dismisss() {
		mWindowManger.removeView(mDialogView);
	}
}
