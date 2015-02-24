package com.han.xpatpub.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.braintreepayments.api.dropin.BraintreePaymentActivity;
import com.braintreepayments.api.dropin.Customization;
import com.han.xpatpub.R;
import com.han.xpatpub.asynctasks.GeneralAsyncTask;
import com.han.xpatpub.asynctasks.UserAsyncTask;
import com.han.xpatpub.model.Action;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.Message;
import com.han.xpatpub.model.URL;
import com.han.xpatpub.network.MessageWebService;
import com.han.xpatpub.utility.DialogUtility;
import com.han.xpatpub.utility.MessagingUtility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PubPatronActivity extends AbstractedActivity {
	
	private AlertDialog dialog = null;
	
	private ImageView imgProfile;
	private TextView txtUserName;
	private Button btnBack;

	private Button btnPrivacySettings;
	private Button btnInbox;
	private Button bLogout;
	private Button bUpgrade;
	
	private RelativeLayout rlytStealthMenuItem;
	private RelativeLayout rlytAnonymousMenuItem;
	private RelativeLayout rlytPublicMenuItem;
	
	private TextView txtBarsFoundedCount;
	private TextView txtInboxCount;
	
	private LinearLayout llytPrivacySettingMenuBar;
	
	private boolean isShowPrivacySettingMenuBar;
	
	private static final int REQUEST_CODE = 100;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pub_patron);
	    
		initWidget();
		onReceive();
		initValue();
		initEvent();
        findClientToken();
	}

    private void findClientToken() {
        new GeneralAsyncTask(this).execute(Action.ACTION_GET_CLIENT_TOKEN);
    }

    @Override
	protected void onResume() {
		super.onResume();

         checkForExpiredCoupons();

        txtInboxCount.setText(Integer.toString(GlobalData.getUsabaleCouponsNumber()));
	}

    private void checkForExpiredCoupons()  {
        ArrayList<Message> messageData = GlobalData.getUsableCoupons();
        for (int i = 0; i<messageData.size(); i++){
            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currentDate = new Date();
            Date expDate = null;
            Message message = messageData.get(i);
            try {
                expDate = dateformat.parse(message.msgExpDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(currentDate.after(expDate)){
                returnCouponTo(message.msgSenderID);
            }
        }
    }

    private void returnCouponTo(int SenderID) {
        String msgSenderID = String.valueOf(SenderID);
        new GeneralAsyncTask(this).execute(Action.ACTION_MARK_MESSAGE,msgSenderID,"0");
        new GeneralAsyncTask(this).execute(Action.OWNER_RETURN_COUPON,msgSenderID);
    }

    public void initWidget() {
		imgProfile = (ImageView) findViewById(R.id.my_profile_imageView);
		txtUserName = (TextView) findViewById(R.id.myname_textView);
		btnPrivacySettings = (Button) findViewById(R.id.privacy_settings_button);
		btnBack = (Button) findViewById(R.id.pub_patron_back_button);
		btnInbox = (Button) findViewById(R.id.pub_owner_search_patrons_button);
		bLogout = (Button) findViewById(R.id.bLogout);
		bUpgrade = (Button) findViewById(R.id.submit_button);
		
		llytPrivacySettingMenuBar = (LinearLayout) findViewById(R.id.privacy_setting_menubar_linearLayout);
		
		rlytStealthMenuItem = (RelativeLayout) findViewById(R.id.stealth_menuitem_relativeLayout);
		rlytAnonymousMenuItem = (RelativeLayout) findViewById(R.id.anonymous_menuitem_relativeLayout);
		rlytPublicMenuItem = (RelativeLayout) findViewById(R.id.public_menuitem_relativeLayout);
		
		txtBarsFoundedCount = (TextView) findViewById(R.id.bars_founded_count_textView);
		txtInboxCount = (TextView) findViewById(R.id.inbox_count_textView);
	}
	
	@Override
	public void onReceive() {
		txtUserName.setText(GlobalData.currentUser.firstName + " " + GlobalData.currentUser.lastName);
		txtBarsFoundedCount.setText(Integer.toString(GlobalData.arrMyPub.size()));			
	}
	
	public void initValue() {		
//		imgProfile.setImageUrl("https://graph.facebook.com/" + GlobalData.currentUser.userToken + "/picture");
		
		llytPrivacySettingMenuBar.setVisibility(View.GONE);
		isShowPrivacySettingMenuBar = false;		
		imgProfile.setImageBitmap(GlobalData.bmpProfile);			
	}
	
	public void initEvent() {
		btnPrivacySettings.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isShowPrivacySettingMenuBar = !isShowPrivacySettingMenuBar;
				if (isShowPrivacySettingMenuBar) {
					llytPrivacySettingMenuBar.setVisibility(View.VISIBLE);
					
				} else {
					llytPrivacySettingMenuBar.setVisibility(View.GONE);
				}
			}
        });
		
		btnBack.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
        });
		
		btnInbox.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PubPatronActivity.this.startActivity(new Intent(PubPatronActivity.this, InboxActivity.class));
			}
        });
		
		bLogout.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogUtility.showLogoutDialog(PubPatronActivity.this);
			}
        });
		
		bUpgrade.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {					
				/*if (dialog == null || !dialog.isShowing()) {
			    	dialog = new AlertDialog.Builder(PubPatronActivity.this)
						.setTitle("Send Request to Upgrade Account")
						.setMessage("We are accepting pub owners by request only during our Beta launch. Please click send and we will have someone contact you via email to setup your Pub Owner account!")
						.setPositiveButton("Send", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {								
								MessagingUtility.sendGMail(PubPatronActivity.this, MessagingUtility.REQUEST_ACCOUNT_UPGRADE);
							}
						})
						
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
							@Override
							public void onClick(DialogInterface dialog, int which) {}
							
						}).show();
		    	}*/
                Intent intent = new Intent(PubPatronActivity.this, BraintreePaymentActivity.class);
                intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, GlobalData.currentUser.userClientToken);
