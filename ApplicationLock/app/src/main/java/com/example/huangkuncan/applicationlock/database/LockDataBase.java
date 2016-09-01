package com.example.huangkuncan.applicationlock.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkuncan on 2016/4/11.
 * 邮箱：673391138@qq.com
 * 功能：数据库的管理
 */
public class LockDataBase {
    //数据库的管理使用单例模式
    private static LockDataBase Instance;
    //此处传递应用的context进来
    private static Context mContext;
    //数据库引用
    LockDatabaseHelper helper;
    private SQLiteDatabase dbWrite;
    private SQLiteDatabase dbRead;

    public static void initConfig(Context context) {
        mContext = context;
    }

    private LockDataBase() {
        helper = new LockDatabaseHelper(mContext);
    }

    public static LockDataBase getInstance() {
        if (Instance == null) {
            Instance = new LockDataBase();
        }
        return Instance;
    }

    /**
     * @param packageName 新加锁的app
     * @return 是否添加成功
     */
    public boolean add(String packageName) {
        ContentValues values = new ContentValues();
        values.put(LockDatabaseHelper.APP_PACKAGE_NAME, packageName);
        return insert(values);
    }

    /**
     * @param packageNames 新加锁的一组app
     * @return 是否添加成功
     */
    public boolean add(List<String> packageNames) {
        ContentValues values = new ContentValues();
        if (packageNames == null || packageNames.size() == 0) return false;
        int length = packageNames.size();
        for (int i = 0; i < length; i++) {
            values.put(LockDatabaseHelper.APP_PACKAGE_NAME, packageNames.get(i));
            if (!insert(values)) {
                return false;
            }
        }
        return true;
    }

    public void delete(String packageName) {
        if (dbWrite == null) dbWrite = helper.getWritableDatabase();
        dbWrite.delete(LockDatabaseHelper.TABLE_NALE, LockDatabaseHelper.APP_PACKAGE_NAME + "=?", new String[]{packageName});
    }

    public void delete(List<String> packageNames) {
        if (dbWrite == null) dbWrite = helper.getWritableDatabase();
        int length = packageNames.size();
        for (int i = 0; i < length; i++) {
        dbWrite.delete(LockDatabaseHelper.TABLE_NALE, LockDatabaseHelper.APP_PACKAGE_NAME + "=?", new String[]{packageNames.get(i)});
        }
    }

    public List<String> getAll() {
        if (dbRead == null) dbRead = helper.getReadableDatabase();
        Cursor cursor = dbRead.query(false, LockDatabaseHelper.TABLE_NALE, null, null, null, null, null, null, null);
        cursor.moveToFirst();
        int length = cursor.getColumnCount();
        List<String> list=new ArrayList<String>();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex(LockDatabaseHelper.APP_PACKAGE_NAME)));
            cursor.moveToNext();
        }
        return list;
    }

    /**
     * @param packageName
     * @return 数据库中是否已经存在
     */
    private boolean isExist(String packageName) {
        if (dbRead == null) dbRead = helper.getReadableDatabase();
        Cursor cursor = dbRead.query(false, LockDatabaseHelper.TABLE_NALE, null, LockDatabaseHelper.APP_PACKAGE_NAME + "=" + packageName, null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getColumnCount() > 0) {
            return true;
        }
        return false;
    }

    private boolean insert(ContentValues values) {
        if (dbWrite == null) dbWrite = helper.getWritableDatabase();
        long num = dbWrite.insert(LockDatabaseHelper.TABLE_NALE, null, values);
        if (num == -1) {
            //插入数据失败
            return false;
        }
        //插入数据成功
        return true;
    }
}
