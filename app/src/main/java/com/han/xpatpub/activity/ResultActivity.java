package com.han.xpatpub.activity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Session;
import com.han.xpatpub.R;
import com.han.xpatpub.app.App;
import com.han.xpatpub.asynctasks.FileAsyncTask;
import com.han.xpatpub.interfaces.OnShow00LocationDialogListener;
import com.han.xpatpub.model.Action;
import com.han.xpatpub.model.Advertise;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.utility.Dialog00Utility;
import com.han.xpatpub.utility.ImageUtility;
import com.han.xpatpub.utility.MessagingUtility;
import com.han.xpatpub.utility.ReleaseUtility;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ResultActivity extends AbstractedActivity implements OnShow00LocationDialogListener {
	
	private static final int DELAY_TIME_HIDE_PUB_TYPE_MENUBAR = 200;
	public static int SEE_BUTTON_INDEX = 1000;

	private Button btnMenu;
	private TextView txtUserName;
	
	public ImageView imgProfile;
	public TextView txtUnreadMessageCount;
	
	private TextView tvCurrentCityHeader;
	private TextView txtCityName;
	
	private LinearLayout llytSrchAllPubsButton;
	private RelativeLayout rlytSrchPubTypeMenu;
	
	private LinearLayout llytSrchPubTypeMenuBar;
	
	private RelativeLayout rlytWesternMenuItem;
	private RelativeLayout rlytSportMenuItem;
	private RelativeLayout rlytIrishMenuItem;
	private RelativeLayout rlytClassicMenuItem;
	private RelativeLayout rlytTaphouseMenuItem;

	private ListView lstAdvertise;
	
	private Dialog00Utility mDialogUtility = null;
	
	private boolean isOpenPubTypeMenu;
	private boolean isCitySet = false;
	
	private AlertDialog dialog = null;
	
	Target target = new Target() {

		@Override
		public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
			GlobalData.bmpProfile = ImageUtility.getRoundCornerBmp(bitmap, 9);
			imgProfile.setImageBitmap(GlobalData.bmpProfile);
		}

		@Override
		public void onBitmapFailed(Drawable arg0) {
		}

		@Override
		public void onPrepareLoad(Drawable arg0) {
		}
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_result);
		
		initWidget();
		initValue();
		initEvent();
	}
	
	@Override
	protected void onStart() {
		super.onStart();		
		if (ReleaseUtility.isRelease(this) && Session.getActiveSession() == null) {
			finish();
			return;
		}
		
		App.updateGlobalData(this, false, this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Dialog00Utility.isShouldShow00Dialog(this);
		App.stopLocationUtility();
	}
	
	public void initWidget() {		
		btnMenu = (Button) findViewById(R.id.menu_button);
		txtUserName = (TextView) findViewById(R.id.myname_textView);
		
		imgProfile = (ImageView) findViewById(R.id.my_profile_imageView);
		txtUnreadMessageCount = (TextView) findViewById(R.id.bars_founded_count_textView);
		
		tvCurrentCityHeader = (TextView) findViewById(R.id.tvCurrentCityHeader);
		txtCityName = (TextView) findViewById(R.id.current_city_textView);
		
		llytSrchAllPubsButton = (LinearLayout) findViewById(R.id.choose_pub_type_linearLayout);
		rlytSrchPubTypeMenu = (RelativeLayout) findViewById(R.id.srch_pub_type_menu_relativeLayout);
		lstAdvertise = (ListView) findViewById(R.id.advertise_listView);
		
		llytSrchPubTypeMenuBar = (LinearLayout) findViewById(R.id.privacy_setting_menubar_linearLayout);

		rlytWesternMenuItem = (RelativeLayout) findViewById(R.id.stealth_menuitem_relativeLayout);
		rlytSportMenuItem = (RelativeLayout) findViewById(R.id.anonymous_menuitem_relativeLayout);
		rlytIrishMenuItem = (RelativeLayout) findViewById(R.id.public_menuitem_relativeLayout);
		rlytClassicMenuItem = (RelativeLayout) findViewById(R.id.classic_menuitem_relativeLayout);
		rlytTaphouseMenuItem = (RelativeLayout) findViewById(R.id.taphouse_menuitem_relativeLayout);
	}
	
	private void initValue() {
		if(GlobalData.currentUser.userEmail != null) {
			txtUserName.setText(GlobalData.currentUser.firstName + " " + GlobalData.currentUser.lastName);
    		App.resultActivitySet = true;
    		
    		if (isCitySetGlobaly()) {
    			isCitySet = false;
    			new FileAsyncTask(this).execute(Action.FILE_GET_CURRENT_CITY);
    		}
    	}	
		
		Picasso.with(ResultActivity.this).load("https://graph.facebook.com/" + GlobalData.currentUser.userToken + "/picture").into(target);
//		Picasso.with(ResultActivity.this).load("https://graph.facebook.com/" + GlobalData.currentUser.userToken + "/picture").into(imgProfile);
		setCurrentCityNameTextView();
		
    	lstAdvertise.setAdapter(new AdvertisesAdapter(this, R.layout.row_advertise_1, GlobalData.arrAdvertise));
    	llytSrchPubTypeMenuBar.setVisibility(View.GONE);
    	isOpenPubTypeMenu = false;    	
	}
	
	private void setCurrentCityNameTextView() {
		String city = GlobalData.curCityName.toUpperCase(Locale.getDefault());
		if (city.equals("") || city == null) {
			tvCurrentCityHeader.setVisibility(View.INVISIBLE);
			
		} else {
			tvCurrentCityHeader.setVisibility(View.VISIBLE);
		}
		
		txtCityName.setText(city);			
	}

	@Override
	public void onReceive() {
		if (!isCitySet && isCitySetGlobaly()) {
			setCurrentCityNameTextView();
			isCitySet = true;
		}
		
		if(!App.resultActivitySet) {
			initValue();			
		}		
	}
	
	/**
	 * 
	 * @return True if {@link GlobalData#curCityName} has value.
	 */
	private boolean isCitySetGlobaly() {
		return !(GlobalData.curCityName == null || GlobalData.curCityName == "");
	}
	
	public void initEvent() {	
		btnMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (GlobalData.currentUser.userType.equals("1")) {
					ResultActivity.this.startActivity(new Intent(ResultActivity.this, PubPatronActivity.class));
//					ResultActivity.this.startActivity(new Intent(ResultActivity.this, PubOwnerActivity.class));
				}
				
				if (GlobalData.currentUser.userType.equals("2")) {
//					ResultActivity.this.startActivity(new Intent(ResultActivity.this, PubPatronActivity.class));
//					new PubAsyncTask(ResultActivity.this).execute(Action.ACTION_GET_PUB_INFO);	 // TODO add pubId
					ResultActivity.this.startActivity(new Intent(ResultActivity.this, PubOwnerActivity.class));
				}				
			}
		});
		
		if (GlobalData.currentUser.userType.equals("1")) {
			OnClickListener listener = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ResultActivity.this.startActivity(new Intent(ResultActivity.this, InboxActivity.class));		
				}
			};
			
			imgProfile.setOnClickListener(listener);
			txtUnreadMessageCount.setOnClickListener(listener);
		}
		
		rlytSrchPubTypeMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isOpenPubTypeMenu = !isOpenPubTypeMenu;
				
				if (isOpenPubTypeMenu) {
					initPubTypeMenuItem();
					llytSrchPubTypeMenuBar.setVisibility(View.VISIBLE);
					
				} else {
					llytSrchPubTypeMenuBar.setVisibility(View.GONE);
				}
			}
        });
		
		rlytWesternMenuItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SearchActivity.strPubType = "1";
				hidePubTypeMenuBar();
			}
        });
		
		rlytSportMenuItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				SearchActivity.strPubType = "2";
				hidePubTypeMenuBar();
			}
        });
		
		rlytIrishMenuItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				SearchActivity.strPubType = "3";
				hidePubTypeMenuBar();
			}
        });
		
		rlytClassicMenuItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SearchActivity.strPubType = "4";
				hidePubTypeMenuBar();
			}
        });
		
		rlytTaphouseMenuItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SearchActivity.strPubType = "5";
				hidePubTypeMenuBar();
			}
        });
		
		llytSrchAllPubsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SearchActivity.strPubType = "";
				startActivity(new Intent(ResultActivity.this, FeatureActivity.class));
			}
        });
	}
	
	private void hidePubTypeMenuBar() {
		Timer timer = new Timer();
	    timer.schedule(new TimerTask() {
	           @Override
	           public void run() {
	        	   ResultActivity.this.runOnUiThread(new Runnable() {
	        		   public void run() {
	    	        	   llytSrchPubTypeMenuBar.setVisibility(View.GONE);
	    	        	   isOpenPubTypeMenu = false;
	        		   }
	        	   });
	           }
	    }, DELAY_TIME_HIDE_PUB_TYPE_MENUBAR);
	    
	    startActivity(new Intent(this, FeatureActivity.class));
	}
	
	public void setUnreadMessageCount() {		
		txtUnreadMessageCount.setText(Integer.toString(GlobalData.getUsabaleCouponsNumber()));
		txtUnreadMessageCount.setVisibility(View.VISIBLE);
		
//		Bitmap bmp = ImageUtility.getBmpFromImageView(imgProfile);
//		bmp = ImageUtility.getRoundCornerBmp(bmp, 9);
//		
//		GlobalData.bmpProfile = bmp;
//		imgProfile.setImageBitmap(bmp);
	}
	
	private void initPubTypeMenuItem() {		
//		rlytWesternMenuItem.setBackgroundColor(0xe1feb902);
//		rlytSportMenuItem.setBackgroundColor(0xe1feb902);
//		rlytIrishMenuItem.setBackgroundColor(0xe1feb902);
//		rlytClassicMenuItem.setBackgroundColor(0xe1feb902);
//		rlytTaphouseMenuItem.setBackgroundColor(0xe1feb902);
	}
	
	public class AdvertisesAdapter extends ArrayAdapter<Advertise> {
    	
    	ArrayList<Advertise> arrData;
    	int row_id;

		public AdvertisesAdapter(Context context, int _row_id, ArrayList<Advertise> _arrData) {
			super(context, _row_id, _arrData);
			row_id = _row_id;
			arrData = _arrData;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        LayoutInflater inflater = ResultActivity.this.getLayoutInflater();
	        row = inflater.inflate(row_id, parent, false);
	        
	        TextView txtAdTitle = (TextView) row.findViewById(R.id.row_name_textView);
	        TextView txtAdType = (TextView) row.findViewById(R.id.row_desc_textView);
	        ImageView imgAdIcon = (ImageView) row.findViewById(R.id.pub_owner_icon_imageView);
	        Button btnSee = (Button) row.findViewById(R.id.change_pub_owner_icon_button);
	        
	        btnSee.setId(SEE_BUTTON_INDEX + position);
	        Advertise advertise = arrData.get(position);
	        
	        if (advertise != null) {
		        txtAdTitle.setText(advertise.title);
		        Picasso.with(ResultActivity.this).load(advertise.adIcon).into(imgAdIcon);
		        txtAdType.setText(advertise.type);
		     
		        btnSee.setOnClickListener(new Button.OnClickListener() {
		        	
					@Override
					public void onClick(View v) {
						int pos = v.getId() - SEE_BUTTON_INDEX;
						Advertise ad = arrData.get(pos);
						Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(ad.link));
						ResultActivity.this.startActivity(intent);
					}
		        });
		        
	        } else {
	        	txtAdTitle.setText("Request to Advertise");
	        	Picasso.with(ResultActivity.this).load("http://xpatpub.com/api/icons/sampleIcon.png").into(imgAdIcon);
		        txtAdType.setText("");
		     
		        btnSee.setOnClickListener(new Button.OnClickListener() {
		        	
					@Override
					public void onClick(View v) {
						if (dialog == null || !dialog.isShowing()) {
					    	dialog = new AlertDialog.Builder(ResultActivity.this)
								.setTitle("Send Request to Advertise")
								.setMessage("We are under Beta testing right now and only are accepting advertisers by request. Please send the request and we will be in contact.")
								.setPositiveButton("Send", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										
										
										MessagingUtility.sendGMail(ResultActivity.this, MessagingUtility.REQUEST_TO_ADVERTISE);
									}
								})
								
								.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						
									@Override
									public void onClick(DialogInterface dialog, int which) {}
									
								}).show();
				    	}	
					}
		        });
	        }
	        
//	        imgAdIcon.setImageUrl("http://www.edumobile.org/iphone/wp-content/uploads/2013/06/Screenshot_41.png");
	        
	        return row;
		}
    }

	@Override
	public void show00LocationDialog() {
//		mDialogUtility = Dialog00Utility.show00Dialog(this, mDialogUtility);
	}	
}