//                intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN,"eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiJmMjFjZmEwMTkxZDhjZmE1NDgzNDhlZDg2NzE2NGI1OWUzNmU1ZTIzZWVjMjUxOGQwYmE1Y2JhMDkwNzEzNGI1fGNyZWF0ZWRfYXQ9MjAxNS0wMi0xN1QxMjoxNDo0Mi4yODk1OTk3NjgrMDAwMFx1MDAyNm1lcmNoYW50X2lkPWRjcHNweTJicndkanIzcW5cdTAwMjZwdWJsaWNfa2V5PTl3d3J6cWszdnIzdDRuYzgiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvZGNwc3B5MmJyd2RqcjNxbi9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzL2RjcHNweTJicndkanIzcW4vY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIn0sInRocmVlRFNlY3VyZUVuYWJsZWQiOnRydWUsInRocmVlRFNlY3VyZSI6eyJsb29rdXBVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvZGNwc3B5MmJyd2RqcjNxbi90aHJlZV9kX3NlY3VyZS9sb29rdXAifSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiQWNtZSBXaWRnZXRzLCBMdGQuIChTYW5kYm94KSIsImNsaWVudElkIjpudWxsLCJwcml2YWN5VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3BwIiwidXNlckFncmVlbWVudFVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS90b3MiLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjp0cnVlLCJlbnZpcm9ubWVudCI6Im9mZmxpbmUiLCJ1bnZldHRlZE1lcmNoYW50IjpmYWxzZSwibWVyY2hhbnRBY2NvdW50SWQiOiJzdGNoMm5mZGZ3c3p5dHc1IiwiY3VycmVuY3lJc29Db2RlIjoiVVNEIn0sImNvaW5iYXNlRW5hYmxlZCI6dHJ1ZSwiY29pbmJhc2UiOnsiY2xpZW50SWQiOiJhZTBkMTk4MWI1MzcxNDgzZDZkODA2OTA5M2EwNjY5MjEyMGRiZTM0YWE0NTBlNzY0ZmY2NTI2ZWVmODhiMTlkIiwibWVyY2hhbnRBY2NvdW50IjoiZGV2cytzYW5kYm94LXNhbXBsZS1tZXJjaGFudEBicmFpbnRyZWVwYXltZW50cy5jb20iLCJzY29wZXMiOiJhdXRob3JpemF0aW9uczpicmFpbnRyZWUgdXNlciIsInJlZGlyZWN0VXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20vY29pbmJhc2Uvb2F1dGgvcmVkaXJlY3QtbGFuZGluZy5odG1sIn0sIm1lcmNoYW50SWQiOiJkY3BzcHkyYnJ3ZGpyM3FuIiwidmVubW8iOiJvZmZsaW5lIiwiYXBwbGVQYXkiOnsic3RhdHVzIjoibW9jayIsImNvdW50cnlDb2RlIjoiVVMiLCJjdXJyZW5jeUNvZGUiOiJVU0QiLCJtZXJjaGFudElkZW50aWZpZXIiOiJtZXJjaGFudC5jb20uYnJhaW50cmVlcGF5bWVudHMuZGV2LWRjb3BlbGFuZCIsInN1cHBvcnRlZE5ldHdvcmtzIjpbInZpc2EiLCJtYXN0ZXJjYXJkIiwiYW1leCJdfX0=");
                Customization customization = new Customization.CustomizationBuilder()
                        .primaryDescription("Pub Owner Account")
                        .amount("$200")
                        .submitButtonText("Upgrade")
                        .build();
                intent.putExtra(BraintreePaymentActivity.EXTRA_CUSTOMIZATION, customization);
                startActivityForResult(intent, REQUEST_CODE);
			}
		});
		
		rlytStealthMenuItem.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isShowPrivacySettingMenuBar = false;
				llytPrivacySettingMenuBar.setVisibility(View.GONE);
				new UserAsyncTask(PubPatronActivity.this).execute(Action.ACTION_UPDATE_USER_PRIVACY, "1");
			}
        });
		
		rlytAnonymousMenuItem.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isShowPrivacySettingMenuBar = false;
				llytPrivacySettingMenuBar.setVisibility(View.GONE);
				new UserAsyncTask(PubPatronActivity.this).execute(Action.ACTION_UPDATE_USER_PRIVACY, "2");
			}
        });
		
		rlytPublicMenuItem.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isShowPrivacySettingMenuBar = false;
				llytPrivacySettingMenuBar.setVisibility(View.GONE);
				new UserAsyncTask(PubPatronActivity.this).execute(Action.ACTION_UPDATE_USER_PRIVACY, "3");
			}
        });
	}
