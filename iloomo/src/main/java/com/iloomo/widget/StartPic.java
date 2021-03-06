package com.iloomo.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.iloomo.paysdk.R;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
/***
 * 启动页
 *
 * @author wpt
 *
 */
public class StartPic extends FrameLayout {

	private ImageView start_pic_image;
	private Context context;
	private AnimatorSet set;
	private int Duration=5000;
	public StartPic(Context context) {
		super(context);
		init(context);
	}

	public StartPic(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		// TODO Auto-generated method stub
		this.context = context;
		View StartPicView = LayoutInflater.from(context).inflate(
				R.layout.startpic_layout, null);
		start_pic_image = (ImageView) StartPicView
				.findViewById(R.id.start_pic_image);

		set = new AnimatorSet();
		set.playTogether(
				ObjectAnimator.ofFloat(start_pic_image, "alpha", 0, 0.25f, 1)
		);
		set.setDuration(Duration).start();
		addView(StartPicView);
	}

	/***
	 *
	 * 设置背景图片
	 *
	 * @param id
	 */
	@SuppressLint("NewApi")
	public void setStartPicImage(int id) {
		start_pic_image.setBackgroundResource(id);
	}

	/***
	 *
	 * 设置动画监听
	 *
	 * @param animationListener
	 */
	public void setAnimationListener(AnimatorListener animationListener){
		set.addListener(animationListener);
	}


	/***
	 *
	 * 设置动画时长
	 *
	 * @param Duration
	 */
	public void setDuration(int Duration){
		this.Duration=Duration;
	}


}
