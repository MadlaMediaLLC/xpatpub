package com.han.xpatpub.dialog;

import com.han.xpatpub.R;
import com.han.xpatpub.activity.SearchActivity;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.Pub;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class SelPubsDialog extends Dialog {
	
	private LinearLayout llytWalkOnly;
	private LinearLayout llytCabIt;
	private LinearLayout llytBack;
	
	private Button btnSearch;
	private Context parent;
	
	private String title;
	private String strSearchType;
	
	public SelPubsDialog(Context context, String _title) {
		super(context);
		parent = context;

		strSearchType = GlobalData.SEARCH_TYPE_NONE;
		setContentView(R.layout.dialog_select_pub);
		setCanceledOnTouchOutside(false);
		setTitle(title);
		
		title = _title;
		
		initWidget();
		initValue();
		initEvent();
	}
	
	public void initWidget() {
		llytWalkOnly = (LinearLayout) findViewById(R.id.back_one_layout);
		llytCabIt = (LinearLayout) findViewById(R.id.back_home_layout);
		llytBack = (LinearLayout) findViewById(R.id.back_layout);
		
		btnSearch = (Button) findViewById(R.id.coupon_button2);
	}
	
	public void initValue() {
		
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
				
				SelPubsDialog.this.dismiss();
			}
        });
		btnSearch.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (title.equals("All Pubs")) {
					SearchActivity.strPubType = "";
					SearchActivity.strPubFeature = "";
				}
				if (title.equals("Live Music")) {
					SearchActivity.strPubType = "";
					SearchActivity.strPubFeature = Pub.PUB_HAVE_LIVE_MUSIC;
				}
				if (title.equals("Sports")) {
					SearchActivity.strPubType = "";
					SearchActivity.strPubFeature = Pub.PUB_HAVE_TV_SPORTS;
				}
				
				SearchActivity.strSearchType = strSearchType;
				SelPubsDialog.this.dismiss();
				
				parent.startActivity(new Intent(parent, SearchActivity.class));
			}
        });
	}
}
