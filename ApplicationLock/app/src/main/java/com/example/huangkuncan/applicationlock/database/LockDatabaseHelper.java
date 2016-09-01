package com.example.huangkuncan.applicationlock.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by huangkuncan on 2016/4/11.
 * 邮箱：673391138@qq.com
 * 功能：数据库的建立
 */
public class LockDatabaseHelper extends SQLiteOpenHelper {
    private static  final int version=1;
    private static final String DBNAME="apps.db";
    public static final String APP_PACKAGE_NAME="app_package_name";
    public static final String TABLE_NALE="pack";
    private static final String CREAT_TBALE="create table pack"+"(_id integer primary key autoincrement,app_package_name varchar(80))";
    public LockDatabaseHelper(Context context) {
        super(context, DBNAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREAT_TBALE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
