package com.framework.page;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;

public class MainActivity extends ActivityGroup {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup(this.getLocalActivityManager());

		TabHost.TabSpec spec = tabHost.newTabSpec("TAB1");
		spec.setContent(new Intent(this, ListActivity.class));
		spec.setIndicator("首页");
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("TAB2");
		spec.setContent(new Intent(this, HotActivity.class));
		spec.setIndicator("热门");
		tabHost.addTab(spec);
		
		spec = tabHost.newTabSpec("TAB3");
        spec.setContent(new Intent(this, CameraActivity.class));
        spec.setIndicator("照相");
        tabHost.addTab(spec);
        
        spec = tabHost.newTabSpec("TAB4");
        spec.setContent(new Intent(this, AccountActivity.class));
        spec.setIndicator("个人");
        tabHost.addTab(spec);

		tabHost.setCurrentTab(1);
	}
}