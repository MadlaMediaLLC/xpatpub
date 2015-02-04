package com.han.xpatpub.addpub;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.han.xpatpub.R;
import com.han.xpatpub.menu.ActionMenu;
import com.han.xpatpub.menu.AlesMenu;
import com.han.xpatpub.menu.AlesMenuForReview;
import com.han.xpatpub.menu.FoodMenu;
import com.han.xpatpub.menu.TunesMenu;
import com.han.xpatpub.menu.TunesMenuForReview;
import com.han.xpatpub.menu.VibeMenu;
import com.han.xpatpub.model.Pub;

public class ChoosePubFeature {

	private Activity parent;
	private int nCurMenu;
	private int purpose;
	
	private RelativeLayout rlytVibeMenuButton;
	private RelativeLayout rlytTunesMenuButton;
	private RelativeLayout rlytTunesMenuForReviewButton;
	private RelativeLayout rlytActionMenuButton;
	private RelativeLayout rlytFoodMenuButton;
	private RelativeLayout rlytAlesMenuButton;
	private RelativeLayout rlytAlesMenuForReviewButton;
	
	private RelativeLayout rlytVibeMenuBar;
	private RelativeLayout rlytTunesMenuBar;
	private RelativeLayout rlytTunesMenuBarForReview;
	private RelativeLayout rlytActionMenuBar;
	private RelativeLayout rlytFoodMenuBar;
	private RelativeLayout rlytAlesMenuBar;
	private RelativeLayout rlytAlesMenuBarForReview;

	private VibeMenu viewVibeMenuBar;
	private TunesMenu viewTunesMenuBar;
	private TunesMenuForReview viewTunesMenuBarForReview;
	private ActionMenu viewActionMenuBar;
	private FoodMenu viewFoodMenuBar;
	private AlesMenu viewAlesMenuBar;
	private AlesMenuForReview viewAlesMenuBarForReview;
	
	public View view;
	
	public static int MENUITEM_NONE = 0;
	
	public static final int MENU_NONE = 0;
	public static final int MENU_VIBE = 1;
	public static final int MENU_TUNES = 2;
	public static final int MENU_ACTION = 3;
	public static final int MENU_ALES = 4;
	public static final int MENU_FOOD = 5;
	public static final int MENU_TUNES_FOR_REVIEW = 6;
	public static final int MENU_ALES_FOR_REVIEW = 7;
	
	public static final int PURPOSE_ADD = 0;
	public static final int PURPOSE_INFO_AND_RATE = 1;
	
	public ChoosePubFeature(Context context, int purpose) {
		parent = (Activity) context;
		this.purpose = purpose;
		
		initWidget();
		initValue();
		initEvent();
	}
	
