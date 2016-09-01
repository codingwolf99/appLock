package com.example.huangkuncan.applicationlock.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkuncan on 2016/4/11.
 * 邮箱：673391138@qq.com
 * 功能：
 */
public class LockStore {
	/**
	 * 正在要求输入的密码的app
	 */
	private String mPackageNameLocked;
	/**
	 * 存储被选择加锁的应用的包名
	 */
	private List<String> listChoosed = new ArrayList<String>();

	public List<String> getListUnlock() {
		return listUnlock;
	}

	public void setListUnlock(List<String> listUnlock) {
		this.listUnlock = listUnlock;
	}

	/**
	 * 存储已经加锁但现在需要解锁的应用的包名
	 */
	private List<String> listUnlock = new ArrayList<String>();
	private PackageManager getApplication;

	/**
	 * @param packageNameLocked
	 * 设置正在要求输入密码的包名
	 */
	public void setPackageNameLocked(String packageNameLocked) {
		mPackageNameLocked = packageNameLocked;
	}

	public String getPackageNameLocked() {
		return mPackageNameLocked;
	}

	public void clearPackageNameLocked() {
		mPackageNameLocked = null;
	}

	/**
	 * @param text 存储密码
	 */
	public void setPassword(String text) {
		SharedPreferences sp = LockAppication.context.getSharedPreferences("lock", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("password", text);
		editor.commit();
	}


	/**
	 * @return 获取密码
	 */
	public String getPassword() {
		SharedPreferences sp = LockAppication.context.getSharedPreferences("lock", Context.MODE_PRIVATE);
		//如果没有设置密码就返回6个0
		return sp.getString("password", "000000");
	}

	public List<String> getListChoosed() {
		return listChoosed;
	}

	private LockStore() {
	}

	public void lock(String packageName) {
		listChoosed.add(packageName);
		if (listUnlock.contains(packageName)) {
			listUnlock.remove(packageName);
		}
	}

	public void unlock(String packageName) {
		if (listChoosed.contains(packageName)) {
			listChoosed.remove(packageName);
		} else {
			listUnlock.add(packageName);
		}
	}

	private static LockStore Instance;


	public PackageManager getGetApplication() {
		return getApplication;
	}

	public void setGetApplication(PackageManager getApplication) {
		this.getApplication = getApplication;
	}

	public static LockStore getInstance() {
		if (Instance == null) {
			Instance = new LockStore();
		}

		return Instance;
	}

}
