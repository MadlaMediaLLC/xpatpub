package com.han.xpatpub.menu;

import com.han.xpatpub.R;
import com.han.xpatpub.activity.FeatureActivity;
import com.han.xpatpub.addpub.ChoosePubFeature;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class AlesMenu {
	
	// TODO revise with Shannon
	public static final int LESS_FOUR = 17;
	public static final int FOUR_TO_TEN = 18;
	public static final int MORE_TEN = 19;
	
	public Activity parent;
	public Object parentViewObject;
	public View view;
	
	private RelativeLayout rlytLessFour;
	private RelativeLayout rlytFourToTen;
	private RelativeLayout rlytMoreTen;
	
	public int nCurAles;

	public AlesMenu(Context context) {
		// TODO Auto-generated constructor stub
		parent = (FeatureActivity) context;
		
		initWidget();
		initValue();
		initEvent();
	}
	
	public AlesMenu(Context context, Object viewObject) {
		// TODO Auto-generated constructor stub
		parent = (Activity) context;
		parentViewObject = viewObject;
		
		initWidget();
		initValue();
		initEvent();
	}
	
	private void initWidget() {
		LayoutInflater inflater = parent.getLayoutInflater();
    	view = inflater.inflate(R.layout.menu_ales, null);
    	
    	rlytLessFour = (RelativeLayout) view.findViewById(R.id.less_four_relativeLayout);
    	rlytFourToTen = (RelativeLayout) view.findViewById(R.id.four_ten_relativeLayout);
    	rlytMoreTen = (RelativeLayout) view.findViewById(R.id.more_ten_relativeLayout);
	}
	
	private void initValue() {
		initMenu();
		nCurAles = FeatureActivity.MENUITEM_NONE;
	}
	
	private void initEvent() {
		rlytLessFour.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
				nCurAles = LESS_FOUR;
				rlytLessFour.setSelected(true);
			}
        });
		rlytFourToTen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
				nCurAles = FOUR_TO_TEN;
				rlytFourToTen.setSelected(true);
			}
        });
		rlytMoreTen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
				nCurAles = MORE_TEN;
				rlytMoreTen.setSelected(true);
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
		
		rlytLessFour.setSelected(false);
		rlytFourToTen.setSelected(false);
		rlytMoreTen.setSelected(false);
	}
}
