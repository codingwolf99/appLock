package com.example.huangkuncan.applicationlock.View;

import android.app.Activity;
import android.view.MotionEvent;

import com.bugtags.library.Bugtags;

/**
 * Created by huangkuncan on 2016/4/25.
 * 邮箱：673391138@qq.com
 * 功能：
 */
public class LockBaseActivity extends Activity{
	@Override
	protected void onResume() {
		super.onResume();
		//注：回调 1
		Bugtags.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		//注：回调 2
		Bugtags.onPause(this);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		//注：回调 3
		Bugtags.onDispatchTouchEvent(this, event);
		return super.dispatchTouchEvent(event);
	}
}

