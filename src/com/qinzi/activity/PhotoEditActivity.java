package com.qinzi.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

public class PhotoEditActivity extends Activity{
	
	public static final int CODE_NONE = 0;
	public static final int CODE_ACTION_IMAGE_CAPTURE = 1;
	public static final int CODE_ACTION_PICK = 2;
	public static final int CODE_FINISHED = 3;

	private String imagePath = "";
	
	private ImageView photoView;

	private ImageButton goButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_edit);
		photoView = (ImageView) super.findViewById(R.id.photoView);
		goButton = (ImageButton)super.findViewById(R.id.go);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		
		if (extras != null) {
			imagePath = extras.getString("imagePath");
			photoView.setImageDrawable(Drawable.createFromPath(imagePath));
		}
		
		goButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(PhotoEditActivity.this, PhotoUploadActivity.class);
				intent.putExtra("imagePath", imagePath);
				startActivityForResult(intent, PhotoEditActivity.CODE_ACTION_IMAGE_CAPTURE);
			}
		});
		
	}
}
