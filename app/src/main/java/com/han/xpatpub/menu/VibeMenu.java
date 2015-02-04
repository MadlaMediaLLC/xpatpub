package com.han.xpatpub.menu;

import com.han.xpatpub.R;
import com.han.xpatpub.activity.FeatureActivity;
import com.han.xpatpub.addpub.ChoosePubFeature;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class VibeMenu {
	
	public static final int LAIDBACK = 2;
	public static final int JAZZY = 3;
	public static final int RAUNCHY = 4;
	public static final int MEET_MARKET = 5;
	
	public Activity parent;
	public Object parentViewObject;
	public View view;
	
	private RelativeLayout rlytLaidBack;
	private RelativeLayout rlytJazzy;
	private RelativeLayout rlytRaunchy;
	private RelativeLayout rlytMeatMarket;
	
	public int nCurVibe;
	
	public VibeMenu(Context context) {
		parent = (Activity) context;
		
		initWidget();
		initValue();
		initEvent();
	}
	
	public VibeMenu(Context context, Object viewObject) {
		parent = (Activity) context;
		parentViewObject = viewObject;
		
		initWidget();
		initValue();
		initEvent();
	}
	
	private void initWidget() {
		LayoutInflater inflater = parent.getLayoutInflater();
    	view = inflater.inflate(R.layout.menu_vibe, null);
    	
    	rlytLaidBack = (RelativeLayout) view.findViewById(R.id.less_four_relativeLayout);
    	rlytJazzy = (RelativeLayout) view.findViewById(R.id.four_ten_relativeLayout);
    	rlytRaunchy = (RelativeLayout) view.findViewById(R.id.more_ten_relativeLayout);
    	rlytMeatMarket = (RelativeLayout) view.findViewById(R.id.cards_relativeLayout);
	}
	
	private void initValue() {
    	nCurVibe = FeatureActivity.MENUITEM_NONE;
		initMenu();
	}
	
	private void initMenu() {		
		if (parent instanceof FeatureActivity) {
			FeatureActivity featureActivity = (FeatureActivity) parent;
			featureActivity.initMenu();
		} else if (parentViewObject instanceof ChoosePubFeature) {
			ChoosePubFeature choosePubFeature = (ChoosePubFeature) parentViewObject;
			choosePubFeature.initMenu();
		}
		
		rlytLaidBack.setSelected(false);
		rlytJazzy.setSelected(false);
		rlytRaunchy.setSelected(false);
		rlytMeatMarket.setSelected(false);
	}
	
	private void initEvent() {
		rlytLaidBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
		    	nCurVibe = LAIDBACK;
				rlytLaidBack.setSelected(true);
			}
        });
		rlytJazzy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
		    	nCurVibe = JAZZY;
				rlytJazzy.setSelected(true);
			}
        });
		rlytRaunchy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
		    	nCurVibe = RAUNCHY;
				rlytRaunchy.setSelected(true);
			}
        });
		rlytMeatMarket.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
		    	nCurVibe = MEET_MARKET;
				rlytMeatMarket.setSelected(true);
			}
        });
	}
}
