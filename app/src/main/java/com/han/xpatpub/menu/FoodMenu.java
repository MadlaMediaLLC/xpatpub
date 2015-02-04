package com.han.xpatpub.menu;

import com.han.xpatpub.R;
import com.han.xpatpub.activity.FeatureActivity;
import com.han.xpatpub.addpub.ChoosePubFeature;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class FoodMenu {
	
	public static final int NONE = 20;
	public static final int NOT_GOOD = 21;
	public static final int AVERAGE = 22;
	public static final int VERY_GOOD = 23;
	
	public Activity parent;
	public Object parentViewObject;
	public View view;
	
	private RelativeLayout rlytNone;
	private RelativeLayout rlytNotGood;
	private RelativeLayout rlytAverage;
	private RelativeLayout rlytVeryGood;
	
	public int nCurFood;
	
	public FoodMenu(Context context) {
		parent = (Activity) context;
		
		initWidget();
		initValue();
		initEvent();
	}
	
	public FoodMenu(Context context, Object viewObject) {
		parent = (Activity) context;
		parentViewObject = viewObject;
		
		initWidget();
		initValue();
		initEvent();
	}
	
	private void initWidget() {
		LayoutInflater inflater = parent.getLayoutInflater();
    	view = inflater.inflate(R.layout.menu_food, null);
    	
    	rlytNone = (RelativeLayout) view.findViewById(R.id.less_four_relativeLayout);
    	rlytNotGood = (RelativeLayout) view.findViewById(R.id.four_ten_relativeLayout);
    	rlytAverage = (RelativeLayout) view.findViewById(R.id.more_ten_relativeLayout);
    	rlytVeryGood = (RelativeLayout) view.findViewById(R.id.cards_relativeLayout);
	}
	
	private void initValue() {
    	nCurFood = FeatureActivity.MENUITEM_NONE;
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
		
		rlytNone.setSelected(false);
		rlytNotGood.setSelected(false);
		rlytAverage.setSelected(false);
		rlytVeryGood.setSelected(false);
	}
	
	private void initEvent() {
		rlytNone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
		    	nCurFood = NONE;
				rlytNone.setSelected(true);
			}
        });
		rlytNotGood.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
		    	nCurFood = NOT_GOOD;
				rlytNotGood.setSelected(true);
			}
        });
		rlytAverage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
		    	nCurFood = AVERAGE;
				rlytAverage.setSelected(true);
			}
        });
		rlytVeryGood.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
		    	nCurFood = VERY_GOOD;
				rlytVeryGood.setSelected(true);
			}
        });
	}
}