	private void initWidget() {
		LayoutInflater inflater = parent.getLayoutInflater();
    	view = inflater.inflate(R.layout.view_choose_pub_feature, null);
    	
		rlytVibeMenuButton = (RelativeLayout) view.findViewById(R.id.vibe_menu_button_relativeLayout);
		rlytTunesMenuButton = (RelativeLayout) view.findViewById(R.id.tunes_menu_button_relativeLayout);
		rlytTunesMenuForReviewButton = (RelativeLayout) view.findViewById(R.id.tunes_menu_for_review_button_relativeLayout);
		rlytActionMenuButton = (RelativeLayout) view.findViewById(R.id.action_menu_button_relativeLayout);
		rlytFoodMenuButton = (RelativeLayout) view.findViewById(R.id.food_menu_button_relativeLayout);
		rlytAlesMenuButton = (RelativeLayout) view.findViewById(R.id.ales_menu_button_relativeLayout);
		rlytAlesMenuForReviewButton = (RelativeLayout) view.findViewById(R.id.ales_menu_for_review_button_relativeLayout);
		
		rlytVibeMenuBar = (RelativeLayout) view.findViewById(R.id.vibe_menubar_relativeLayout);
		rlytTunesMenuBar = (RelativeLayout) view.findViewById(R.id.tunes_menubar_relativeLayout);
		rlytTunesMenuBarForReview = (RelativeLayout) view.findViewById(R.id.tunes_menubar_for_review_relativeLayout);
		rlytActionMenuBar = (RelativeLayout) view.findViewById(R.id.action_menubar_relativeLayout);
		rlytFoodMenuBar = (RelativeLayout) view.findViewById(R.id.food_menubar_relativeLayout);
		rlytAlesMenuBar = (RelativeLayout) view.findViewById(R.id.ales_menubar_relativeLayout);
		rlytAlesMenuBarForReview = (RelativeLayout) view.findViewById(R.id.ales_menubar_for_review_relativeLayout);
		
		viewVibeMenuBar = new VibeMenu(parent, this);
		viewTunesMenuBar = new TunesMenu(parent, this);
		viewTunesMenuBarForReview = new TunesMenuForReview(parent, this);
		viewActionMenuBar = new ActionMenu(parent, this);
		viewFoodMenuBar = new FoodMenu(parent, this);
		viewAlesMenuBar = new AlesMenu(parent, this);
		viewAlesMenuBarForReview = new AlesMenuForReview(parent, this);
		
		rlytVibeMenuBar.addView(viewVibeMenuBar.view);		
		
		if (purpose == PURPOSE_ADD) {
			rlytActionMenuBar.addView(viewActionMenuBar.view);
			rlytTunesMenuBar.addView(viewTunesMenuBar.view);
			rlytAlesMenuBar.addView(viewAlesMenuBar.view);

			rlytFoodMenuButton.setVisibility(View.GONE);
			rlytTunesMenuForReviewButton.setVisibility(View.GONE);
			rlytAlesMenuForReviewButton.setVisibility(View.GONE);
			
		} else if (purpose == PURPOSE_INFO_AND_RATE) {
			rlytFoodMenuBar.addView(viewFoodMenuBar.view);
			rlytTunesMenuBarForReview.addView(viewTunesMenuBarForReview.view);	
			rlytAlesMenuBarForReview.addView(viewAlesMenuBarForReview.view);

			rlytActionMenuButton.setVisibility(View.GONE);
			rlytTunesMenuButton.setVisibility(View.GONE);
			rlytAlesMenuButton.setVisibility(View.GONE);
		}				
	}
	
	private void initValue() {
		initMenu();
	}
	
