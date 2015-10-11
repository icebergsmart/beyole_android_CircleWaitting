package com.beyole.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.beyole.circlewaitting.R;

public class CustomProgressbar extends View {

	// 设置第一圈颜色
	private int mFirstColor=Color.GREEN;
	// 设置第二圈颜色
	private int mSecondColor=Color.RED;
	// 设置圈的宽度
	private int mCircleWidth=20;
	// 设置颜色填充画笔
	private Paint mPaint;
	// 设置当前进度
	private int mProgress;
	// 设置当前进度加载速度
	private int speed=20;
	// 是否开始下一个
	private boolean isNext = false;

	public CustomProgressbar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomProgressbar(Context context) {
		this(context, null);
	}

	/**
	 * 必要的初始化，获取一些自定义的值
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CustomProgressbar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// 获取自定义的属性集合
		TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomProgressbar, defStyle, 0);
		// 获取自定义属性的个数
		int n = array.getIndexCount();
		Log.i("test", "自定义属性的个数：" + n);
		// 遍历属性值
		for (int i = 0; i < n; i++) {
			int attr = array.getIndex(i);
			Log.i("test", "自定义的属性为:"+attr);
			switch (attr) {
			case R.styleable.CustomProgressbar_firstColor:
				// 获取第一圈颜色值
				mFirstColor = array.getColor(attr, Color.GREEN);
				break;
			case R.styleable.CustomProgressbar_secondColor:
				// 获取第一圈颜色值
				mSecondColor = array.getColor(attr, Color.RED);
				break;
			case R.styleable.CustomProgressbar_circleWidth:
				// 设置默认圈的宽度为20px
				mCircleWidth = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
				break;
			case R.styleable.CustomProgressbar_speed:
				// 获取默认加载速度
				speed = array.getInt(attr, 20);
				break;
			}
		}
		// 回收
		array.recycle();
		mPaint = new Paint();
		// 绘图线程 此线程为耗时线程，放在子线程中执行，防止主线程的卡顿
		new Thread() {
			public void run() {
				while (true) {
					mProgress++;
					if (mProgress == 360) {
						mProgress = 0;
						// 如果没有开始下一个，则设置isNext为true
						if (!isNext) {
							isNext = true;
						} else {
							isNext = false;
						}
					}
					// 刷新UI
					// postInvalidate()此方法可以直接在UI线程调用，invalidate()则需要在handler中进行调用
					postInvalidate();
					try {
						Thread.sleep(speed);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 获取圆心的x坐标
		int center = getWidth() / 2;
		// 获取圆的半径
		int radius = center - mCircleWidth / 2;
		// 设置填充的宽度
		mPaint.setStrokeWidth(mCircleWidth);
		mPaint.setAntiAlias(true);
		// 设置填充的style
		mPaint.setStyle(Paint.Style.STROKE);
		// new RectF(left, top, right, bottom) 为距离x轴，y轴之间的距离
		// 定义rect的形状
		RectF f = new RectF(center - radius, center - radius, center + radius, center + radius);
		if (!isNext) {
			// 第一圈颜色完整，第二圈颜色跑
			mPaint.setColor(mFirstColor);// 设置画笔颜色
			// 画出圆环
			canvas.drawCircle(center, center, radius, mPaint);
			// 设置圆环颜色
			mPaint.setColor(mSecondColor);
			/*
			 * public void drawArc(RectF oval, float startAngle, float sweepAngle,
			 * boolean useCenter, Paint paint) oval :指定圆弧的外轮廓矩形区域。 startAngle:
			 * 圆弧起始角度，单位为度。 sweepAngle: 圆弧扫过的角度，顺时针方向，单位为度。 useCenter:
			 * 如果为True时，在绘制圆弧时将圆心包括在内，通常用来绘制扇形。 paint: 绘制圆弧的画板属性，如颜色，是否填充等。
			 */
			canvas.drawArc(f, -90, mProgress, false, mPaint);
		} else {
			// 第一圈颜色完整，第二圈颜色跑
			mPaint.setColor(mSecondColor);// 设置画笔颜色
			// 画出圆环
			canvas.drawCircle(center, center, radius, mPaint);
			// 设置圆环颜色
			mPaint.setColor(mFirstColor);
			/*
			 * public void drawArc(RectF oval, float startAngle, float sweepAngle,
			 * boolean useCenter, Paint paint) oval :指定圆弧的外轮廓矩形区域。 startAngle:
			 * 圆弧起始角度，单位为度。 sweepAngle: 圆弧扫过的角度，顺时针方向，单位为度。 useCenter:
			 * 如果为True时，在绘制圆弧时将圆心包括在内，通常用来绘制扇形。 paint: 绘制圆弧的画板属性，如颜色，是否填充等。
			 */
			canvas.drawArc(f, -90, mProgress, false, mPaint);
		}
	}
}