/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            Log.d("resultCode",String.valueOf(resultCode));
            if (resultCode == BraintreePaymentActivity.RESULT_OK) {
                String paymentMethodNonce = data.getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);
                new GeneralAsyncTask(this).execute(Action.ACTION_SEND_NONCE,paymentMethodNonce);
            }
        }
    }*/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ERROR", String.valueOf(data.getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE)));
		if (requestCode == REQUEST_CODE) {
			switch (resultCode) {
			case BraintreePaymentActivity.RESULT_OK:
				String paymentMethodNonce = data.getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);
                new GeneralAsyncTask(this).execute(Action.ACTION_SEND_NONCE,paymentMethodNonce);
				break;
			case BraintreePaymentActivity.BRAINTREE_RESULT_DEVELOPER_ERROR:
			case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_ERROR:
                Log.d("ERROR", String.valueOf(data.getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE)));
			case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_UNAVAILABLE:
				// handle errors here, a throwable may be available in

				break;
			default:
				break;
			}
		}
	}

//	
//	public void onBraintreeSubmit(View v) {
//		Customization customization = new CustomizationBuilder()
//				.primaryDescription("Cart")
//				.secondaryDescription("12 months + 2 months free")
//				.amount("$200").submitButtonText("Purchase").build();		
//		
//		Intent intent = new Intent(this, BraintreePaymentActivity.class);
//		intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN,
//				clientToken);
//		intent.putExtra(BraintreePaymentActivity.EXTRA_CUSTOMIZATION, customization);
//		// REQUEST_CODE is arbitrary and is only used within this activity.
//		startActivityForResult(intent, REQUEST_CODE);		
//	}
//	
//	private void setupSanboxBraintree() {
//		AsyncHttpClient client = new AsyncHttpClient();
//		client.get("https://your-server/" + GlobalData.currentUser.session_id,
//				new TextHttpResponseHandler() {
//
//					@Override
//					public void onFailure(int arg0, Header[] arg1,
//							String arg2, Throwable arg3) {}
//
//					@Override
//					public void onSuccess(int arg0, Header[] arg1,
//							String arg2) {
//						// TODO						
//					}
//				});
//	}
//	
//	private void setupProductionBraintree() {}
//	
//	private void generateClientToken() {
//		ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
//				.customerId(aCustomerId);
//		String clientToken = gateway.clientToken().generate(clientTokenRequest);
//	}
//	
//	private static String renderHtml(String pageName) {
//        try {
//            return FileUtils.readFileToString(new File(pageName));
//            
//        } catch (IOException e) {
//            return "Couldn't find " + pageName;
//        }
//    }
//
//    public static void main(String[] args) {
//        get(new Route("/") {
//            @Override
//            public Object handle(Request request, Response response) {
//                response.type("text/html");
//                return renderHtml("views/braintree.html");
//            }
//        });
//
//        post(new Route("/create_transaction") {
//            @Override
//            public Object handle(Request request, Response response) {
//                TransactionRequest transactionRequest = new TransactionRequest()
//                    .amount(new BigDecimal("1000.00"))
//                    .creditCard()
//                        .number(request.queryParams("number"))
//                        .cvv(request.queryParams("cvv"))
//                        .expirationMonth(request.queryParams("month"))
//                        .expirationYear(request.queryParams("year"))
//                        .done()
//                    .options()
//                        .submitForSettlement(true)
//                        .done();
//
//                Result<Transaction> result = gateway.transaction().sale(transactionRequest);
//				response.type("text/html");
//
//				if (result.isSuccess()) {
//					return "<h1>Success! Transaction ID: "
//							+ result.getTarget().getId() + "</h1>";
//
//				} else {
//					return "<h1>Error: " + result.getMessage() + "</h1>";
//				}
//            }
//        });
//    }
}
