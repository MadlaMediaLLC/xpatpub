package com.han.xpatpub.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.han.xpatpub.R;
import com.han.xpatpub.activity.SettingActivity;
import com.han.xpatpub.model.GlobalData;

public class PubOwnerDialog extends Dialog {

	private Context parent;
	private ImageView imgClose;
	
	private Button btnSearchPatrons;
	private Button btnChangePubIcons;
	
	private ImageView imgPubIcon;

	private TextView txtUserName;
	
	public PubOwnerDialog(Context context) {
		super(context);
		parent = context;
		// TODO Auto-generated constructor stub
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_pub_owner);
		setCanceledOnTouchOutside(false);
		
		initWidget();
		initValue();
		initEvent();
	}
	
	public void initWidget() {
		imgClose = (ImageView) findViewById(R.id.close_image);
		
		txtUserName = (TextView) findViewById(R.id.pub_owner_user_name_textView);
		
		imgPubIcon = (ImageView) findViewById(R.id.pub_info_pub_icon_imageView);
		
		btnSearchPatrons = (Button) findViewById(R.id.search_patrons_button);
		btnChangePubIcons = (Button) findViewById(R.id.change_pub_icon_button);
	}
	
	public void initValue() {
		txtUserName.setText(GlobalData.currentUser.userName);
	}
	
	public void initEvent() {
		imgClose.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				PubOwnerDialog.this.dismiss();
			}
        });
		btnSearchPatrons.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingActivity parentActivity = (SettingActivity) parent;
				parentActivity.startSearchPatron();
				PubOwnerDialog.this.dismiss();
			}
        });
		btnChangePubIcons.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingActivity settingActivity = (SettingActivity) parent;
				settingActivity.captureImage();
			}
        });
	}
	
	public void setCapturedImage(Bitmap bmp) {
		imgPubIcon.setImageBitmap(bmp);
	}
}
