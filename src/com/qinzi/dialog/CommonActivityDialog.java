package com.qinzi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

public class CommonActivityDialog {

	private Dialog dialog;
	
	public CommonActivityDialog(Context context, int style, int layoutId) {
		dialog = new Dialog(context, style);
		dialog.setContentView(layoutId);
		dialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 1);
	}

	public void show() {
		dialog.show();
	}

	public void hide() {
		dialog.hide();
	}
	
	public void dismiss() {
		dialog.dismiss();
	}
	
	public Dialog getDialog() {
		return dialog;
	}
}
