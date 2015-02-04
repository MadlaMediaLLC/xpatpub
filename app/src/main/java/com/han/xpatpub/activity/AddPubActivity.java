package com.han.xpatpub.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.han.xpatpub.R;
import com.han.xpatpub.addpub.ChoosePubFeature;
import com.han.xpatpub.addpub.ChoosePubType;
import com.han.xpatpub.app.LocationUtility;
import com.han.xpatpub.asynctasks.PubAsyncTask;
import com.han.xpatpub.model.Action;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.utility.DialogUtility;

public class AddPubActivity extends Activity {
	
//	Button 		btnSubmit;
	
//	ImageView imgSetting;
	public static final int STAGE_ENTER_PUB_NAME = 0;
	public static final int STAGE_CHOOSE_PUB_TYPE = 1;
	public static final int STAGE_CHOOSE_PUB_FEATURE = 2;
	
	private EditText edtPubName;

	private boolean isLiveMusic;
	private boolean isTVSports;
	private boolean isPubGames;
	
	private RelativeLayout rlytEnterPubName;
	private RelativeLayout rlytChoosePubType;
	private RelativeLayout rlytChoosePubFeature;
	
	private ChoosePubType viewChoosePubType;
	private ChoosePubFeature viewChoosePubFeature;
	
	private int nStage;
//	private int nType;
	
	private Button btnNext;
	private Button btnBack;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_pub);
		
		initWidget();
		initValue();
		initEvent();
	}
	
	public void initWidget() {		
		edtPubName = (EditText) findViewById(R.id.coupon_code_editText);
		rlytEnterPubName = (RelativeLayout) findViewById(R.id.enter_pub_name_relativeLayout);
		rlytChoosePubType = (RelativeLayout) findViewById(R.id.choose_pub_type_relativeLayout);
		rlytChoosePubFeature = (RelativeLayout) findViewById(R.id.choose_pub_feature_relativeLayout);

		btnBack = (Button) findViewById(R.id.pub_patron_back_button);
		btnNext = (Button) findViewById(R.id.submit_button);
		
		viewChoosePubType = new ChoosePubType(this);
		viewChoosePubFeature = new ChoosePubFeature(this, 0);
		
		rlytChoosePubType.addView(viewChoosePubType.view);
		rlytChoosePubFeature.addView(viewChoosePubFeature.view);
		
//		btnSubmit = (Button) findViewById(R.id.coupon_button2);
//		imgSetting = (ImageView) findViewById(R.id.setting_image);
	}
	
	public void initValue() {
		isLiveMusic = false;
		isTVSports = false;
		isPubGames = false;
		
		nStage = STAGE_ENTER_PUB_NAME;
		initStage();
	}
	
	public void initStage() {
		rlytEnterPubName.setVisibility(View.GONE);
		rlytChoosePubType.setVisibility(View.GONE);
		rlytChoosePubFeature.setVisibility(View.GONE);

		if (nStage == STAGE_ENTER_PUB_NAME) {
			rlytEnterPubName.setVisibility(View.VISIBLE);
			btnNext.setText("Next");
		}
		if (nStage == STAGE_CHOOSE_PUB_TYPE) {
			String strPubName = edtPubName.getText().toString();
			if (strPubName.equals("") || strPubName == null) {
				DialogUtility.showGeneralAlert(this, "Error!", "Please input pub name!");
				nStage = STAGE_ENTER_PUB_NAME;
				initStage();
				return;
			}
			rlytChoosePubType.setVisibility(View.VISIBLE);
			btnNext.setText("Next");
		}
		if (nStage == STAGE_CHOOSE_PUB_FEATURE) {
			if (viewChoosePubType.nPubType == 0) {
				DialogUtility.showGeneralAlert(this, "Error!", "Please choose pub type!");
				nStage = STAGE_CHOOSE_PUB_TYPE;
				initStage();
				return;
			}
			rlytChoosePubFeature.setVisibility(View.VISIBLE);
			btnNext.setText("Finish");
		}
	}
	
	public void initEvent() {
		
//		btnSubmit.setOnClickListener(new Button.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				String strPubName = "22";//edtPubName.getText().toString();
//				if (strPubName.equals("")) {
//					showGeneralAlert("Error!", "Please input pub name");
//					return;
//				}
//				
//				String strPubType = Integer.toString(nType);
//				
//				String strIsLiveMusic = (isLiveMusic == true) ? "1":"0";
//				String strIsTVSports = (isTVSports == true) ? "1":"0";
//				String strIsPubGames = (isPubGames == true) ? "1":"0";
//				
//				new MyAsyncTask(AddPubActivity.this).execute(MyAsyncTask.ACTION_ADD_PUB, strPubName, strPubType, 
//						strIsLiveMusic, strIsTVSports, strIsPubGames);
//			}
//        });
		btnNext.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				nStage++;
				if (nStage > STAGE_CHOOSE_PUB_FEATURE) {
					onFinishButton(Action.ACTION_ADD_PUB);
				} else {
					initStage();
				}
			}
        });
		edtPubName.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,	KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_DONE) {
		            // do your stuff here
					Log.e("key event", "Done Click");
					nStage++;
					initStage();
		        }
				return false;
			}
		});
		btnBack.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
        });
	}
	
	public void onFinishButton(String action) {
		String strPubName = edtPubName.getText().toString();
		String strPubType = Integer.toString(viewChoosePubType.nPubType);
		String strLat = Double.toString(LocationUtility.getCurLat(this));
		String strLng = Double.toString(LocationUtility.getCurLng(this));
//		String strLat = "39.345634";
//		String strLng = "61.643466";
		String strCountry = GlobalData.countryNumber;
		String strPubCategory = strPubType;
		String strPubIcon = "http://xpatpub.com/api/icons/sampleIcon.png";
		String strPubFounder = GlobalData.currentUser.userID;
		
		String strFeature = viewChoosePubFeature.getStrFeature();
		Log.e(AddPubActivity.class.getName(), "strFeature for add = " + strFeature);
		
		new PubAsyncTask(AddPubActivity.this).execute(action, strPubName, strPubType, strLat, strLng, strCountry,
				strPubCategory,	strPubIcon, strPubFounder, strFeature);
	}
	
	public void successSubmit() {
		String strLat = Double.toString(LocationUtility.getCurLat(this));
		String strLng = Double.toString(LocationUtility.getCurLng(this));
		
		new PubAsyncTask(AddPubActivity.this).execute(Action.ACTION_SEARCH_PUB, strLat, strLng, "", FeatureActivity.strFeature, 
				Double.toString(GlobalData.MILES_FOR_CAB_IT));
	}
	
	public void successSearch() {
		finish();
	}
	
    public void pubExistAleady() {
    	new AlertDialog.Builder(this)
			.setTitle("Pub exists!")
			.setMessage("There is a pub in this position.\nWould you like to add a pub anyway?")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					onFinishButton(Action.ACTION_ADD_PUB_FORCE);
	//				finish();
				}
			})
			
			.setNegativeButton("No", new DialogInterface.OnClickListener() {
	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// continue with delete
	
				}
			})
			
			.show();
	//		DialogUtility.showGeneralAlert(this, "Error!", "There is an pub in this position.");
	//    	DialogUtility.showGeneralYesNoAlert(this, "Error!", "There is an pub in this position.\nWould you like to add pub anyway?");
    }
    
    public void pubCantBeAdded() {
		DialogUtility.showGeneralAlert(this, "Error!", "Pub is not added.");
    }
}
