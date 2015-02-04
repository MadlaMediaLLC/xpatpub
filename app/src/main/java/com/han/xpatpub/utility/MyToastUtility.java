package com.han.xpatpub.utility;

import com.han.xpatpub.R;

import android.content.Context;
import android.widget.Toast;

public class MyToastUtility {

	public static final int MESSAGE_ID = 0;
	public static final int MESSAGE_REGISTERING_SUCCESSFUL = 1;
	public static final int MESSAGE_REGISTERING_UNSUCCESSFUL = 2;
	public static final int MESSAGE_LOGGING_IN_SUCCESSFUL = 3;
	public static final int MESSAGE_LOGGING_IN_UNSUCCESSFUL = 4;
	public static final int MESSAGE_REGISTERING_OR_LOGGING_IN_UNSUCCESSFUL = 5;
	
	public static void showToast(Context context, int messageId, boolean showLenghtShort) {
		String text = "";
		int lenght = showLenghtShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
		
		switch (messageId) {
		case MESSAGE_ID:
//			text = context.getResources().getString(R.string.);
			break;

		case MESSAGE_REGISTERING_SUCCESSFUL:
			text = context.getResources().getString(R.string.toast_registering_successful);
			break;
			
		case MESSAGE_REGISTERING_UNSUCCESSFUL:
			text = context.getResources().getString(R.string.toast_registering_unsuccessful);
			break;
			
		case MESSAGE_LOGGING_IN_SUCCESSFUL:
			text = context.getResources().getString(R.string.toast_logging_in_successful);
			break;		
			
		case MESSAGE_LOGGING_IN_UNSUCCESSFUL:
			text = context.getResources().getString(R.string.toast_logging_in_unsuccessful);
			break;
			
		case MESSAGE_REGISTERING_OR_LOGGING_IN_UNSUCCESSFUL:
			text = context.getResources().getString(R.string.toast_registering_or_logging_in_unsuccessful);
			break;
			
		default:
			break;
		}
		
		Toast.makeText(context, text, lenght).show();
	}
}
