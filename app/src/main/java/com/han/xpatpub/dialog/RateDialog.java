package com.han.xpatpub.dialog;

import com.han.xpatpub.R;
import com.han.xpatpub.activity.ReviewActivity;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class RateDialog extends Dialog {

	private Context parent;
	
	private LinearLayout llytRate1;
	private LinearLayout llytRate2;
	private LinearLayout llytRate3;
	private LinearLayout llytRate4;
	private LinearLayout llytRate5;
	
	private ImageView imgRadioRate1;
	private ImageView imgRadioRate2;
	private ImageView imgRadioRate3;
	private ImageView imgRadioRate4;
	private ImageView imgRadioRate5;

	private Button btnSubmitRating;
	public int nRate;
	
	public RateDialog(Context context) {
		super(context);
		parent = context;
		
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.dialog_rate);
		setCanceledOnTouchOutside(false);
		setTitle("Rate");
		
		initWidget();
		initValue();
		initEvent();
	}
	
	public void initWidget() {
		btnSubmitRating = (Button) findViewById(R.id.coupon_button2);
		
		llytRate1 = (LinearLayout) findViewById(R.id.rate1_layout);
		llytRate2 = (LinearLayout) findViewById(R.id.rate2_layout);
		llytRate3 = (LinearLayout) findViewById(R.id.rate3_layout);
		llytRate4 = (LinearLayout) findViewById(R.id.rate4_layout);
		llytRate5 = (LinearLayout) findViewById(R.id.rate5_layout);
		
		imgRadioRate1 = (ImageView) findViewById(R.id.pub_info_pub_icon_imageView);
		imgRadioRate2 = (ImageView) findViewById(R.id.rate2_radio_image);
		imgRadioRate3 = (ImageView) findViewById(R.id.rate3_radio_image);
		imgRadioRate4 = (ImageView) findViewById(R.id.rate4_radio_image);
		imgRadioRate5 = (ImageView) findViewById(R.id.rate5_radio_image);
	}
	
	public void initValue() {
//		nRate = 5;
		refreshRateRadio();
//		imgRadioRate5.setImageResource(R.drawable.radio_on);
	}
	
	public void initEvent() {
		llytRate1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshRateRadio();
				imgRadioRate1.setImageResource(R.drawable.radio_on);
				nRate = 1;
			}
        });
		llytRate2.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshRateRadio();
				imgRadioRate2.setImageResource(R.drawable.radio_on);
				nRate = 2;
			}
        });
		llytRate3.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshRateRadio();
				imgRadioRate3.setImageResource(R.drawable.radio_on);
				nRate = 3;
			}
        });
		llytRate4.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshRateRadio();
				imgRadioRate4.setImageResource(R.drawable.radio_on);
				nRate = 4;
			}
        });
		llytRate5.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshRateRadio();
				imgRadioRate5.setImageResource(R.drawable.radio_on);
				nRate = 5;
			}
        });
		
		btnSubmitRating.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				ReviewActivity critiqueActivity = (ReviewActivity) parent;
				critiqueActivity.setRate(nRate);
				RateDialog.this.hide();
			}
        });
	}
	
	public void refreshRateRadio() {
		imgRadioRate1.setImageResource(R.drawable.radio_off);
		imgRadioRate2.setImageResource(R.drawable.radio_off);
		imgRadioRate3.setImageResource(R.drawable.radio_off);
		imgRadioRate4.setImageResource(R.drawable.radio_off);
		imgRadioRate5.setImageResource(R.drawable.radio_off);
	}
}
