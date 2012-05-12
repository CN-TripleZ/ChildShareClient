package com.qinzi.dialog;

import android.content.Context;

import com.qinzi.activity.R;

public class DialogFactory {
	private final static DialogFactory instance = new DialogFactory();

	public static DialogFactory getInstance() {
		return instance;
	}

	public CommonActivityDialog getCameraDialog(Context context) {
		CommonActivityDialog dialog = new CommonActivityDialog(context, R.style.dialog, R.layout.camera_dialog);
		return dialog;
	}
}