	private void initEvent() {
		rlytVibeMenuButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selMenu(MENU_VIBE);
			}
        });
		rlytTunesMenuButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selMenu(MENU_TUNES);
			}
        });
		rlytTunesMenuForReviewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selMenu(MENU_TUNES_FOR_REVIEW);
			}
        });
		rlytActionMenuButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selMenu(MENU_ACTION);
			}
        });
		rlytFoodMenuButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selMenu(MENU_FOOD);
			}
        });
		rlytAlesMenuButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selMenu(MENU_ALES);
			}
        });
		rlytAlesMenuForReviewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selMenu(MENU_ALES_FOR_REVIEW);
			}
        });
	}
	
	public void initMenu() {
		nCurMenu = MENU_NONE;

		rlytVibeMenuBar.setVisibility(View.GONE);
		rlytTunesMenuBar.setVisibility(View.GONE);
		rlytTunesMenuBarForReview.setVisibility(View.GONE);
		rlytActionMenuBar.setVisibility(View.GONE);
		rlytFoodMenuBar.setVisibility(View.GONE);
		rlytAlesMenuBar.setVisibility(View.GONE);
		rlytAlesMenuBarForReview.setVisibility(View.GONE);
		
		rlytVibeMenuButton.setSelected(false);
		rlytTunesMenuButton.setSelected(false);
		rlytTunesMenuForReviewButton.setSelected(false);
		rlytActionMenuButton.setSelected(false);
		rlytFoodMenuButton.setSelected(false);
		rlytAlesMenuButton.setSelected(false);
		rlytAlesMenuForReviewButton.setSelected(false);
	}
	
	private void selMenu(int nMenu) {
		if (nMenu == nCurMenu) {
			initMenu();
			return;
		}
		
		initMenu();
		nCurMenu = nMenu;
		
		if (nMenu == MENU_VIBE) {
			rlytVibeMenuBar.setVisibility(View.VISIBLE);
			rlytVibeMenuButton.setSelected(true);
			
		} else if (nMenu == MENU_TUNES) {
			rlytTunesMenuBar.setVisibility(View.VISIBLE);
			rlytTunesMenuButton.setSelected(true);
			
		} else if (nMenu == MENU_TUNES_FOR_REVIEW) {
			rlytTunesMenuBarForReview.setVisibility(View.VISIBLE);
			rlytTunesMenuForReviewButton.setSelected(true);
			
		} else if (nMenu == MENU_ACTION) {
			rlytActionMenuBar.setVisibility(View.VISIBLE);
			rlytActionMenuButton.setSelected(true);
			
		} else if (nMenu == MENU_FOOD) {
			rlytFoodMenuBar.setVisibility(View.VISIBLE);
			rlytFoodMenuButton.setSelected(true);
			
		} else if (nMenu == MENU_ALES) {
			rlytAlesMenuBar.setVisibility(View.VISIBLE);
			rlytAlesMenuButton.setSelected(true);
			
		} else if (nMenu == MENU_ALES_FOR_REVIEW) {
			rlytAlesMenuBarForReview.setVisibility(View.VISIBLE);
			rlytAlesMenuForReviewButton.setSelected(true);
		}
	}

	public Integer[] getFeatureIdsToUpdate() {
		List<Integer> featureIds = new ArrayList<Integer>();
		
		if (viewVibeMenuBar.nCurVibe != MENUITEM_NONE) {
			featureIds.add(viewVibeMenuBar.nCurVibe);
		}
		
		if (viewTunesMenuBarForReview.nCurTunes != MENUITEM_NONE) {
			featureIds.add(viewTunesMenuBarForReview.nCurTunes);
		}		
		
		if (viewAlesMenuBarForReview.nCurAles != MENUITEM_NONE) {
			featureIds.add(viewAlesMenuBarForReview.nCurAles);
		}
		
		if (viewFoodMenuBar.nCurFood != MENUITEM_NONE) {
			featureIds.add(viewFoodMenuBar.nCurFood);
		}
		
		return featureIds.toArray(new Integer[featureIds.size()]);
	}
	
	public String[] getFeatureKeysToUpdate() {
		List<String> featureKeys = new ArrayList<String>();
		
		if (viewVibeMenuBar.nCurVibe != MENUITEM_NONE) {
			featureKeys.add(Pub.getFeatureKeyById(viewVibeMenuBar.nCurVibe));
		}
		
		if (viewTunesMenuBarForReview.nCurTunes != MENUITEM_NONE) {
			featureKeys.add(Pub.getFeatureKeyById(viewTunesMenuBarForReview.nCurTunes));
		}		
		
		if (viewAlesMenuBarForReview.nCurAles != MENUITEM_NONE) {
			featureKeys.add(Pub.getFeatureKeyById(viewAlesMenuBarForReview.nCurAles));
		}
		
		if (viewFoodMenuBar.nCurFood != MENUITEM_NONE) {
			featureKeys.add(Pub.getFeatureKeyById(viewFoodMenuBar.nCurFood));
		}
		
		return featureKeys.toArray(new String[featureKeys.size()]);
	}
	
	public String getStrFeature() {		
		String strFeature = "";
		
		if (viewVibeMenuBar.nCurVibe != MENUITEM_NONE) {
			strFeature = strFeature + Integer.toString(viewVibeMenuBar.nCurVibe) + ",";
		}
		if (viewTunesMenuBar.nCurTunes != MENUITEM_NONE) {
			strFeature = strFeature + Integer.toString(viewTunesMenuBar.nCurTunes) + ",";
		}
		if (viewActionMenuBar.isGames) {
			strFeature = strFeature + Integer.toString(ActionMenu.GAMES_ON_TV) + ",";
		}
		if (viewActionMenuBar.isDarts) {
			strFeature = strFeature + Integer.toString(ActionMenu.DARTS) + ",";
		}
		if (viewActionMenuBar.isPool) {
			strFeature = strFeature + Integer.toString(ActionMenu.POOL) + ",";
		}
		if (viewActionMenuBar.isCards) {
			strFeature = strFeature + Integer.toString(ActionMenu.CARDS) + ",";
		}
		if (viewActionMenuBar.isShuffleboard) {
			strFeature = strFeature + Integer.toString(ActionMenu.SHUFFLEBOARD) + ",";
		}
		if (viewAlesMenuBar.nCurAles != MENUITEM_NONE) {
			strFeature = strFeature + Integer.toString(viewAlesMenuBar.nCurAles) + ","; 
		}
		
		return strFeature;
	}
}
