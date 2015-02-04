package com.han.xpatpub.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.han.xpatpub.R;
import com.han.xpatpub.app.App;
import com.han.xpatpub.app.LocationUtility;
import com.han.xpatpub.interfaces.AddPubListener;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.Pub;
import com.han.xpatpub.utility.ImageUtility;
import com.han.xpatpub.utility.MyLogUtility;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AbstractedActivity implements AddPubListener {	
	
	private boolean firstOnLocationChange = true;
	private boolean stabilized;
	private boolean addPubPressed;
	private boolean searching = false; // DON'T REMOVE THIS. It breaks the infinite loop.
	private Button btnHome;
	private Button btnAddPub;
	private Button btnRefresh;
	private Handler locationHandler = new Handler() {
		
        public void handleMessage(Message msg) {
        	super.handleMessage(msg);
        	switch (msg.what) {
			case LOCATION_UPDATING:
				stabilized = false;
				break;

			case LOCATION_STABILIZED:
				stabilized = true;
				if (addPubPressed) {
					onAddPubButtonClicked();
				}
				
				break;
				
			default:
				break;
			}
        }
    };   
    
	private ListView lstPubs;	
	private GoogleMap mMap;
    private Thread locationThread;
	
    private static ArrayList<Pub> tempArrSearchPub = new ArrayList<Pub>();
    
	private static final int PUB_INFO_REQUEST_CODE = 1001;
	private static final int ADD_PUB_REQUEST_CODE  = 1002;	
	
	private static final int LOCATION_UPDATE_INTERVAL_THREAD_IN_MILLISECONDS = 6000;
	private static final int LOCATION_UPDATING = 1;
	private static final int LOCATION_STABILIZED = 2;	
	
	public static ArrayList<Pub> arrSearchPub = new ArrayList<Pub>();
    public static int SEE_BUTTON_INDEX = 2000;
	public static String strPubFeature = "";
	public static String strPubType = ""; 
	public static String strSearchType = GlobalData.SEARCH_TYPE_CAB_IT;	
	
	public static final int RESULT_CLOSE = 101;
	
	private AlertDialog dialog = null;
	
//	LocationClient mLocationClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
//		mLocationClient = new LocationClient(this, this, this);
		
		initWidget();	
		initValue();
		initEvent();
	}	
	
    @Override
    protected void onStart() {
        super.onStart();
        App.setAddPubListener(this);
        App.setActivity(this);
        App.mLocationUtility.onCreateLocationService();
    }
    
    @Override
    public void onResume() {
    	super.onResume();      	
    	stabilized = addPubPressed = false;
    	onReceive();
    }

	@Override
	public void onStop() {
		App.stopLocationUtility();
	    super.onStop();
	}
    
	public void initWidget() {		
//		llytPub = (LinearLayout) findViewById(R.id.pub_info_layout);
		
		btnAddPub = (Button) findViewById(R.id.search_add_pub_button);	
		btnHome = (Button) findViewById(R.id.search_home_button);
		btnRefresh = (Button) findViewById(R.id.search_refresh_button);
		lstPubs = (ListView) findViewById(R.id.pubs_listView);        
	}
	
	private void initValue() {
		setUpMapIfNeeded();
	}
	
	@Override
	public void onReceive() {
		drawPubList();
	}
	
	private void searchAction(Location location) {		
		if (location != null) {
			LatLng latLng = new LatLng(location.getLatitude(),
					location.getLongitude());

			float zoom = 17;
			if (strSearchType == GlobalData.SEARCH_TYPE_CAB_IT) {
				zoom = 15;
			}

			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
					latLng, zoom);

			mMap.animateCamera(cameraUpdate);
			searching = false;
			
		} else if (!searching) {
			App.mLocationUtility.openLocationInActivityAndSearchForPubs();
			searching = true;
			searchAction(App.mLocationUtility.getCurrentLocation());
		}
	}
	
	public void initEvent() {		
//		llytPub.setOnClickListener(new Button.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				SearchActivity.this.startActivityForResult(new Intent(SearchActivity.this, PubInfoActivity.class), SEARCH_REQUEST_CODE);
//			}
//        });
		
		btnAddPub.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pressAddPubButton();				
			}
		});

		btnHome.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CLOSE);
				finish();
			}
		});
		
		btnRefresh.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				App.mLocationUtility.openLocationInActivityAndSearchForPubs();		
				searchAction(App.mLocationUtility.getCurrentLocation());	    
				onReceive();
			}
		});
	}
	
	private void pressAddPubButton() {
		addPubPressed = true;
		onAddPubButtonClicked();
	}
	
	private void onAddPubButtonClicked() {
		if (stabilized) {
			LocationUtility.setLatLonFromLocation(this);
			SearchActivity.this.startActivity(new Intent(SearchActivity.this, AddPubActivity.class));
		
		} else {
			Toast.makeText(SearchActivity.this, 
					"Stabilizing your location. Please, don't not move until we redirect you to adding pub screen.", 
					Toast.LENGTH_LONG).show();			
			
			startLocationThread();
		}
	}
	
    @SuppressLint("NewApi")
	private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the MapFragment.
        	if (Build.VERSION.SDK_INT > 11) {
        		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        	}
            
            // Check if we were successful in obtaining the map.
            if (mMap != null) {            	
                setUpMap();
                searchAction(App.mLocationUtility.getCurrentLocation());
            }
        }
    }
    
    private void setUpMap() {
    	mMap.setMyLocationEnabled(true);
    	mMap.getUiSettings().setZoomControlsEnabled(true);
    }      
    
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        	case LocationUtility.CONNECTION_FAILURE_RESOLUTION_REQUEST:
        /*
         * If the result code is Activity.RESULT_OK, try
         * to connect again
         */
	            switch (resultCode) {
	                case Activity.RESULT_OK :
	                /*
	                 * Try the request again
	                 */
	                break;
	            }
	            
	            break;
	            
        	case PUB_INFO_REQUEST_CODE:
	        	if(resultCode == RESULT_OK) {}
	        	
				if (resultCode == RESULT_FIRST_USER) {
					finish();
				}
				
				break;
				
        	case ADD_PUB_REQUEST_CODE:        		
