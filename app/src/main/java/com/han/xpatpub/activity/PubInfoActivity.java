package com.han.xpatpub.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.han.xpatpub.R;
import com.han.xpatpub.adapters.Item;
import com.han.xpatpub.adapters.PubInfoFeatureListAdapter;
import com.han.xpatpub.asynctasks.PubAsyncTask;
import com.han.xpatpub.menu.AlesMenuForReview;
import com.han.xpatpub.menu.FoodMenu;
import com.han.xpatpub.menu.TunesMenuForReview;
import com.han.xpatpub.menu.VibeMenu;
import com.han.xpatpub.model.Action;
import com.han.xpatpub.model.General;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.Pub;

public class PubInfoActivity extends Activity implements OnClickListener {
	
	public static Pub curPub = new Pub();
	public static int curPubID;
	
	private Button btnBack;
	private Button btnSubmit;
	
	private ImageView imgSelVibe;
	private ImageView imgSelTunes;
	private ImageView imgSelGames;
	private ImageView imgSelDarts;
	private ImageView imgSelPool;
	private ImageView imgSelCards;
	private ImageView imgSelShuffleBoard;
	private ImageView imgSelAles;
	
	private RelativeLayout rlInfoVibe;
	private RelativeLayout rlInfoTunes;
	private RelativeLayout rlInfoAles;
	private RelativeLayout rlInfoFoods;
	private RelativeLayout rlVoteDown;
	private RelativeLayout rlVoteUp;
	
	private TextView txtPubName;
	private TextView txtSelVibe;
	private TextView txtSelTunes;
	private TextView txtSelAles;
	private TextView txtSelFood;	
	
