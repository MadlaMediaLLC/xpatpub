package com.han.xpatpub.menu;

import com.han.xpatpub.R;
import com.han.xpatpub.activity.FeatureActivity;
import com.han.xpatpub.addpub.ChoosePubFeature;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class TunesMenuForReview {
	
	public static final int ROCK = 10;
	public static final int RNB = 11;
	public static final int POP = 12;
	public static final int COUNTRY = 13;
	public static final int MIXED = 15;
	
	public Activity parent;
	public Object parentViewObject;
	public View view;
	
	private RelativeLayout rlytRock;
	private RelativeLayout rlytRNB;
	private RelativeLayout rlytPop;
	private RelativeLayout rlytCountry;
	private RelativeLayout rlytMixed;
	
	public int nCurTunes;
	
	public TunesMenuForReview(Context context) {
		// TODO Auto-generated constructor stub
		parent = (FeatureActivity) context;
		
		initWidget();
		initValue();
		initEvent();
	}
	
	public TunesMenuForReview(Context context, Object viewObject) {
		parent = (Activity) context;
		parentViewObject = viewObject;
		
		initWidget();
		initValue();
		initEvent();
	}
	
	private void initWidget() {
		LayoutInflater inflater = parent.getLayoutInflater();
    	view = inflater.inflate(R.layout.menu_tunes_for_review, null);
    	
    	rlytRock = (RelativeLayout) view.findViewById(R.id.less_four_relativeLayout);
    	rlytRNB = (RelativeLayout) view.findViewById(R.id.four_ten_relativeLayout);
    	rlytPop = (RelativeLayout) view.findViewById(R.id.more_ten_relativeLayout);
    	rlytCountry = (RelativeLayout) view.findViewById(R.id.country_relativeLayout);
    	rlytMixed = (RelativeLayout) view.findViewById(R.id.mixed_relativeLayout);
	}
	
	private void initValue() {
		initMenu();
		nCurTunes = FeatureActivity.MENUITEM_NONE;
	}
	
	private void initEvent() {
		rlytRock.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
				nCurTunes = ROCK;
				rlytRock.setSelected(true);
			}
        });
		rlytRNB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
				nCurTunes = RNB;
				rlytRNB.setSelected(true);
			}
        });
		rlytPop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
				nCurTunes = POP;
				rlytPop.setSelected(true);
			}
        });
		rlytCountry.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
				nCurTunes = COUNTRY;
				rlytCountry.setSelected(true);
			}
        });
		rlytMixed.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMenu();
				nCurTunes = MIXED;
				rlytMixed.setSelected(true);
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
		
		rlytRock.setSelected(false);
		rlytRNB.setSelected(false);
		rlytPop.setSelected(false);
		rlytCountry.setSelected(false);
		rlytMixed.setSelected(false);
	}
}
