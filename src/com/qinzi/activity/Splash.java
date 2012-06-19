package com.qinzi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class Splash extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 取消标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		this.setContentView(R.layout.splash);
		// 取消状态栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		ImageView iv = (ImageView) findViewById(R.id.logo);

		AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
		aa.setDuration(200);
		iv.startAnimation(aa);

		aa.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				Intent it = new Intent(Splash.this, Layout2Activity.class);
				startActivity(it);
				finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
}
