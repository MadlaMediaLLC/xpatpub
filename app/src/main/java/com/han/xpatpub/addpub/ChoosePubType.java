package com.han.xpatpub.addpub;

import com.han.xpatpub.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ChoosePubType {

	public Activity parent;
	public View view;
	
	private LinearLayout llytChoosePubTypeButton;
	private LinearLayout llytPubTypeMenuBar;
	
	private RelativeLayout rlytWesternMenuItem;
	private RelativeLayout rlytSportMenuItem;
	private RelativeLayout rlytIrishMenuItem;
	private RelativeLayout rlytClassicMenuItem;
	private RelativeLayout rlytTaphouseMenuItem;
	
	private boolean isShowMenuBar;
	public int nPubType;

	public ChoosePubType(Context context) {
		// TODO Auto-generated constructor stub
		parent = (Activity) context;
		
		initWidget();
		initValue();
		initEvent();
	}
	
	private void initWidget() {
		LayoutInflater inflater = parent.getLayoutInflater();
    	view = inflater.inflate(R.layout.view_choose_pub_type, null);
    	
    	llytChoosePubTypeButton = (LinearLayout) view.findViewById(R.id.choose_pub_type_linearLayout);
    	llytPubTypeMenuBar = (LinearLayout) view.findViewById(R.id.privacy_setting_menubar_linearLayout);
    	
		rlytWesternMenuItem = (RelativeLayout) view.findViewById(R.id.stealth_menuitem_relativeLayout);
		rlytSportMenuItem = (RelativeLayout) view.findViewById(R.id.anonymous_menuitem_relativeLayout);
		rlytIrishMenuItem = (RelativeLayout) view.findViewById(R.id.public_menuitem_relativeLayout);
		rlytClassicMenuItem = (RelativeLayout) view.findViewById(R.id.classic_menuitem_relativeLayout);
		rlytTaphouseMenuItem = (RelativeLayout) view.findViewById(R.id.taphouse_menuitem_relativeLayout);
	}
	
	private void initValue() {
		nPubType = 0;
		isShowMenuBar = false;
		llytPubTypeMenuBar.setVisibility(View.GONE);
	}
	
	private void initEvent() {
		llytChoosePubTypeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isShowMenuBar = !isShowMenuBar;
				if (isShowMenuBar) {
					llytPubTypeMenuBar.setVisibility(View.VISIBLE);
				} else {
					llytPubTypeMenuBar.setVisibility(View.GONE);
				}
			}
        });
		
		rlytWesternMenuItem.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				setLMenuBar(1);
			}
        });
		
		rlytSportMenuItem.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				setLMenuBar(2);
			}
        });
		
		rlytIrishMenuItem.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				setLMenuBar(3);
			}
        });
		
		rlytClassicMenuItem.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				setLMenuBar(4);
			}
        });
		
		rlytTaphouseMenuItem.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				setLMenuBar(5);
			}
        });
	}
	
	private void setLMenuBar(int nType) {		
		rlytWesternMenuItem.setSelected(false);
		rlytSportMenuItem.setSelected(false);
		rlytIrishMenuItem.setSelected(false);
		rlytClassicMenuItem.setSelected(false);
		rlytTaphouseMenuItem.setSelected(false);
		
		nPubType = nType;
		isShowMenuBar = false;
		llytPubTypeMenuBar.setVisibility(View.GONE);
		
		rlytWesternMenuItem = (RelativeLayout) view.findViewById(R.id.stealth_menuitem_relativeLayout);
		rlytSportMenuItem = (RelativeLayout) view.findViewById(R.id.anonymous_menuitem_relativeLayout);
		rlytIrishMenuItem = (RelativeLayout) view.findViewById(R.id.public_menuitem_relativeLayout);
		rlytClassicMenuItem = (RelativeLayout) view.findViewById(R.id.classic_menuitem_relativeLayout);
		rlytTaphouseMenuItem = (RelativeLayout) view.findViewById(R.id.taphouse_menuitem_relativeLayout);
		
		if (nType == 1) rlytWesternMenuItem.setSelected(true);
		if (nType == 2) rlytSportMenuItem.setSelected(true);
		if (nType == 3) rlytIrishMenuItem.setSelected(true);
		if (nType == 4) rlytClassicMenuItem.setSelected(true);
		if (nType == 5) rlytTaphouseMenuItem.setSelected(true);
	}
}
