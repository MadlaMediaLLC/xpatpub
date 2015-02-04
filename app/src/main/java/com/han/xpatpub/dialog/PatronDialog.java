package com.han.xpatpub.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.han.xpatpub.R;
import com.han.xpatpub.activity.SettingActivity;
import com.han.xpatpub.asynctasks.UserAsyncTask;
import com.han.xpatpub.model.Action;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.User;

public class PatronDialog extends Dialog {

	private Activity parent;
	private ImageView imgClose;
	
	private LinearLayout llytStealth;
	private LinearLayout llytAnonymous;
	private LinearLayout llytPublic;
	
	private ImageView imgStealth;
	private ImageView imgAnonymous;
	private ImageView imgPublic;
	
	private Button btnInbox;
	
	private TextView txtUserType;
	private TextView txtUserName;
	private TextView txtBarsFounded;
	private TextView txtNumberMessage;
	
	public PatronDialog(Activity context) {
		super(context);
		parent = context;
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_patron);
		setCanceledOnTouchOutside(false);
		
		initWidget();
		initValue();
		initEvent();
	}
	
	public void initWidget() {
		imgClose = (ImageView) findViewById(R.id.close_image);
		
		llytStealth = (LinearLayout) findViewById(R.id.western_menu_linearLayout);
		llytAnonymous = (LinearLayout) findViewById(R.id.taphouse_menu_linearLayout);
		llytPublic = (LinearLayout) findViewById(R.id.irish_menu_linearLayout);
		
		txtUserType = (TextView) findViewById(R.id.inbox_pubName_textView);
		txtUserName = (TextView) findViewById(R.id.coupon_desc_textView);
		txtBarsFounded = (TextView) findViewById(R.id.pub_owner_user_name_textView);
		txtNumberMessage = (TextView) findViewById(R.id.number_of_msg_textView);
		
		imgStealth = (ImageView) findViewById(R.id.stealth_imageView);
		imgAnonymous = (ImageView) findViewById(R.id.anonymous_imageView);
		imgPublic = (ImageView) findViewById(R.id.public_imageView);
		
		btnInbox = (Button) findViewById(R.id.inbox_button);
	}
	
	public void initValue() {
		txtUserType.setText(User.getLabelPubType(GlobalData.currentUser.userType));
		txtUserName.setText(GlobalData.currentUser.userName);
		txtBarsFounded.setText(Integer.toString(GlobalData.arrMyPub.size()));
		txtNumberMessage.setText(Integer.toString(GlobalData.arrMyMessage.size()));
	}
	
	public void initEvent() {
		imgClose.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				PatronDialog.this.dismiss();
			}
        });
		
		llytStealth.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				setRadioOn(imgStealth);
				GlobalData.currentUser.userPrivacy = "1";
				new UserAsyncTask(parent).execute(Action.ACTION_UPDATE_USER_PRIVACY, "1");
			}
        });
		
		llytAnonymous.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				setRadioOn(imgAnonymous);
				GlobalData.currentUser.userPrivacy = "2";
				new UserAsyncTask(parent).execute(Action.ACTION_UPDATE_USER_PRIVACY, "2");
			}
        });
		
		llytPublic.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				setRadioOn(imgPublic);
				GlobalData.currentUser.userPrivacy = "3";
				new UserAsyncTask(parent).execute(Action.ACTION_UPDATE_USER_PRIVACY, "3");
			}
        });
		
		btnInbox.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingActivity parentActivity = (SettingActivity) parent;
				parentActivity.startInbox();
				PatronDialog.this.dismiss();
			}
        });
	}
	
	public void refreshAllRadio() {
		imgStealth.setImageResource(R.drawable.radio_off);
		imgAnonymous.setImageResource(R.drawable.radio_off);
		imgPublic.setImageResource(R.drawable.radio_off);
	}
	
	private void setRadioOn(ImageView imgView) {
		refreshAllRadio();
		imgView.setImageResource(R.drawable.radio_on);
	}
}
