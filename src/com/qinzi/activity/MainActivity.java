package com.qinzi.activity;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;

import com.qinzi.dialog.CommonActivityDialog;
import com.qinzi.dialog.DialogFactory;

public class MainActivity extends ActivityGroup {
	
	private CommonActivityDialog dialog;
	
	private Button cameraButton;
	
	private Button albumButton;
	
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
		
		ImageButton camera = (ImageButton)super.findViewById(R.id.camera);
		dialog = DialogFactory.getInstance().getCameraDialog(MainActivity.this);
		cameraButton = (Button) dialog.getDialog().findViewById(R.id.cameraButton);
		albumButton = (Button) dialog.getDialog().findViewById(R.id.albumButton);
		
		camera.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});	
		cameraButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				startActivityForResult(intent, 0);
			}
		});
		albumButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
				intent.setType("image/*");
				startActivityForResult(intent, 1);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != Activity.RESULT_OK) {
			return;
		}
		Uri uri = data.getData();
		switch (requestCode) {
			case 0:
			case 1:
			default:
		}
		Intent intent =  new Intent(dialog.getDialog().getContext(), PhotoActivity.class);
		intent.setData(uri);
		this.startActivity(intent); 

	}

}