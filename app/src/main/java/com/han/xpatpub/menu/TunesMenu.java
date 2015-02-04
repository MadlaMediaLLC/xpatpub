package com.han.xpatpub.menu;

import com.han.xpatpub.R;
import com.han.xpatpub.activity.FeatureActivity;
import com.han.xpatpub.addpub.ChoosePubFeature;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class TunesMenu {
	
	public static final int LIVE = 7;
	public static final int CANNED = 8;
	public static final int BOTH = 9;
	
	public Activity parent;
	public Object parentViewObject;
	public View view;
	
	private RelativeLayout rlytLive;
	private RelativeLayout rlytCanned;
	private RelativeLayout rlytBoth;
	
	public int nCurTunes;
	
	public TunesMenu(Context context) {
		// TODO Auto-generated constructor stub
		parent = (FeatureActivity) context;
		
		initWidget();
		initValue();
		initEvent();
	}
	
	public TunesMenu(Context context, Object viewObject) {
		parent = (Activity) context;
		parentViewObject = viewObject;
		
		initWidget();
		initValue();
		initEvent();
	}
	
	private void initWidget() {
		LayoutInflater inflater = parent.getLayoutInflater();
    	view = inflater.inflate(R.layout.menu_tunes, null);
    	
    	rlytLive = (RelativeLayout) view.findViewById(R.id.less_four_relativeLayout);
    	rlytCanned = (RelativeLayout) view.findViewById(R.id.four_ten_relativeLayout);
    	rlytBoth = (RelativeLayout) view.findViewById(R.id.more_ten_relativeLayout);
	}
	
	private void initValue() {
		initMenu();
		nCurTunes = FeatureActivity.MENUITEM_NONE;
	}
	
	private void initEvent() {
		rlytLive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
				nCurTunes = LIVE;
				rlytLive.setSelected(true);
			}
        });
		rlytCanned.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
				nCurTunes = CANNED;
				rlytCanned.setSelected(true);
			}
        });
		rlytBoth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
				nCurTunes = BOTH;
				rlytBoth.setSelected(true);
			}
        });
	}
	
	private void initMenu() {		
		if (parent instanceof FeatureActivity) {
			FeatureActivity featureActivity = (FeatureActivity) parent;
			featureActivity.initMenu();
			
		} else if (parentViewObject instanceof ChoosePubFeature) {
			ChoosePubFeature choosePubFeature = (ChoosePubFeature) parentViewObject;
			choosePubFeature.initMenu();
		}
		
		rlytLive.setSelected(false);
		rlytCanned.setSelected(false);
		rlytBoth.setSelected(false);
	}
}
