package com.han.xpatpub.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.han.xpatpub.R;
import com.han.xpatpub.app.App;
import com.han.xpatpub.app.LocationUtility;
import com.han.xpatpub.asynctasks.FileAsyncTask;
import com.han.xpatpub.asynctasks.GeneralAsyncTask;
import com.han.xpatpub.asynctasks.UserAsyncTask;
import com.han.xpatpub.model.Action;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.utility.MyToastUtility;
import com.han.xpatpub.utility.ReleaseUtility;

public class LoginActivity extends Activity 
//		implements OnShow00LocationDialogListener
		{
	
	private Button btnLogin;
	private Button btnLoginOwner;
	private Button btnLoginPatron;
//	private DialogUtility mDialogUtility = null;
	
	public ProgressDialog dlgLoading;
//	private UiLifecycleHelper uiHelper;	
//	private LoginButton loginButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		// Add code to print out the key hash
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.han.xpatpub", PackageManager.GET_SIGNATURES);
			
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d(LoginActivity.class.getName(), "KeyHash = " +
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
			
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {}
	    
		initWidget();
		initEvent();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);	 
	    Session session = Session.getActiveSession();
	    if (session.onActivityResult(this, requestCode, resultCode, data)) {
	    	if (session == null || session.isClosed()) {
	    		MyToastUtility.showToast(
	    				this, 
	    				MyToastUtility.MESSAGE_REGISTERING_OR_LOGGING_IN_UNSUCCESSFUL, 
	    				true);
	    	}
	    }
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		App.setActivity(this);
		App.mLocationUtility.onCreateLocationService();
//		DialogUtility.showWithDialogListener(this, LocationUtility.dialogListener);
//		App.show00Dialog(this);
		setButtonEnabled(true);
	}
	
	@Override
	public void onStop() {
		App.stopLocationUtility();
	    if (dlgLoading != null) {
	    	dlgLoading.dismiss();
	    }
	    	    
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		App.stopLocationUtility();
		super.onDestroy();
	}
	
	public static void fb_login(final Activity activity) {
		List<String> permissions = new ArrayList<String>();
		permissions.add("email");
		Session.openActiveSession(activity, true, permissions,
			new Session.StatusCallback() {

				// callback when session changes state
				@Override
				public void call(Session session, SessionState state,
						Exception exception) {
					if (session.isOpened()) {
						// make request to the /me API
						Request.newMeRequest(session,
							new Request.GraphUserCallback() {

								// callback after Graph API response
								// with user object
								@Override
								public void onCompleted(GraphUser user,
										Response response) {
									if (user != null) {
										GlobalData.currentUser.firstName = user
												.getFirstName();
										GlobalData.currentUser.lastName = user
												.getLastName();
										GlobalData.currentUser.userName = user
												.getName();
										GlobalData.currentUser.userEmail = user
												.getProperty("email")
												.toString();
										GlobalData.currentUser.userToken = user
												.getId();

										try {
											// URL image_value = new
											// URL("https://graph.facebook.com/"
											// + user.getId() +
											// "/picture");
											// profPic =
											// BitmapFactory.decodeStream(image_value.openConnection().getInputStream());

											Log.i(LoginActivity.class.getName(), "username: " + user.getName());
											Log.i(LoginActivity.class.getName(), "email: " + user.getProperty("email").toString());
											Log.i(LoginActivity.class.getName(), "ID: " + user.getId());
											
										} catch (Exception e) {}

										new UserAsyncTask(activity).execute(
												Action.ACTION_LOGIN, user.getName());
									} else {
										
									}
								}
								
					}).executeAsync();
				}
			}
		});
	}
	  
	public void initWidget() {
		btnLogin = (Button) findViewById(R.id.coupon_button2);		
		btnLoginOwner = (Button) findViewById(R.id.coupon_button3);		
		btnLoginPatron = (Button) findViewById(R.id.coupon_button4);
		
		if (!ReleaseUtility.isRelease(this)) {
			btnLoginOwner.setVisibility(View.VISIBLE);
			btnLoginPatron.setVisibility(View.VISIBLE);
		}
	}
	
	public void initEvent() {
		btnLogin.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
//				LoginActivity.this.startActivity(new Intent(LoginActivity.this, ResultActivity.class));
//				finish();
				Log.d("LoginActivity", "onClick");
				setButtonEnabled(false);
				fb_login(LoginActivity.this);
			}						
        });
		
		if (!ReleaseUtility.isRelease(this)) {
			btnLoginOwner.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					new UserAsyncTask(LoginActivity.this).execute(
							Action.ACTION_LOGIN, "Shannon Allen");
				}						
	        });
			
			btnLoginPatron.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					new UserAsyncTask(LoginActivity.this).execute(
							Action.ACTION_LOGIN, "Shannon Born");
				}						
	        });
		}
		
		App.application.session();
	}	
	
	public void setButtonEnabled(boolean value) {
		btnLogin.setEnabled(value);
	}
	
	public static void successLogin(Context context) {	
		if (context instanceof LoginActivity) {
			context.startActivity(new Intent(context, ResultActivity.class));
			((LoginActivity) context).finish();
		}		
	}
	
	public static void register(Context context) {
		new UserAsyncTask(context).execute(Action.ACTION_REGISTER, GlobalData.currentUser.userName, 
				GlobalData.currentUser.userToken, GlobalData.currentUser.userEmail, "1", 
				"1", GlobalData.currentUser.userFounder);
	}
	
	public static void reLogin(Context context) {
		new UserAsyncTask(context).execute(Action.ACTION_LOGIN, GlobalData.currentUser.userName);
	}

	public static void successSession(Context context) {
		new GeneralAsyncTask(context).execute(Action.ACTION_GET_COUNTRY_CODE_FROM_GEO_DATA, 
			Double.toString(LocationUtility.getCurLat(null)), Double.toString(LocationUtility.getCurLng(null)));
		new FileAsyncTask(context).execute(Action.FILE_ADVERTISE_LIST);
	    new FileAsyncTask(context).execute(Action.FILE_GET_CURRENT_CITY);
	}	
	
	public void showLoginDialog() {
		if (dlgLoading == null) {
			dlgLoading = new ProgressDialog(LoginActivity.this);
			dlgLoading.setMessage("\tLoading...");
			dlgLoading.setCanceledOnTouchOutside(false);
			dlgLoading.setCancelable(false);
		}
		
		try {
			dlgLoading.show();	
			
		} catch (Exception e) {
			Log.e("LoginActivity", "Progress dialog not shown", e);
		}
	}
	
	public void dismissLoginDialog() {
		if (dlgLoading != null && dlgLoading.isShowing()) {
        	dlgLoading.dismiss();
        }
	}
	
	public static void fb_logout() {
		if (Session.getActiveSession() != null) {
		    Session.getActiveSession().closeAndClearTokenInformation();
		}

		Session.setActiveSession(null);
		GlobalData.clearGlobalData();
	}

//	@Override
//	public void show00LocationDialog() {
//		mDialogUtility = DialogUtility.show00Dialog(this, mDialogUtility);		
//	}
}