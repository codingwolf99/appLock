package com.example.huangkuncan.applicationlock.View;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huangkuncan.applicationlock.R;
import com.example.huangkuncan.applicationlock.View.View.TopView;

/**
 * Created by huangkuncan on 2016/5/8.
 * 邮箱：673391138@qq.com
 * 功能：
 */
public class PerssionActivity extends LockBaseActivity {
    private TopView mTopview;
    private TextView mOverTv;
    private TextView mLookTv;
    private static final int OPEN_OVERLAY_WINDOW = 0;
    private static final int LOOK_UP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        initView();
    }

    private void initView(){
        mTopview = (TopView) findViewById(R.id.permission_topview);
        mTopview.setStateText(getString(R.string.permission_setting));
        if (isMiui()) {
            mOverTv = (TextView) findViewById(R.id.permission_over);
            mOverTv.setVisibility(View.VISIBLE);
            mOverTv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    openPopWindow(OPEN_OVERLAY_WINDOW);
                }
            });
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mLookTv = (TextView) findViewById(R.id.permission_look);
            mLookTv.setVisibility(View.VISIBLE);
            mLookTv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    openPopWindow(LOOK_UP);
                }
            });
        }
        findViewById(R.id.permission_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static void startActivity(Context context) {
        //不需要开启权限
        if (!TextUtils.equals(Build.MANUFACTURER, "Xiaomi") && !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP))
            return;
        Intent i = new Intent(context, PerssionActivity.class);
        context.startActivity(i);
    }

    private void openPopWindow(final int flag) {
        View view = LayoutInflater.from(this).inflate(R.layout.popwindow_overlay, null);
        TextView tv = (TextView) view.findViewById(R.id.popwindow_tv);
        if (flag == 0) {
            tv.setText(getString(R.string.guide_open_state));
        } else {
            tv.setText(getString(R.string.guide_look));
        }
        final PopupWindow window = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.showAsDropDown(findViewById(R.id.anchor), 0, -20);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                if (flag == 0) {
                    openOverPermission();
                } else {
                    permissionLook();
                }
            }
        });
    }

    /**
     * 开启查看应用的权限
     */
    private void permissionLook() {
        PackageManager pm = getPackageManager();
        //查看是否具有获取应用运行的权限
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.SYSTEM_OVERLAY_WINDOW", getPackageName()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }

    /**
     * 针对小米手机开启悬浮窗权限
     */
    private void openOverPermission() {
        Intent i = new Intent("miui.intent.action.APP_PERM_EDITOR");
        i.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        i.putExtra("extra_pkgname", this.getPackageName());
        try {
            this.startActivity(i);
        } catch (Exception e) {
        }
    }

    private boolean isMiui() {
        if (TextUtils.equals(Build.MANUFACTURER, "Xiaomi")) {
            return true;
        }
        return false;
    }
}
