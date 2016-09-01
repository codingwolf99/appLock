package com.example.huangkuncan.applicationlock.View;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.huangkuncan.applicationlock.R;
import com.example.huangkuncan.applicationlock.View.View.TopView;
import com.example.huangkuncan.applicationlock.controller.LockAppManager;
import com.example.huangkuncan.applicationlock.controller.LockGridViewAdapter;
import com.example.huangkuncan.applicationlock.controller.LockSerivice;
import com.example.huangkuncan.applicationlock.controller.LockStore;
import com.example.huangkuncan.applicationlock.controller.LockViewPagerAdapter;
import com.example.huangkuncan.applicationlock.controller.Utils;
import com.example.huangkuncan.applicationlock.model.LockAppInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends LockBaseActivity {
	private ViewPager mViewpager;
	private RelativeLayout rl;
	private LinearLayout ll;
	private int mPageNum = 2;
	private static final int column_num=4;
	private static final int mGridHaveAppNum = 3*column_num;
	private LockViewPagerAdapter mViewPagerAdapter;
	private LockGridViewAdapter mGridViewAdapter;
	private List<View> mGridViews = new ArrayList<View>();
	private TextView mNextBtn;
	private TopView mTopViwe;
	private static final String TAG = "hkc";
	private boolean mNeedCheckPasswod;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
//		permissionManager();
		setGridView();
		setmViewpagerAdapter();
		setIndicator();
		//配置信息
		LockStore.getInstance().setGetApplication(getPackageManager());

		mNeedCheckPasswod = false;
	}

	@Override
	protected void onResume() {
//		permissionManager();
		super.onResume();
	}


	public static void startActivity(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		//关闭后台
//        LockSerivice.stopSerivice(this);
		super.onDestroy();
	}

	@Override
	protected void onStart() {
//按返回键或者home键退出后进入也需要输入密码
		if (mNeedCheckPasswod) {
			mNeedCheckPasswod = false;
			SetPassWordActivity.startActivity(this, SetPassWordActivity.CHECK_PASSWORD);
		}
		super.onStart();
	}

	@Override
	protected void onStop() {
		mNeedCheckPasswod = true;
		super.onStop();
	}

	/**
	 * 设置gridview的数据信息
	 */
	private void setGridView() {
		//获取所有的应用信息
		List<LockAppInfo> list = LockAppManager.getInstance().getPackageInfos();
		int num = list.size();
		int start = 0;
		//计算页数
		mPageNum = num % mGridHaveAppNum == 0 ? num / mGridHaveAppNum : num / mGridHaveAppNum + 1;
		for (int i = 0; i < mPageNum; i++) {
			GridView gridView = new GridView(this);
			gridView.setNumColumns(column_num);
			gridView.setHorizontalSpacing(Utils.px2dip(this,20f));
			gridView.setGravity(Gravity.CENTER);
			LockGridViewAdapter adapter = new LockGridViewAdapter(this, generateList(list, start, start + mGridHaveAppNum <= num ? start + mGridHaveAppNum : num));
			gridView.setAdapter(adapter);
			mGridViews.add(gridView);
			start += mGridHaveAppNum;
		}

	}

	/**
	 * 现在采用的手段是对list进行截取
	 *
	 * @param list
	 * @param start 截取的起点
	 * @param stop  截取的终点（此点不截取）
	 * @return
	 */
	private List<LockAppInfo> generateList(List<LockAppInfo> list, int start, int stop) {
		List<LockAppInfo> newList = new ArrayList<LockAppInfo>();
		for (int i = start; i < stop; i++) {
			newList.add(list.get(i));
		}
		return newList;
	}

	/**
	 * 小圆点相关的代码
	 */
	private void setIndicator() {
		ImageView[] views = new ImageView[mPageNum];
		//设置小圆点之间的间距
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		param.leftMargin = Utils.dip2px(this, 4);
		param.rightMargin = Utils.dip2px(this, 4);
		for (int i = 0; i < mPageNum; i++) {
			ImageView iv = new ImageView(this);
			if (i == 0) {
				iv.setImageResource(R.mipmap.indicator_choosed);
			} else {
				iv.setImageResource(R.mipmap.indicator_unchoosed);
			}

			ll.addView(iv, param);
		}
		ll.setGravity(Gravity.CENTER);
		mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				for (int i = 0; i < mPageNum; i++) {
					if (position == i) {
						((ImageView) ll.getChildAt(i)).setImageResource(R.mipmap.indicator_choosed);
					} else {
						((ImageView) ll.getChildAt(i)).setImageResource(R.mipmap.indicator_unchoosed);
					}
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	private void setmViewpagerAdapter() {
		LockViewPagerAdapter viewPagerAdapter = new LockViewPagerAdapter(mGridViews);
		mViewpager.setAdapter(viewPagerAdapter);
	}

	private void initView() {
		mViewpager = (ViewPager) findViewById(R.id.main_activity_viewpager);
		rl = (RelativeLayout) findViewById(R.id.main_activity_rl);
		ll = (LinearLayout) findViewById(R.id.main_activity_ll);
		mNextBtn = (TextView) findViewById(R.id.main_activity_btn_next);
		mNextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LockAppManager.getInstance().lock(LockStore.getInstance().getListChoosed());
				LockAppManager.getInstance().unlock(LockStore.getInstance().getListUnlock());
				finish();
				SetSuccessActivity.startActivity(MainActivity.this, SetSuccessActivity.AFTER_SET_APP);
			}
		});
		mTopViwe = (TopView) findViewById(R.id.main_activity_topview);
		mTopViwe.setStateText(getResources().getString(R.string.main_activity_state));
	}
}
