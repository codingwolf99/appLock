package com.example.huangkuncan.applicationlock.View;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;

import com.example.huangkuncan.applicationlock.R;
import com.example.huangkuncan.applicationlock.controller.LockStore;

/**
 * Created by huangkuncan on 2016/4/22.
 * 邮箱：673391138@qq.com
 * 功能：
 */
public class LaunchActivity extends LockBaseActivity {
	private Runnable mRunnable;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);

		mRunnable = new Runnable() {
			@Override
			public void run() {
				//第一次使用
				if (LockStore.getInstance().getPassword().length() == 6) {

					SetPassWordActivity.startActivity(LaunchActivity.this, SetPassWordActivity.FIRST_TIME_USE);
					finish();
				} else {

					SetPassWordActivity.startActivity(LaunchActivity.this, SetPassWordActivity.CHECK_PASSWORD);
					finish();
				}
			}
		};
		//2秒延时启动
		mHandler.postDelayed(mRunnable, 2000);
	}

	@Override
	public void onBackPressed() {
		//屏蔽返回键
	}
}
