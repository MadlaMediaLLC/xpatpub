package com.han.xpatpub.utility;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.network.GMailSender;
import com.han.xpatpub.network.Result;

public class MessagingUtility {
	
	public static final int REQUEST_TO_ADVERTISE = 0;
	public static final int REQUEST_ACCOUNT_UPGRADE = 1;

	public static void sendGMail(final Context context, final int request) {
		String email = null;
		Account[] accounts = AccountManager.get(context).getAccounts();
		for (Account account : accounts) {
		    if (account.type.equals("com.google")) {
		    	email = account.name;
		    }
		}
		
		if (email != null) {
			final String gMail = email;
			final GMailSender sender = new GMailSender("expatpub@gmail.com", "bigdogs22!");
			new AsyncTask<Void, Void, Integer>() {		
				
				@Override
				protected Integer doInBackground(Void... params) {
					try {   	
						switch (request) {
						case REQUEST_TO_ADVERTISE:
							sender.sendMail("Ad Request by " + GlobalData.currentUser.userEmail,   
				                    "Please contact me when you are ready to discuss.\n\nThis is my xpatpub login mail: " + 
				                    GlobalData.currentUser.userEmail +
				                    "\nThis is my gMail: " + gMail,   
				                    gMail,   
				                    "info@madlamedia.com"); 
//				            		"gabriel.gircenko@gmail.com");
							break;
						case REQUEST_ACCOUNT_UPGRADE:
							sender.sendMail("Account Upgrade Request by " + GlobalData.currentUser.userEmail,   
				                    "Please contact me when you are ready to discuss.\n\nThis is my xpatpub login mail: " + 
				                    GlobalData.currentUser.userEmail +
				                    "\nThis is my gMail: " + gMail,   
				                    gMail,   
				                    "info@madlamedia.com"); 
//				            		"gabriel.gircenko@gmail.com");
							break;
						}
			             
			        } catch (Exception e) {   
			            Log.e("SendMail", e.getMessage(), e);   
			            return Result.FAIL;
			        } 
					
					return Result.SUCCESS;
				}
				
				protected void onPostExecute(Integer result) {
					if (result == Result.SUCCESS) {
						Toast.makeText(context, "Request sent successfully", Toast.LENGTH_SHORT).show();
					}
				};
				
			}.execute();	
		}
	}
}