//        		Criteria criteria = new Criteria();
//
//                // Getting the name of the best provider
//                String provider = locationManager.getBestProvider(criteria, true);
//
//                // Getting Current Location
//                Location location = locationManager.getLastKnownLocation(provider);
                searchAction(App.mLocationUtility.getCurrentLocation());
        		break;
        }
	}
		
	private void startLocationThread() {
		if (locationThread != null && !locationThread.isInterrupted()) {
			locationThread.interrupt();
		}

		locationThread = new Thread() {
			public void run() {
				Log.i("SearchActivity", "Thread started");
				locationHandler.sendEmptyMessage(LOCATION_UPDATING);

				try {
					Thread.sleep(LOCATION_UPDATE_INTERVAL_THREAD_IN_MILLISECONDS);
					locationHandler.sendEmptyMessage(LOCATION_STABILIZED);

				} catch (InterruptedException e) {
					MyLogUtility.error(SearchActivity.class, e, -1);
				}
			}
		};

		locationThread.start();
	}
    
    public void drawPubList() {    	
    	if (arrSearchPub != null) {
    		tempArrSearchPub = arrSearchPub;
    	}
    	
    	arrSearchPub = new ArrayList<Pub>(); 
		
		for (int i = 0; i < GlobalData.arrAllPub.size(); i++) {
			Pub pub = GlobalData.arrAllPub.get(i);

			// TODO check if these if statements should be here
			if (strPubFeature.equals(Pub.PUB_HAVE_LIVE_MUSIC)) {
				if (!pub.isHaveLiveMusic)
					continue;
			}

			if (strPubFeature.equals(Pub.PUB_HAVE_TV_SPORTS)) {
				if (!pub.isHaveTVSports)
					continue;
			}

			if (!strPubType.equals("")) {
				if (!strPubType.equals(Integer.toString(pub.type))) {
					continue;
				}
			}

			double radius = GlobalData.getRadius(strSearchType);
			if (pub.dis_min > radius)
				continue;

			arrSearchPub.add(pub);
		}
		
		if (!tempArrSearchPub.equals(arrSearchPub)) {		
			mMap.clear();
	    	lstPubs.setAdapter(new PubsAdapter(this, R.layout.row_advertise_1, arrSearchPub));
	    	    	
	    	for (int i = 0; i < arrSearchPub.size(); i++) {
	    		Pub pub = arrSearchPub.get(i);
	    		
	    		double lat = pub.lat;
	    		double lng = pub.lng;
	    		
	    		Log.i(SearchActivity.class.getName(), 
	    				"Pub postion = " + Double.toString(lat) + ", " + Double.toString(lng));
	    		LatLng pos = new LatLng(lat, lng);
	
	    		MarkerOptions marker = new MarkerOptions().position(pos).title(pub.name);
	    		 
	//    		marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_1 + i));
	    		marker.icon(BitmapDescriptorFactory.fromBitmap(
	    				ImageUtility.drawTextToBitmap(this, R.drawable.pin, String.valueOf(i + 1))));
	    		mMap.addMarker(marker);
	    	}
	    	
	//    	lstPubs.setOnItemClickListener(new OnItemClickListener() {
	//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	//                
	//        		Pub pub = arrSearchPub.get(position);
	//        	    PubInfoActivity.curPubID = pub.id;
	//        	    
	//        	    Log.e("Current Pub id", Integer.toString(pub.id));
	//            	SearchActivity.this.startActivityForResult(new Intent(SearchActivity.this, PubInfoActivity.class), PUB_INFO_REQUEST_CODE);
	//            }
	//        });
		}
		
		if(arrSearchPub.size() == 0) {
			firstPubAdd();
		}
    }
    
    public void firstPubAdd() {
    	if (dialog == null || !dialog.isShowing()) {
	    	dialog = new AlertDialog.Builder(this)
				.setTitle("No Pubs :(")
				.setMessage("There aren't any pubs added in your area. Please accept this mission to find some pubs and add them!")
				.setPositiveButton("Add Now", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						pressAddPubButton();
					}
				})
				
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		
					@Override
					public void onClick(DialogInterface dialog, int which) {}
					
				}).show();
    	}			
    }
    
    public class PubsAdapter extends ArrayAdapter<Pub> {
    	
    	ArrayList<Pub> arrData;
    	int row_id;

		public PubsAdapter(Context context, int _row_id, ArrayList<Pub> _arrData) {
			super(context, _row_id, _arrData);
			// TODO Auto-generated constructor stub
			row_id = _row_id;
			arrData = _arrData;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        LayoutInflater inflater = SearchActivity.this.getLayoutInflater();
	        row = inflater.inflate(row_id, parent, false);
	        
	        TextView txtPubTitle = (TextView) row.findViewById(R.id.row_name_textView);
	        TextView txtPubType = (TextView) row.findViewById(R.id.row_desc_textView);
	        ImageView imgPubIcon = (ImageView) row.findViewById(R.id.pub_owner_icon_imageView);
	        Button btnSee = (Button) row.findViewById(R.id.change_pub_owner_icon_button);
	        
	        btnSee.setId(SEE_BUTTON_INDEX + position);

	        Pub pub = arrData.get(position);
	        
	        txtPubTitle.setText(Integer.toString(position + 1) + ". " + pub.name);
	        txtPubType.setText(Pub.getTypeName(pub.type));
	        Picasso.with(SearchActivity.this).load(pub.iconUrl).into(imgPubIcon);
	        
	        btnSee.setOnClickListener(new Button.OnClickListener() {
	        	
				@Override
				public void onClick(View v) {
					int pos = v.getId() - SEE_BUTTON_INDEX;

					Pub pub = arrSearchPub.get(pos);
	        	    PubInfoActivity.curPubID = pub.id;
	        	    
	        	    Log.i(SearchActivity.class.getName(), "Current Pub id = " + Integer.toString(pub.id));
	            	SearchActivity.this.startActivityForResult(new Intent(SearchActivity.this, PubInfoActivity.class), PUB_INFO_REQUEST_CODE);
				}
	        });
	        
	        return row;
		}
    }

	@Override
	public void firstOnLocationChange() {
		if (firstOnLocationChange) {
		    searchAction(App.mLocationUtility.getCurrentLocation());	    
		    firstOnLocationChange = false;		    
		} 
		
		startLocationThread();		
	}   
}