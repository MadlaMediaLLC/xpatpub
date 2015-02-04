package com.han.xpatpub.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.han.xpatpub.R;
import com.han.xpatpub.app.App;
import com.han.xpatpub.interfaces.DialogsListener;
import com.han.xpatpub.interfaces.OnShow00LocationDialogListener;
import com.han.xpatpub.menu.ActionMenu;
import com.han.xpatpub.menu.AlesMenu;
import com.han.xpatpub.menu.TunesMenu;
import com.han.xpatpub.menu.VibeMenu;
import com.han.xpatpub.utility.Dialog00Utility;

public class FeatureActivity extends Activity implements OnShow00LocationDialogListener, DialogsListener {
	
	public static final int MENU_NONE = 0;
	public static final int MENU_VIBE = 1;
	public static final int MENU_TUNES = 2;
	public static final int MENU_ACTION = 3;
	public static final int MENU_ALES = 4;
	
	public static int MENUITEM_NONE = 0;
		
	public int curMenu;

	private RelativeLayout rlytVibeMenu;
	private RelativeLayout rlytTunesMenu;
	private RelativeLayout rlytActionMenu;
	private RelativeLayout rlytAlesMenu;
	
	private ImageView imvVibeMenu;
	private ImageView imvTunesMenu;
	private ImageView imvActionMenu;
	private ImageView imvAlesMenu;
	
	private TextView txtVibeMenu;
	private TextView txtTunesMenu;
	private TextView txtActionMenu;
	private TextView txtAlesMenu;
	
	private ImageView imvVibeMenuSelMark;
	private ImageView imvTunesMenuSelMark;
	private ImageView imvActionMenuSelMark;
	private ImageView imvAlesMenuSelMark;
	
	private VibeMenu viewVibeMenu;
	private TunesMenu viewTunesMenu;
	private ActionMenu viewActionMenu;
	private AlesMenu viewAlesMenu;
	
	private RelativeLayout rlytVibeView;
	private RelativeLayout rlytTunesView;
	private RelativeLayout rlytActionView;
	private RelativeLayout rlytAlesView;
	
	private Button btnSearch;
	private Button btnBack;
	
	private Dialog00Utility mDialogUtility = null;
	
