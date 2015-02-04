package com.han.xpatpub.menu;

import com.han.xpatpub.R;
import com.han.xpatpub.activity.FeatureActivity;
import com.han.xpatpub.addpub.ChoosePubFeature;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class ActionMenu {
	
	public static int GAMES_ON_TV = 15;
	public static int DARTS = 12;
	public static int POOL = 11;
	public static int CARDS = 14;
	public static int SHUFFLEBOARD = 13;
	
	public Activity parent;
	public Object parentViewObject;
	public View view;
	
	private RelativeLayout rlytGames;
	private RelativeLayout rlytDarts;
	private RelativeLayout rlytPool;
	private RelativeLayout rlytCards;
	private RelativeLayout rlytShuffleboard;
	
	public boolean isGames;
	public boolean isDarts;
	public boolean isPool;
	public boolean isCards;
	public boolean isShuffleboard;
	
	public ActionMenu(Context context) {
		// TODO Auto-generated constructor stub
		parent = (Activity) context;
		
		initWidget();
		initValue();
		initEvent();
	}
	
	public ActionMenu(Context context, Object viewObject) {
		// TODO Auto-generated constructor stub
		parent = (Activity) context;
		parentViewObject = viewObject;
		
		initWidget();
		initValue();
		initEvent();
	}
	
	private void initWidget() {
		LayoutInflater inflater = parent.getLayoutInflater();
    	view = inflater.inflate(R.layout.menu_action, null);
    	
    	rlytGames = (RelativeLayout) view.findViewById(R.id.less_four_relativeLayout);
    	rlytDarts = (RelativeLayout) view.findViewById(R.id.four_ten_relativeLayout);
    	rlytPool = (RelativeLayout) view.findViewById(R.id.more_ten_relativeLayout);
    	rlytCards = (RelativeLayout) view.findViewById(R.id.cards_relativeLayout);
    	rlytShuffleboard = (RelativeLayout) view.findViewById(R.id.shuffleboard_relativeLayout);
	}
	
	private void initValue() {
		initMenu();
	}
	
	private void initEvent() {
		rlytGames.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isGames = !isGames;
				rlytGames.setSelected(isGames);
			}
        });
		rlytDarts.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isDarts = !isDarts;
				rlytDarts.setSelected(isDarts);
			}
        });
		rlytPool.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isPool = !isPool;
				rlytPool.setSelected(isPool);
			}
        });
		rlytCards.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isCards = !isCards;
				rlytCards.setSelected(isCards);
			}
        });
		rlytShuffleboard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isShuffleboard = !isShuffleboard;
				rlytShuffleboard.setSelected(isShuffleboard);
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
		
		rlytGames.setSelected(false);
		rlytDarts.setSelected(false);
		rlytPool.setSelected(false);
		rlytCards.setSelected(false);
		rlytShuffleboard.setSelected(false);
		
		isGames = false;
		isDarts = false;
		isPool = false;
		isCards = false;
		isShuffleboard = false;
	}
}
