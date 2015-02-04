package com.han.xpatpub.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.han.xpatpub.R;
import com.han.xpatpub.asynctasks.GeneralAsyncTask;
import com.han.xpatpub.model.Action;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.Message;
import com.han.xpatpub.utility.DialogUtility;
import com.squareup.picasso.Picasso;

public class MessageActivity extends Activity {
	
	private ImageView imgProfile;
	private TextView txtUserName;
	private Button btnBack;
	
	private TextView txtMsgText;
	private TextView txtMsgDescription;
	private Button btnUse;
	private ImageView imgMsgIcon;
	private EditText edtCouponCode;
	
	public static Message curMessage = new Message();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
	    
		initWidget();
		initValue();
		initEvent();
	}
	
	private void initWidget() {
		imgProfile = (ImageView) findViewById(R.id.my_profile_imageView);
		txtUserName = (TextView) findViewById(R.id.myname_textView);
		txtMsgDescription = (TextView) findViewById(R.id.row_desc_textView);
		btnBack = (Button) findViewById(R.id.pub_patron_back_button);
		
		txtMsgText = (TextView) findViewById(R.id.row_name_textView);
		btnUse = (Button) findViewById(R.id.change_pub_owner_icon_button);
		imgMsgIcon = (ImageView) findViewById(R.id.pub_owner_icon_imageView);
		
		edtCouponCode = (EditText) findViewById(R.id.coupon_code_editText);
	}
	
	private void initValue() {
		txtUserName.setText(GlobalData.currentUser.firstName + " " + GlobalData.currentUser.lastName);
		imgProfile.setImageBitmap(GlobalData.bmpProfile);
		
		txtMsgText.setText(curMessage.ownPub.name);
		txtMsgDescription.setText(curMessage.ownCoupon.couponDescription);
		Picasso.with(MessageActivity.this).load(curMessage.ownPub.iconUrl).into(imgMsgIcon);
	}
	
	private void initEvent() {
        btnUse.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isCheckCouponCode()) {
					new GeneralAsyncTask(MessageActivity.this).execute(Action.ACTION_MARK_MESSAGE, Integer.toString(curMessage.msgID));
				}
			}
        });
        
        btnBack.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
        });
	}
	
	private boolean isCheckCouponCode() {
		String strCode = edtCouponCode.getText().toString();
		
		if (strCode.isEmpty()) {
			DialogUtility.showGeneralAlert(this, "Error", "Please input code");
			return false;
		}
		
		if (!strCode.equals(curMessage.ownCoupon.couponCode)) {
			DialogUtility.showGeneralAlert(this, "Error", "This code is invalid");
			return false;
		}
		
		return true;
	}
	
	public void successMarkMessage() {
		new AlertDialog.Builder(this).setTitle("Success")
				.setMessage("Coupon has been used. Thanks")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// continue with delete
						finish();
					}
				}).show();
	}
}