	public static String strFeature = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feature);
		
		initWidget();
		initValue();
		initEvent();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    switch(resultCode) {
	    case SearchActivity.RESULT_CLOSE:
	        finish();
	    }
	    
	    super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onStop() {
		App.stopLocationUtility();		
		super.onStop();
	}
	
	private void initWidget() {
		rlytVibeMenu = (RelativeLayout) findViewById(R.id.vibe_menu_relativeLayout);
		rlytTunesMenu = (RelativeLayout) findViewById(R.id.tunes_menu_relativeLayout);
		rlytActionMenu = (RelativeLayout) findViewById(R.id.action_menu_relativeLayout);
		rlytAlesMenu = (RelativeLayout) findViewById(R.id.ales_menu_relativeLayout);

		imvVibeMenu = (ImageView) findViewById(R.id.vibe_menu_imageView);
		imvTunesMenu = (ImageView) findViewById(R.id.tunes_menu_imageView);
		imvActionMenu = (ImageView) findViewById(R.id.action_menu_imageView);
		imvAlesMenu = (ImageView) findViewById(R.id.ales_menu_imageView);
		
		txtVibeMenu = (TextView) findViewById(R.id.vibe_menu_textView);
		txtTunesMenu = (TextView) findViewById(R.id.tunes_menu_textView);
		txtActionMenu = (TextView) findViewById(R.id.action_menu_textView);
		txtAlesMenu = (TextView) findViewById(R.id.ales_menu_textView);
		
		imvVibeMenuSelMark = (ImageView) findViewById(R.id.vibe_menu_sel_mark_imageView);
		imvTunesMenuSelMark = (ImageView) findViewById(R.id.tunes_menu_sel_mark_imageView);
		imvActionMenuSelMark = (ImageView) findViewById(R.id.action_menu_sel_mark_imageView);
		imvAlesMenuSelMark = (ImageView) findViewById(R.id.ales_menu_sel_mark_imageView);
		
		rlytVibeView = (RelativeLayout) findViewById(R.id.vibe_menubar_relativeLayout);
		rlytTunesView = (RelativeLayout) findViewById(R.id.tunes_menubar_relativeLayout);
		rlytActionView = (RelativeLayout) findViewById(R.id.action_menubar_relativeLayout);
		rlytAlesView = (RelativeLayout) findViewById(R.id.ales_view_relativeLayout);
		
		viewVibeMenu = new VibeMenu(this);
		viewTunesMenu = new TunesMenu(this);
		viewActionMenu = new ActionMenu(this);
		viewAlesMenu = new AlesMenu(this);
		
		rlytVibeView.addView(viewVibeMenu.view);
		rlytTunesView.addView(viewTunesMenu.view);
		rlytActionView.addView(viewActionMenu.view);
		rlytAlesView.addView(viewAlesMenu.view);
		
		btnSearch = (Button) findViewById(R.id.submit_button);
		btnBack = (Button) findViewById(R.id.pub_patron_back_button);
	}
	
	private void initValue() {
//		curMenu = MENU_NONE;
		
//		String str = Double.toString(GlobalData.curLat) + ", " + Double.toString(GlobalData.curLng);
//		DialogUtility.showGeneralAlert(this, "Current Location", str);
		initMenu();
	}
	
	private void initEvent() {
		rlytVibeMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selMenu(MENU_VIBE);
			}
		});

		rlytTunesMenu.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				selMenu(MENU_TUNES);
			}
		});

		rlytActionMenu.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				selMenu(MENU_ACTION);
			}
		});

		rlytAlesMenu.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				selMenu(MENU_ALES);
			}
		});
		
		btnSearch.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("FeatureActivity", "onClick");
				setButtonEnabled(false);
				strFeature = "";
				
				if (viewVibeMenu.nCurVibe != MENUITEM_NONE) {
					strFeature = strFeature + Integer.toString(viewVibeMenu.nCurVibe) + ",";
				}
				
				if (viewTunesMenu.nCurTunes != MENUITEM_NONE) {
					strFeature = strFeature + Integer.toString(viewTunesMenu.nCurTunes) + ",";
				}
				
				if (viewActionMenu.isGames) {
					strFeature = strFeature + Integer.toString(ActionMenu.GAMES_ON_TV) + ",";
				}
				
				if (viewActionMenu.isDarts) {
					strFeature = strFeature + Integer.toString(ActionMenu.DARTS) + ",";
				}
				
				if (viewActionMenu.isPool) {
					strFeature = strFeature + Integer.toString(ActionMenu.POOL) + ",";
				}
				
				if (viewActionMenu.isCards) {
					strFeature = strFeature + Integer.toString(ActionMenu.CARDS) + ",";
				}
				
				if (viewActionMenu.isShuffleboard) {
					strFeature = strFeature + Integer.toString(ActionMenu.SHUFFLEBOARD) + ",";
				}
				
				if (viewAlesMenu.nCurAles != MENUITEM_NONE) {
					strFeature = strFeature + Integer.toString(viewAlesMenu.nCurAles) + ","; 
				}				

				if (!strFeature.equals("")) {
					strFeature = strFeature.substring(0, strFeature.length() - 1);
				}
				
				Log.e("search by strFeature origin", strFeature);
				
				App.setActivity(FeatureActivity.this);
				if(!Dialog00Utility.isShouldShow00Dialog(FeatureActivity.this)) {
					continueWithAppFlow(true);
				}
			}
        });
		
		btnBack.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
