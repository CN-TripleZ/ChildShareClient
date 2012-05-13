package com.qinzi.activity;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;

import com.qinzi.dialog.CommonActivityDialog;
import com.qinzi.dialog.DialogFactory;
import com.qinzi.util.Utils;

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
		/*
		spec = tabHost.newTabSpec("TAB3");
        spec.setContent(new Intent(this, CameraActivity.class));
        spec.setIndicator("照相");
        tabHost.addTab(spec);
        */
        spec = tabHost.newTabSpec("TAB4");
        spec.setContent(new Intent(this, AccountActivity.class));
        spec.setIndicator("个人");
        tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
		
		ImageButton camera = (ImageButton)super.findViewById(R.id.tab_camera);
		dialog = DialogFactory.getInstance().getCameraDialog(MainActivity.this);
		cameraButton = (Button) dialog.getDialog().findViewById(R.id.cameraButton);
		albumButton = (Button) dialog.getDialog().findViewById(R.id.albumButton);
		
		camera.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				dialog.show();
			}
		});	
		cameraButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
				startActivityForResult(intent, PhotoEditActivity.CODE_ACTION_IMAGE_CAPTURE);
			}
		});
		albumButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, PhotoEditActivity.CODE_ACTION_PICK);
			}
		});
		
		ImageButton tab_home = (ImageButton)super.findViewById(R.id.tab_home);
		tab_home.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				TabHost tab = (TabHost) findViewById(R.id.tabhost);
				tab.setCurrentTab(0);
			}
		});
		ImageButton tab_hot = (ImageButton)super.findViewById(R.id.tab_hot);
		tab_hot.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				TabHost tab = (TabHost) findViewById(R.id.tabhost);
				tab.setCurrentTab(1);
			}
		});
		ImageButton tab_account = (ImageButton)super.findViewById(R.id.tab_account);
		tab_account.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				TabHost tab = (TabHost) findViewById(R.id.tabhost);
				tab.setCurrentTab(2);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PhotoEditActivity.CODE_NONE)
			return;
		
		if (resultCode != Activity.RESULT_OK)
			return;
		
		if (requestCode == PhotoEditActivity.CODE_ACTION_IMAGE_CAPTURE) {
			File picture = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
			cropPhoto(Uri.fromFile(picture));
		}
		
		if (data == null)
			return;
		
		Uri uri = data.getData();
		if (requestCode == PhotoEditActivity.CODE_ACTION_PICK) {
			cropPhoto(uri);
		}

		if (requestCode == PhotoEditActivity.CODE_FINISHED) {
			String imagePath = "";
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				try {
					String filePath = getApplicationContext().getFilesDir().getPath() + File.separator + "jeffreyzhang";
					String fileName = System.currentTimeMillis() + ".jpg";
					imagePath = Utils.saveTempFile(photo, filePath, fileName);
					System.out.println(imagePath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, PhotoEditActivity.class);
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("imagePath", imagePath);
			startActivity(intent);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 128);
		intent.putExtra("outputY", 128);
		intent.putExtra("scale", true);    
		intent.putExtra("return-data", true);//如果返回uri需注释掉
//		String filePath = getApplicationContext().getFilesDir().getPath() + File.separator + "jeffreyzhang";
//		String fileName = System.currentTimeMillis() + ".jpg";
//		File outputFile = new File(filePath, fileName);
//		if (!outputFile.exists()) {
//			outputFile.mkdirs();
//		}
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
//		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, PhotoEditActivity.CODE_FINISHED);
	}

}
