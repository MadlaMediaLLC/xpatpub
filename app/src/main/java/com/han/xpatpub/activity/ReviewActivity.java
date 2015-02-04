package com.han.xpatpub.activity;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.han.xpatpub.R;
import com.han.xpatpub.addpub.ChoosePubFeature;
import com.han.xpatpub.app.LocationUtility;
import com.han.xpatpub.asynctasks.PubAsyncTask;
import com.han.xpatpub.model.Action;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.Pub;

public class ReviewActivity extends Activity {
	
	private Button btnBack;
	private Button btnSubmit;
	private Button bWriteReview;
	private ChoosePubFeature viewChoosePubFeature;
	private EditText etWriteReview;	
	private RelativeLayout rlytChoosePubFeature;
	
	public static Pub curPub = new Pub();
	
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String TAG = ReviewActivity.class.getName();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review);
		
		initWidget();
		initValue();
		initEvent();
	}
	
	public void initWidget() {
		btnBack = (Button) findViewById(R.id.pub_patron_back_button);
		btnSubmit = (Button) findViewById(R.id.submit_button);
		bWriteReview = (Button) findViewById(R.id.bWriteReview);
		
		etWriteReview = (EditText) findViewById(R.id.etWriteReview);
		
		rlytChoosePubFeature = (RelativeLayout) findViewById(R.id.choose_pub_feature_relativeLayout);
		viewChoosePubFeature = new ChoosePubFeature(this, ChoosePubFeature.PURPOSE_INFO_AND_RATE);
		rlytChoosePubFeature.addView(viewChoosePubFeature.view);
	}
	
	public void initValue() {

	}
	
	public void initEvent() {
		btnBack.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnSubmit.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Add when Facebook critique will be enabled
//				postReviewToFacebook("Xpatpub", etWriteReview.getText().toString(), null, null, null);
				
//				String strFeature = viewChoosePubFeature.getStrFeature();
				String[] featuresKeysToUpdate = viewChoosePubFeature.getFeatureKeysToUpdate();				
				new PubAsyncTask(ReviewActivity.this, curPub, featuresKeysToUpdate).execute(Action.ACTION_UPDATE_FEATURE);			
			}		
		});
		
		bWriteReview.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				bWriteReview.setVisibility(View.GONE);
				etWriteReview.setVisibility(View.VISIBLE);				
			}
		});
	}

	/**
	 * 
	 * @param msgName Header - either Xpatpub, pub name or something else
	 * @param msgCaption Review to post to xpatpub fan page
	 * @param msgDescription Pub Description
	 * @param msgLink Link to Pub page or Pub owner account page
	 * @param msgPicture Link to picture
	 */
	private void postReviewToFacebook(String msgName, String msgCaption, String msgDescription, String msgLink, String msgPicture) {
		Session session = Session.getActiveSession();
	    if (session != null) {

		    // Check for publish permissions    
		    List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
//				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}

		    Bundle postParams = new Bundle();		    
		    postParams.putString("name", msgName);
		    postParams.putString("caption", msgCaption);
		    postParams.putString("description", msgDescription);
		    postParams.putString("link", msgLink);
		    postParams.putString("picture", msgPicture);

			Request.Callback callback = new Request.Callback() {
				public void onCompleted(Response response) {
					if (response != null) {
						GraphObject object = response.getGraphObject();
						if (object != null) {
							JSONObject graphResponse = object
									.getInnerJSONObject();
							String postId = null;
							try {
								postId = graphResponse.getString("id");

							} catch (JSONException e) {
								Log.i(TAG, "JSON error " + e.getMessage());
							}

							FacebookRequestError error = response.getError();

							if (error != null) {
								Toast.makeText(
										ReviewActivity.this
												.getApplicationContext(),
										error.getErrorMessage(),
										Toast.LENGTH_SHORT).show();

							} else {
								Toast.makeText(
										ReviewActivity.this
												.getApplicationContext(),
										postId, Toast.LENGTH_LONG).show();
							}
						}
					}
				}
			};

		    Request request = new Request(session, "xpatpub/feed", postParams, 
		                          HttpMethod.POST, callback);

		    RequestAsyncTask task = new RequestAsyncTask(request);
		    task.execute();
		}
	}
	
	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	            return false;
	        }
	    }
	    
	    return true;
	}
	
	public void setRate(int nRate) {
		
	}
	
	public void successSearch() {
		this.finish();
	}
	
	public void successUpdateFeature() {
		String strLat = Double.toString(LocationUtility.getCurLat(this));
		String strLng = Double.toString(LocationUtility.getCurLng(this));
		
		new PubAsyncTask(ReviewActivity.this).execute(Action.ACTION_SEARCH_PUB, strLat, strLng, "", FeatureActivity.strFeature,
				Double.toString(GlobalData.MILES_FOR_CAB_IT));
	}
}
