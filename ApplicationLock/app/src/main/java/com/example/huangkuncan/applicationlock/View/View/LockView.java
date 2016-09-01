package com.example.huangkuncan.applicationlock.View.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.huangkuncan.applicationlock.R;

/**
 * Created by huangkuncan on 2016/4/14.
 * 邮箱：673391138@qq.com
 * 功能：软件解锁界面
 */
public class LockView extends LinearLayout {
	private Context context;
	private LayoutInflater inflater;
	private DivideEditText mDivideEidtText;
	private TopView mTopView;
	private LockViewListener mListener;

	public interface LockViewListener {
		void textInputComplete(String text);
	}

	/**
	 * @param Listener 添加密码输入框输入完毕的监听器
	 */
	public void addTextInputConpleteListener(LockViewListener Listener) {
		mListener = Listener;
	}

	public LockView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public LockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public LockView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		init();
	}

	/**
	 * 将输入框数据清空
	 */
	public void clearEditText() {
		if (mDivideEidtText == null) return;
		mDivideEidtText.setText("");
	}
	/**
	 * @param drawable 设置app的图标
	 */
	public void setAppDrawable(Drawable drawable) {

		mTopView.setImage(drawable);
	}

	/**
	 * @param s 设置图标下面的文字，比如app的名字
	 */
	public void setAppName(String s) {
		mTopView.setNameText(s);
	}

	/**
	 * @param s 设置说明性的那一段话
	 */
	public void setTopText(String s) {
		mTopView.setStateText(s);
	}

	private void init() {
		inflater = LayoutInflater.from(context);
		View view = inflate(context, R.layout.full_screen_input_password, this);
		mDivideEidtText = (DivideEditText) view.findViewById(R.id.full_screen_input_password_et);
		mTopView = (TopView) view.findViewById(R.id.full_screen_input_password_topview);
		this.setGravity(Gravity.CENTER_HORIZONTAL);
		this.setOrientation(VERTICAL);
		this.setBackgroundColor(Color.WHITE);
		setTextCompleteInputListener();
	}

	private void setTextCompleteInputListener() {
		mDivideEidtText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				if (mDivideEidtText.getTextNum() == mDivideEidtText.getText().length()) {
					//输入完毕
					if (mListener != null) {
						mListener.textInputComplete(mDivideEidtText.getText().toString());
					}
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}
}
