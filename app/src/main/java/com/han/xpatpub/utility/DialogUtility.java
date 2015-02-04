package com.han.xpatpub.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;

import com.han.xpatpub.R;
import com.han.xpatpub.activity.LoginActivity;

public class DialogUtility {

	private static final String TAG = "DialogUtility";

	public static void showGeneralAlert(Context context, String title,
			String message) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(message)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// continue with delete
					}
					
				}).show();
	}

	public static void showGeneralYesNoAlert(final Activity activity,
			String title, String message) {
		new AlertDialog.Builder(activity)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								activity.finish();
							}
						})
						
				.setNegativeButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// continue with delete

					}
					
				}).show();
	}
	
	public static void showLogoutDialog(final Activity activity) {
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage(R.string.dialog_logout_message)
		       .setTitle(R.string.dialog_logout_title)
//		       .setCancelable(true)		       
		       .setPositiveButton(R.string.dialog_logout_logout, new DialogInterface.OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						LoginActivity.fb_logout();
						activity.finish();					
					}
				})
				// TODO add negative button so it is on a left side of the dialog
//				.setNegativeButton(R.string.dialog_logout_cancel, new OnClickListener() {
//				
//					@Override
//					public void onClick(DialogInterface dialog, int which) {	
//						
//					}
//				})
				;

		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		showDialog(dialog);
	}

	
		
	public static void showDialog(Dialog dialog) {
		try {
			dialog.show();
			
		} catch (Exception e) {
			Log.e("DialogUtility", "Unsucessful dialog show", e);
			dialog = null;
		}
	}

	public static void dismissDialog(Dialog dialog) {
		try {
			dialog.dismiss();
			
		} catch (Exception e) {
			Log.e(TAG, "Cancel", e);
		}				
	}	
    
//    public static void showWithDialogListener(Activity activity, OnShow00LocationDialogListener dialogListener) {
//    	try {
//	    	dialogListener = (OnShow00LocationDialogListener) activity;
//			if (dialogListener != null) {
//				dialogListener.show00LocationDialog();
//			}
//			
//    	} catch (Exception e) {
//    		Log.e("DialogUtility", "showWithDialogListener error", e);
//    	}
//    }
    
    public static class MyOnKeyListener implements DialogInterface.OnKeyListener {
		
		Dialog dialog = null;
		
		public MyOnKeyListener(Dialog dialog) {
			this.dialog = dialog;
		}

		@Override
		public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
			if (arg1 == KeyEvent.KEYCODE_BACK) {
				DialogUtility.dismissDialog(dialog);
                return true;
                
			} else {
				return false;
			}              
		}
	}

	// private void setMobileDataEnabled(Context context, boolean enabled) {
	// try {
	// final ConnectivityManager conman = (ConnectivityManager)
	// context.getSystemService(Context.CONNECTIVITY_SERVICE);
	// Class conmanClass = Class.forName(conman.getClass().getName());
	// final Field iConnectivityManagerField =
	// conmanClass.getDeclaredField("mService");
	// iConnectivityManagerField.setAccessible(true);
	// final Object iConnectivityManager =
	// iConnectivityManagerField.get(conman);
	// final Class iConnectivityManagerClass =
	// Class.forName(iConnectivityManager.getClass().getName());
	// final Method setMobileDataEnabledMethod = iConnectivityManagerClass
	// .getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
	// setMobileDataEnabledMethod.setAccessible(true);
	//
	// setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
	// } catch (ClassNotFoundException e) {
	// Log.e(TAG, "ClassNotFound", e);
	// } catch (IllegalArgumentException e) {
	// Log.e(TAG, "IllegalArgument", e);
	// } catch (IllegalAccessException e) {
	// Log.e(TAG, "IllegalAccess", e);
	// } catch (InvocationTargetException e) {
	// Log.e(TAG, "InnvocationTarget", e);
	// } catch (SecurityException e) {
	// Log.e(TAG, "Security", e);
	// } catch (NoSuchMethodException e) {
	// Log.e(TAG, "NoSuchMethod", e);
	// } catch (NoSuchFieldException e) {
	// Log.e(TAG, "NoSuchField", e);
	// }
	//
	// }
	
//	private static void showDialog() {
//	    mStackLevel++;
//
//	    // DialogFragment.show() will take care of adding the fragment
//	    // in a transaction.  We also want to remove any currently showing
//	    // dialog, so make our own transaction and take care of that here.
//	    FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
//	    Fragment prev = activity.getFragmentManager().findFragmentByTag("dialog");
//	    if (prev != null) {
//	        ft.remove(prev);
//	    }
//	    
//	    ft.addToBackStack(null);
//
//	    // Create and show the dialog.
//	    DialogFragment newFragment = Dialog00LocationFragment.newInstance(mStackLevel);
//	    newFragment.show(ft, "dialog");
//	}
//	
//	public static class Dialog00LocationFragment extends DialogFragment {
//	    int mNum;
//
//	    /**
//	     * Create a new instance of MyDialogFragment, providing "num"
//	     * as an argument.
//	     */
//	    static Dialog00LocationFragment newInstance(int num) {
//	    	Dialog00LocationFragment f = new Dialog00LocationFragment();
//
//	        // Supply num input as an argument.
//	        Bundle args = new Bundle();
//	        args.putInt("num", num);
//	        f.setArguments(args);
//
//	        return f;
//	    }	    
//	    
//	    @Override
//	    public void onCreate(Bundle savedInstanceState) {
//	        super.onCreate(savedInstanceState);
//	        
//	    }
//	    
//	    @Override
//	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//	            Bundle savedInstanceState) {
//	        View v = inflater.inflate(R.layout.dialog_0_0_location, container, false);
//	       
//	        Button cancel = (Button) v.findViewById(R.id.bCancel);
//			cancel.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					dismiss();
//					mDisplay00LocationDialog = false;
//					continueWithAppFlow(activity);
//				}
//			});
//
//	        return v;
//	    }
//	}
}
