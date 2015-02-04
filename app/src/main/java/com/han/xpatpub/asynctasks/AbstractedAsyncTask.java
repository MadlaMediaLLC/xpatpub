package com.han.xpatpub.asynctasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.han.xpatpub.activity.FeatureActivity;
import com.han.xpatpub.activity.LoginActivity;
import com.han.xpatpub.network.Result;


public abstract class AbstractedAsyncTask extends AsyncTask<String, Integer, Integer> {

	protected Activity activity = null;
	protected Context parent;
	protected ProgressDialog dlgLoading;	
	
	protected String curAction;
	protected int nResult;
	
	// TODO remove calledFromService
	public AbstractedAsyncTask(Context parent) {		
		this.parent = parent;
	}
	
	/**
	 * Construct with this from activities.
	 * 
	 * @param parent
	 * @param calledFromService
	 * @param activity
	 */
	public AbstractedAsyncTask(Activity activity) {	
		this.parent = this.activity = activity;
//		activity;
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		nResult = Result.FAIL;		
		curAction = params[0];
		return null;
	}

	@Override
	protected void onPreExecute() {
		if (activity != null) {
	//		if (isParentFinishing()) {
	//			return;
	//		}		
			
			if (parent instanceof LoginActivity) {
				((LoginActivity) parent).showLoginDialog();	
				
			} else if (parent instanceof FeatureActivity) {
				if (dlgLoading == null) {
					dlgLoading = new ProgressDialog(parent);
					dlgLoading.setMessage("\tLoading...");
					dlgLoading.setCanceledOnTouchOutside(false);
					dlgLoading.setCancelable(false);
				}
				
				dlgLoading.show();	
			}		
		}
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		if(activity != null) {
	//		if (isParentFinishing()) {
	//			return;
	//		}
			
			if (parent instanceof LoginActivity) {
				((LoginActivity) parent).dismissLoginDialog();
		        
			} else if (parent instanceof FeatureActivity) {
				if (dlgLoading != null && dlgLoading.isShowing()) {
		        	dlgLoading.dismiss();
		        }
			}		
		}
	}
	
//	@SuppressLint("NewApi")
//	private boolean isParentFinishing() {
//		boolean result = false;
//		if (Build.VERSION.SDK_INT < 17) {
//			if (parent.isFinishing()) {
//				result = true;
//			}
//		} else {
//			if (parent.isDestroyed()) { 
//				result = true;
//			}
//		}
//		return result;
//	}
}
