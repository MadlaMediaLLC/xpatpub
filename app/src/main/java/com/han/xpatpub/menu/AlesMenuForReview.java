package com.han.xpatpub.menu;

import com.han.xpatpub.R;
import com.han.xpatpub.activity.FeatureActivity;
import com.han.xpatpub.addpub.ChoosePubFeature;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class AlesMenuForReview {
		
	public static final int GENERIC = 30;
	public static final int LOCAL = 31;
	public static final int IMPORT = 32;
	
	public Activity parent;
	public Object parentViewObject;
	public View view;
	
	private RelativeLayout rlytGeneric;
	private RelativeLayout rlytLocal;
	private RelativeLayout rlytImport;
	
	public int nCurAles;

	public AlesMenuForReview(Context context) {
		parent = (FeatureActivity) context;
		
		initWidget();
		initValue();
		initEvent();
	}
	
	public AlesMenuForReview(Context context, Object viewObject) {
		// TODO Auto-generated constructor stub
		parent = (Activity) context;
		parentViewObject = viewObject;
		
		initWidget();
		initValue();
		initEvent();
	}
	
	private void initWidget() {
		LayoutInflater inflater = parent.getLayoutInflater();
    	view = inflater.inflate(R.layout.menu_ales_for_review, null);
    	
    	rlytGeneric = (RelativeLayout) view.findViewById(R.id.less_four_relativeLayout);
    	rlytLocal = (RelativeLayout) view.findViewById(R.id.four_ten_relativeLayout);
    	rlytImport = (RelativeLayout) view.findViewById(R.id.more_ten_relativeLayout);
	}
	
	private void initValue() {
		initMenu();
		nCurAles = FeatureActivity.MENUITEM_NONE;
	}
	
	private void initEvent() {
		rlytGeneric.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
				nCurAles = GENERIC;
				rlytGeneric.setSelected(true);
			}
        });
		rlytLocal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
				nCurAles = LOCAL;
				rlytLocal.setSelected(true);
			}
        });
		rlytImport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
				nCurAles = IMPORT;
				rlytImport.setSelected(true);
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
		
		rlytGeneric.setSelected(false);
		rlytLocal.setSelected(false);
		rlytImport.setSelected(false);
	}
}
