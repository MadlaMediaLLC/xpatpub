package com.han.xpatpub.dialog;

import com.han.xpatpub.R;
import com.han.xpatpub.activity.SearchActivity;
import com.han.xpatpub.model.GlobalData;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PubTypeDialog extends Dialog implements View.OnClickListener {

	private LinearLayout llytWalkOnly;
	private LinearLayout llytCabIt;
	private LinearLayout llytBack;
	
	private LinearLayout llytWestern;
	private LinearLayout llytTapHouse;
	private LinearLayout llytIrish;
	private LinearLayout llytSports;
	private LinearLayout llytNeighborhood;
	
	private ImageView imgWestern;
	private ImageView imgTapHouse;
	private ImageView imgIrish;
	private ImageView imgSports;
	private ImageView imgNeighborhood;
		
	private Button btnSearch;
	private Context parent;
	private String strSearchType;

	private int curPubType;
	
	public PubTypeDialog(Context context) {
		super(context);
		parent = context;
		
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		curPubType = 0;
		setContentView(R.layout.dialog_pub_type);
		setCanceledOnTouchOutside(false);
		setTitle("Pub Type");
//		
		initWidget();
		initValue();
		initEvent();
	}
	
	public void initWidget() {
		llytWalkOnly = (LinearLayout) findViewById(R.id.back_one_layout);
		llytCabIt = (LinearLayout) findViewById(R.id.back_home_layout);
		llytBack = (LinearLayout) findViewById(R.id.back_layout);
		
		llytWestern = (LinearLayout) findViewById(R.id.western_layout);
		llytTapHouse = (LinearLayout) findViewById(R.id.tap_house_layout);
		llytIrish = (LinearLayout) findViewById(R.id.irish_layout);
		llytSports = (LinearLayout) findViewById(R.id.sports_layout);
		llytNeighborhood = (LinearLayout) findViewById(R.id.neighborhood_layout);
		
		llytWestern.setOnClickListener(this);
		llytTapHouse.setOnClickListener(this);
		llytIrish.setOnClickListener(this);
		llytSports.setOnClickListener(this);
		llytNeighborhood.setOnClickListener(this);
		
		imgWestern = (ImageView) findViewById(R.id.radio_western_image);
		imgTapHouse = (ImageView) findViewById(R.id.radio_tap_house_image);
		imgIrish = (ImageView) findViewById(R.id.radio_irish_image);
		imgSports = (ImageView) findViewById(R.id.radio_sports_image);
		imgNeighborhood = (ImageView) findViewById(R.id.radio_neighborhood_image);
		
		btnSearch = (Button) findViewById(R.id.coupon_button2);
	}
	
	public void initValue() {
		strSearchType = GlobalData.SEARCH_TYPE_NONE;
	}
	
	public void initEvent() {
		llytWalkOnly.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				llytWalkOnly.setBackgroundResource(R.color.tab_color_selected);
				llytCabIt.setBackgroundResource(R.color.tab_color_general);
				llytBack.setBackgroundResource(R.color.tab_color_general);
				
				strSearchType = GlobalData.SEARCH_TYPE_WALK_ONLY;
			}
        });
		llytCabIt.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				llytWalkOnly.setBackgroundResource(R.color.tab_color_general);
				llytCabIt.setBackgroundResource(R.color.tab_color_selected);
				llytBack.setBackgroundResource(R.color.tab_color_general);
				
				strSearchType = GlobalData.SEARCH_TYPE_CAB_IT;
			}
        });
		llytBack.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				llytWalkOnly.setBackgroundResource(R.color.tab_color_general);
				llytCabIt.setBackgroundResource(R.color.tab_color_general);
				llytBack.setBackgroundResource(R.color.tab_color_selected);
				
				PubTypeDialog.this.dismiss();
			}
        });
		
		btnSearch.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				SearchActivity.strPubFeature = "";
				
				if (curPubType > 0) {
					SearchActivity.strPubType = Integer.toString(curPubType);	
				} else {
					SearchActivity.strPubType = "";
				}
				
				SearchActivity.strSearchType = strSearchType;
				PubTypeDialog.this.dismiss();
				parent.startActivity(new Intent(parent, SearchActivity.class));
			}
        });
	}

	@Override
	public void onClick(View v) {
//		this.dismiss();
//		parent.startActivity(new Intent(parent, SearchActivity.class));
		refreshAllRadio();
		
		if (v == llytWestern) {
			imgWestern.setImageResource(R.drawable.radio_on);
			curPubType = 1;
		}
		if (v == llytTapHouse) {
			imgTapHouse.setImageResource(R.drawable.radio_on);
			curPubType = 2;
		}
		if (v == llytIrish) {
			imgIrish.setImageResource(R.drawable.radio_on);
			curPubType = 3;
		}
		if (v == llytSports) {
			imgSports.setImageResource(R.drawable.radio_on);
			curPubType = 4;
		}
		if (v == llytNeighborhood) {
			imgNeighborhood.setImageResource(R.drawable.radio_on);
			curPubType = 5;
		}
	}
	
	public void refreshAllRadio() {
		imgWestern.setImageResource(R.drawable.radio_off);
		imgTapHouse.setImageResource(R.drawable.radio_off);
		imgIrish.setImageResource(R.drawable.radio_off);
		imgSports.setImageResource(R.drawable.radio_off);
		imgNeighborhood.setImageResource(R.drawable.radio_off);
	}
}