	private TextView tvSelVibePercentage;
	private TextView tvSelTunesPercentage;
	private TextView tvSelAlesPercentage;
	private TextView tvSelFoodPercentage;
	private TextView tvVoteUpPercentage;
	private TextView tvVoteDownPercentage;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_pub_info);
		
		initWidget();
		initValue();
		initEvent();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		Log.i(PubInfoActivity.class.getName(), "onResume");
		drawPub();
		new PubAsyncTask(this, curPub).execute(Action.ACTION_GET_PUB_INFO_FROM_FEATURE_COUNTER);
		getPubInfo();
	}
	
	public void getPubInfo() {
		new PubAsyncTask(this).execute(Action.ACTION_GET_PUB_INFO, Integer.toString(curPub.id));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pub_patron_back_button:
			finish();
			break;
			
		case R.id.submit_button:
			ReviewActivity.curPub = curPub;
			PubInfoActivity.this.startActivity(new Intent(PubInfoActivity.this, ReviewActivity.class));
			break;
			
		case R.id.rlInfoAles:
			showFeatureDialog(Pub.FEATURE_GROUP_ID_ALES);
			break;
			
		case R.id.rlInfoFood:
			showFeatureDialog(Pub.FEATURE_GROUP_ID_FOOD);
			break;
			
		case R.id.rlInfoTunes:
			showFeatureDialog(Pub.FEATURE_GROUP_ID_TUNES);
			break;
			
		case R.id.rlInfoVibe:
			showFeatureDialog(Pub.FEATURE_GROUP_ID_VIBE);
			break;
			
		case R.id.rlVoteDown:
			vote(Pub.VOTE_DOWN);		
			break;
			
		case R.id.rlVoteUp:
			vote(Pub.VOTE_UP);			
			break;
			
		default:
			break;
		}
	}
	
	private void vote(int voteType) {
		SharedPreferences prefs = getSharedPreferences(General.SHARED_PREFS, 0);
		
		if (!prefs.contains(String.valueOf(curPub.id))) {
			new PubAsyncTask(PubInfoActivity.this, curPub).execute(Action.ACTION_VOTE, Integer.toString(voteType));				
			
		} else {
			String lastSessionVote = prefs.getString(String.valueOf(curPub.id), GlobalData.currentUser.session_id);
			if (!lastSessionVote.equals(GlobalData.currentUser.session_id)) {
				new PubAsyncTask(PubInfoActivity.this, curPub).execute(Action.ACTION_VOTE, Integer.toString(voteType));
			}
		}
	}
	
	public void initWidget() {
		btnBack = (Button) findViewById(R.id.pub_patron_back_button);
		btnSubmit = (Button) findViewById(R.id.submit_button);
		
		imgSelVibe = (ImageView) findViewById(R.id.vibe_sel_imageView);
		imgSelTunes = (ImageView) findViewById(R.id.tunes_sel_imageView);		
		imgSelGames = (ImageView) findViewById(R.id.game_sel_imageView);
		imgSelDarts = (ImageView) findViewById(R.id.darts_sel_imageView);
		imgSelPool = (ImageView) findViewById(R.id.pool_sel_imageView);
		imgSelCards = (ImageView) findViewById(R.id.cards_sel_imageView);
		imgSelShuffleBoard = (ImageView) findViewById(R.id.shuffleboard_sel_imageView);		
		imgSelAles = (ImageView) findViewById(R.id.ales_sel_imageView);
		
		rlInfoAles = (RelativeLayout) findViewById(R.id.rlInfoAles);
		rlInfoFoods = (RelativeLayout) findViewById(R.id.rlInfoFood);
		rlInfoTunes = (RelativeLayout) findViewById(R.id.rlInfoTunes);
		rlInfoVibe = (RelativeLayout) findViewById(R.id.rlInfoVibe);
		rlVoteDown = (RelativeLayout) findViewById(R.id.rlVoteDown);
		rlVoteUp = (RelativeLayout) findViewById(R.id.rlVoteUp);
		
		tvSelVibePercentage = (TextView) findViewById(R.id.tvSelVibePercentage);
		tvSelTunesPercentage = (TextView) findViewById(R.id.tvSelTunesPercentage);
		tvSelAlesPercentage = (TextView) findViewById(R.id.tvSelAlesPercentage);
		tvSelFoodPercentage = (TextView) findViewById(R.id.tvSelFoodPercentage);
		tvVoteUpPercentage = (TextView) findViewById(R.id.tvVoteUpPercentage);
		tvVoteDownPercentage = (TextView) findViewById(R.id.tvVoteDownPercentage);
		
		txtPubName = (TextView) findViewById(R.id.pub_info_pub_name_textView);
		
		txtSelVibe = (TextView) findViewById(R.id.vibe_sel_textView);
		txtSelTunes = (TextView) findViewById(R.id.tunes_sel_textView);
		txtSelAles = (TextView) findViewById(R.id.ales_sel_textView);
		txtSelFood = (TextView) findViewById(R.id.food_sel_textView);		
	}
	
	public void initValue() {
		tvSelVibePercentage.setVisibility(View.GONE);
		tvSelTunesPercentage.setVisibility(View.GONE);
		tvSelAlesPercentage.setVisibility(View.GONE);
		tvSelFoodPercentage.setVisibility(View.GONE);
		tvVoteDownPercentage.setVisibility(View.GONE);
		tvVoteUpPercentage.setVisibility(View.GONE);
		
//		drawPub();
	}
	
	public void initEvent() {
		btnBack.setOnClickListener(this);		
		btnSubmit.setOnClickListener(this);		
		rlInfoAles.setOnClickListener(this);
		rlInfoFoods.setOnClickListener(this);
		rlInfoTunes.setOnClickListener(this);
		rlInfoVibe.setOnClickListener(this);
		rlVoteDown.setOnClickListener(this);
		rlVoteUp.setOnClickListener(this);
	}
	
	public void drawPub() {		
		updateCurrentPub();
		txtPubName.setText(curPub.name);
		draw();	
	}
	
	private static void updateCurrentPub() {
		curPub = GlobalData.getPub(curPubID);
	}
	
	private void draw() {
		drawVibe();
		drawTunes();
		drawAction();
		drawAles();
		drawFood();
	}
	
	private void drawVibe() {
		if (curPub.nVibe == VibeMenu.LAIDBACK) {
			imgSelVibe.setImageResource(R.drawable.laidback_sel);
			txtSelVibe.setText("Laidback");
			
		} else if (curPub.nVibe == VibeMenu.JAZZY) {
			imgSelVibe.setImageResource(R.drawable.jazzy_sel);
			txtSelVibe.setText("Jazzy");
			
		} else if (curPub.nVibe == VibeMenu.RAUNCHY) {
			imgSelVibe.setImageResource(R.drawable.raunchy_sel);
			txtSelVibe.setText("Raunchy");
			
		} else if (curPub.nVibe == VibeMenu.MEET_MARKET) {
			imgSelVibe.setImageResource(R.drawable.meat_market_sel);
			txtSelVibe.setText("Meet Market");
			
		} else if (curPub.nVibe == Pub.NONE) {
			imgSelVibe.setImageResource(android.R.color.transparent);
			txtSelVibe.setText("");
		}
	}
	
	private void drawTunes() {
		if (curPub.nTunes == TunesMenuForReview.ROCK) {
			imgSelTunes.setImageResource(R.drawable.live_sel);
			txtSelTunes.setText("Rock");
			
		} else if (curPub.nTunes == TunesMenuForReview.POP) {
			imgSelTunes.setImageResource(R.drawable.canned_sel);
			txtSelTunes.setText("Pop");
			
		} else if (curPub.nTunes == TunesMenuForReview.RNB) {
			imgSelTunes.setImageResource(R.drawable.both_sel);
			txtSelTunes.setText("RNB");
			
		} else if (curPub.nTunes == TunesMenuForReview.COUNTRY) {
			imgSelTunes.setImageResource(R.drawable.both_sel);
			txtSelTunes.setText("Country");
			
		} else if (curPub.nTunes == TunesMenuForReview.MIXED) {
			imgSelTunes.setImageResource(R.drawable.both_sel);
			txtSelTunes.setText("Mixed");
			
		} else if (curPub.nTunes == Pub.NONE) {
			imgSelTunes.setImageResource(android.R.color.transparent);
			txtSelTunes.setText("");
		}
	}

	private void drawAction() {		
		imgSelGames.setVisibility(View.GONE);
		imgSelDarts.setVisibility(View.GONE);
		imgSelPool.setVisibility(View.GONE);
		imgSelCards.setVisibility(View.GONE);
		imgSelShuffleBoard.setVisibility(View.GONE);
		
		if (curPub.isGames) {
			imgSelGames.setVisibility(View.VISIBLE);
		}
		if (curPub.isDarts) {
			imgSelDarts.setVisibility(View.VISIBLE);
		}
		if (curPub.isPool) {
			imgSelPool.setVisibility(View.VISIBLE);
		}
		if (curPub.isCards) {
			imgSelCards.setVisibility(View.VISIBLE);
		}
		if (curPub.isShuffleboard) {
			imgSelShuffleBoard.setVisibility(View.VISIBLE);
		}
	}
	
	private void drawAles() {
		if (curPub.nAles == AlesMenuForReview.GENERIC) {
			imgSelAles.setImageResource(R.drawable.less_four_sel);
			txtSelAles.setText("Generic");
			
		} else if (curPub.nAles == AlesMenuForReview.LOCAL) {
			imgSelAles.setImageResource(R.drawable.four_to_ten_sel);
			txtSelAles.setText("Local Craft");
			
		} else if (curPub.nAles == AlesMenuForReview.IMPORT) {
			imgSelAles.setImageResource(R.drawable.more_ten_sel);
			txtSelAles.setText("Import Craft");
			
		} else if (curPub.nAles == Pub.NONE) {
			imgSelAles.setImageResource(android.R.color.transparent);
			txtSelAles.setText("");
		}
	}
	
	// TODO add icons when done
	private void drawFood() {
		if (curPub.nFood == FoodMenu.VERY_GOOD) {
			txtSelFood.setText("Very Good");
			
		} else if (curPub.nFood == FoodMenu.AVERAGE) {
			txtSelFood.setText("Average");
			
		} else if (curPub.nFood == FoodMenu.NOT_GOOD) {
			txtSelFood.setText("Not Good");
			
		} else if (curPub.nFood == FoodMenu.NONE) {
			txtSelFood.setText("None");
		}
	}
	
	private static String getVotingPercentage(int type) {
		int value = 0;
		
		int up = curPub.recommendYes;
		int down = curPub.recommendNo;
		int sum = up + down;
		
		if (type == Pub.VOTE_UP) {
			value = up;
			
		} else if (type == Pub.VOTE_DOWN) {
			value = down;
		}
		
		return Integer.toString((int) ((float) 100 / sum * value)) + "%";
	}
	
	// TODO change to getMaxPercentageOfRatings AND update CLEAN and UPBEAT
	private static String getMaxReviewPercentage(int featureGroup) {
		int max = 0;
		int sum = 0;
		if (featureGroup == Pub.FEATURE_GROUP_ID_VIBE) {
			curPub.nVibe = VibeMenu.LAIDBACK;
			if (max < curPub.ratingVibeClean) {
				max = curPub.ratingVibeClean;	
//				curPub.nVibe = VibeMenu.CLEAN;
			}
			
			if (max < curPub.ratingVibeJazzy) {
				max = curPub.ratingVibeJazzy;	
				curPub.nVibe = VibeMenu.JAZZY;
			}
			
			if (max < curPub.ratingVibeLaidback) {
				max = curPub.ratingVibeLaidback;
				curPub.nVibe = VibeMenu.LAIDBACK;
			}
			
			if (max < curPub.ratingVibeMeetMarket) {
				max = curPub.ratingVibeMeetMarket;	
				curPub.nVibe = VibeMenu.MEET_MARKET;						
			}
			
			if (max < curPub.ratingVibeRaunchy) {
				max = curPub.ratingVibeRaunchy;	
				curPub.nVibe = VibeMenu.RAUNCHY;
			}
			
			if (max < curPub.ratingVibeUpbeat) {
				max = curPub.ratingVibeUpbeat;	
//				curPub.nVibe = VibeMenu.UPBEAT;
			}
			
			sum = curPub.ratingVibeClean
					+ curPub.ratingVibeJazzy
					+ curPub.ratingVibeLaidback
					+ curPub.ratingVibeMeetMarket
					+ curPub.ratingVibeRaunchy
					+ curPub.ratingVibeUpbeat;
			
		} else if (featureGroup == Pub.FEATURE_GROUP_ID_TUNES) {
			curPub.nTunes = TunesMenuForReview.ROCK;
			if (max < curPub.ratingTunesCountry) {
				max = curPub.ratingTunesCountry;
				curPub.nTunes = TunesMenuForReview.COUNTRY;
			}
			
			if (max < curPub.ratingTunesMixed) {
				max = curPub.ratingTunesMixed;	
				curPub.nTunes = TunesMenuForReview.MIXED;
			}
			
			if (max < curPub.ratingTunesPop) {
				max = curPub.ratingTunesPop;
				curPub.nTunes = TunesMenuForReview.POP;
			}
			
			if (max < curPub.ratingTunesRNB) {
				max = curPub.ratingTunesRNB;	
				curPub.nTunes = TunesMenuForReview.RNB;
			}
			
			if (max < curPub.ratingTunesRock) {
				max = curPub.ratingTunesRock;	
				curPub.nTunes = TunesMenuForReview.ROCK;
			}
			
			sum = curPub.ratingTunesCountry
					+ curPub.ratingTunesMixed
					+ curPub.ratingTunesPop
					+ curPub.ratingTunesRNB
					+ curPub.ratingTunesRock;
					
		} else if (featureGroup == Pub.FEATURE_GROUP_ID_ALES) {
			curPub.nAles = AlesMenuForReview.GENERIC;
			if (max < curPub.ratingAlesGeneric) {
				max = curPub.ratingAlesGeneric;	
				curPub.nAles = AlesMenuForReview.GENERIC;
			}
			
			if (max < curPub.ratingAlesImportCraft) {
				max = curPub.ratingAlesImportCraft;		
				curPub.nAles = AlesMenuForReview.IMPORT;
			}
			
			if (max < curPub.ratingAlesLocalCraft) {
				max = curPub.ratingAlesLocalCraft;	
				curPub.nAles = AlesMenuForReview.LOCAL;
			}
			
			sum = curPub.ratingAlesGeneric
					+ curPub.ratingAlesImportCraft
					+ curPub.ratingAlesLocalCraft;
			
		} else if (featureGroup == Pub.FEATURE_GROUP_ID_FOOD) {
			curPub.nFood = FoodMenu.NONE;
			if (max < curPub.ratingFoodAverage) {
				max = curPub.ratingFoodAverage;	
				curPub.nFood = FoodMenu.AVERAGE;
			}
			
			if (max < curPub.ratingFoodNone) {
				max = curPub.ratingFoodNone;	
				curPub.nFood = FoodMenu.NONE;
			}
			
			if (max < curPub.ratingFoodNotGood) {
				max = curPub.ratingFoodNotGood;
				curPub.nFood = FoodMenu.NOT_GOOD;
			}
			
			if (max < curPub.ratingFoodVeryGood) {
				max = curPub.ratingFoodVeryGood;
				curPub.nFood = FoodMenu.VERY_GOOD;
			}		
			
			sum = curPub.ratingFoodAverage
					+ curPub.ratingFoodNone
					+ curPub.ratingFoodNotGood
					+ curPub.ratingFoodVeryGood;
		}
		
		return Integer.toString((int) ((float) 100 / sum * max)) + "%";
	}
	
	public void setPercentages() {
		updateCurrentPub();
		
		tvSelVibePercentage.setText(getMaxReviewPercentage(Pub.FEATURE_GROUP_ID_VIBE));
		tvSelTunesPercentage.setText(getMaxReviewPercentage(Pub.FEATURE_GROUP_ID_TUNES));
		tvSelAlesPercentage.setText(getMaxReviewPercentage(Pub.FEATURE_GROUP_ID_ALES));
		tvSelFoodPercentage.setText(getMaxReviewPercentage(Pub.FEATURE_GROUP_ID_FOOD));
		tvVoteUpPercentage.setText(getVotingPercentage(Pub.VOTE_UP));
		tvVoteDownPercentage.setText(getVotingPercentage(Pub.VOTE_DOWN));
		
		draw();
		
		tvSelVibePercentage.setVisibility(View.VISIBLE);
		tvSelTunesPercentage.setVisibility(View.VISIBLE);
		tvSelAlesPercentage.setVisibility(View.VISIBLE);
		tvSelFoodPercentage.setVisibility(View.VISIBLE);	
		tvVoteUpPercentage.setVisibility(View.VISIBLE);
		tvVoteDownPercentage.setVisibility(View.VISIBLE);
	}

	public void removePercentages() {
		tvSelVibePercentage.setText("");
		tvSelTunesPercentage.setText("");
		tvSelAlesPercentage.setText("");
		tvSelFoodPercentage.setText("");
		
//		tvSelVibePercentage.setVisibility(View.GONE);
//		tvSelTunesPercentage.setVisibility(View.GONE);
//		tvSelAlesPercentage.setVisibility(View.GONE);
//		tvSelFoodPercentage.setVisibility(View.GONE);		
	}
	
	private void showFeatureDialog(int featureGroup) {
		List<Item> featureList = PubInfoFeatureListAdapter.populateList(this, curPub, featureGroup);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(new PubInfoFeatureListAdapter(this, featureList), 
                new DialogInterface.OnClickListener() {
        	
            @Override
            public void onClick(DialogInterface dialog, int item) {
            	dialog.dismiss();
            }
        });
        		
        AlertDialog alert = builder.create();
        alert.show();
	}	
}
