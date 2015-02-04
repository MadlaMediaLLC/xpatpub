package com.han.xpatpub.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.han.xpatpub.R;

public class SettingDialog extends Dialog {

	private Context parent;
	private ImageView imgClose;

	public SettingDialog(Context context) {
		super(context);
		parent = context;
		// TODO Auto-generated constructor stub
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.dialog_setting);
		setCanceledOnTouchOutside(false);
//		setTitle("Settings");
		
		initWidget();
		initValue();
		initEvent();
	}
	
	public void initWidget() {
		imgClose = (ImageView) findViewById(R.id.close_image);
	}
	
	public void initValue() {
		
	}
	
	public void initEvent() {
		imgClose.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingDialog.this.dismiss();
			}
        });
	}
}
