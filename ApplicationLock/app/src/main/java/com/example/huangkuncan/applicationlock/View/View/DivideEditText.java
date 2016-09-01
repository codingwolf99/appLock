package com.example.huangkuncan.applicationlock.View.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.example.huangkuncan.applicationlock.R;


/**
 * Created by huangkuncan on 2016/4/8.
 * 邮箱：huangkuncan@didichuxing.com
 */
public class DivideEditText extends EditText {
	private String TAG = "hkc";
	private Context context;
	private int length;
	//数字的个数
	private int textNum;
	//边框的颜色
	private int boardColor;
	//字体颜色
	private int textColor;
	//view的宽度
	private float width;
	private String text;
	//已经输入的个数
	private int textSize;
	private RectF rect;
	private Paint paint;
	private String mTextToDraw;
	private int TotalWidth;

	public DivideEditText(Context context) {
		super(context);
		this.context = context;
	}

	public DivideEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initData(attrs);
	}


	public DivideEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		initData(attrs);
	}

	/**
	 * @return 输入框总共可以输入的个数
	 */
	public int getTextNum() {
		return textNum;
	}

	private void initData(AttributeSet attrs) {
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DivideEditText);
		//默认是接收四个输入
		textNum = array.getInt(R.styleable.DivideEditText_textLenth, 4);
		textColor = array.getColor(R.styleable.DivideEditText_textColor, Color.BLACK);
		boardColor = array.getColor(R.styleable.DivideEditText_boardColor, Color.BLACK);
		width = array.getDimension(R.styleable.DivideEditText_viewWidth, 400);
		array.recycle();
		paint = new Paint();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		length = length();
		width = getWidth();
		paint.setColor(boardColor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);
		rect = new RectF(0, 0, getWidth(), getHeight());
		canvas.drawRoundRect(rect, 1, 1, paint);
		for (int i = 0; i < textNum; i++) {
			canvas.drawLine(width * i / textNum, 0, width * i / textNum, getHeight(), paint);
		}
		text = getText().toString();
		textSize = text.length();
		paint.setColor(textColor);
		paint.setTextSize(getTextSize());
		for (int i = 0; i < textSize; i++) {
			canvas.drawText(text, i, i + 1, width * i / textNum + width * 2 / (5 * textNum), getHeight() * 2 / 3, paint);
		}
	}
}
