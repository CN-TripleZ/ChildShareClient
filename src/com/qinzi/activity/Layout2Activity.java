package com.qinzi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

public class Layout2Activity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout2);
        
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.LoginLayout);
        Animation translate=AnimationUtils.loadAnimation(Layout2Activity.this, R.anim.translate);
        layout.startAnimation(translate);
        
        Button login_btn = (Button)findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Layout2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}