//				btnSearch.setFocusableInTouchMode(true);
				finish();
			}
        });
	}
	
	public void setButtonEnabled(boolean value) {
		btnSearch.setEnabled(value);
	}
	
	public void initMenu() {
		curMenu = MENU_NONE;

		rlytVibeMenu.setBackgroundResource(R.drawable.general_button_default_back);
		imvVibeMenu.setImageResource(R.drawable.vibe_nor);
		imvVibeMenuSelMark.setVisibility(View.GONE);
		txtVibeMenu.setTextColor(0xffffffff);

		rlytTunesMenu.setBackgroundResource(R.drawable.general_button_default_back);
		imvTunesMenu.setImageResource(R.drawable.tunes_nor);
		imvTunesMenuSelMark.setVisibility(View.GONE);
		txtTunesMenu.setTextColor(0xffffffff);

		rlytActionMenu.setBackgroundResource(R.drawable.general_button_default_back);
		imvActionMenu.setImageResource(R.drawable.action_nor);
		imvActionMenuSelMark.setVisibility(View.GONE);
		txtActionMenu.setTextColor(0xffffffff);

		rlytAlesMenu.setBackgroundResource(R.drawable.general_button_default_back);
		imvAlesMenu.setImageResource(R.drawable.ales_nor);
		imvAlesMenuSelMark.setVisibility(View.GONE);
		txtAlesMenu.setTextColor(0xffffffff);
		
		rlytVibeView.setVisibility(View.GONE);
		rlytTunesView.setVisibility(View.GONE);
		rlytActionView.setVisibility(View.GONE);
		rlytAlesView.setVisibility(View.GONE);		
	}
	
	private void selMenu(int nMenu) {
		if (nMenu == curMenu) {
			initMenu();
			return;
		}
		
		initMenu();
		curMenu = nMenu;
		
		if (nMenu == MENU_VIBE) {
			rlytVibeMenu.setBackgroundResource(R.drawable.feature_menu_sel_backgroud);
			imvVibeMenu.setImageResource(R.drawable.vibe_sel);
			imvVibeMenuSelMark.setVisibility(View.VISIBLE);
			txtVibeMenu.setTextColor(0xfffcc300);
			rlytVibeView.setVisibility(View.VISIBLE);
		}
		
		if (nMenu == MENU_TUNES) {
			rlytTunesMenu.setBackgroundResource(R.drawable.feature_menu_sel_backgroud);
			imvTunesMenu.setImageResource(R.drawable.tunes_sel);
			imvTunesMenuSelMark.setVisibility(View.VISIBLE);
			txtTunesMenu.setTextColor(0xfffcc300);
			rlytTunesView.setVisibility(View.VISIBLE);
		}
		
		if (nMenu == MENU_ACTION) {
			rlytActionMenu.setBackgroundResource(R.drawable.feature_menu_sel_backgroud);
			imvActionMenu.setImageResource(R.drawable.action_sel);
			imvActionMenuSelMark.setVisibility(View.VISIBLE);
			txtActionMenu.setTextColor(0xfffcc300);
			rlytActionView.setVisibility(View.VISIBLE);
		}
		
		if (nMenu == MENU_ALES) {
			rlytAlesMenu.setBackgroundResource(R.drawable.feature_menu_sel_backgroud);
			imvAlesMenu.setImageResource(R.drawable.ales_sel);
			imvAlesMenuSelMark.setVisibility(View.VISIBLE);
			txtAlesMenu.setTextColor(0xfffcc300);
			rlytAlesView.setVisibility(View.VISIBLE);
		}
	}
	
	public void sucessSearchPub() {
		FeatureActivity.this.startActivityForResult(new Intent(FeatureActivity.this, SearchActivity.class), 0);
	}

	@Override
	public void continueWithAppFlow(boolean shouldContinue) {
		if (shouldContinue) {
			App.mLocationUtility.openLocationInActivityAndSearchForPubs();		
			
		} else {
			setButtonEnabled(true);
		}
	}

	@Override
	public void show00LocationDialog() {
//		mDialogUtility = Dialog00Utility.show00Dialog(this, mDialogUtility);
	}
}
