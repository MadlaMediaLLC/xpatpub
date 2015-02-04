package com.han.xpatpub.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.han.xpatpub.R;
import com.han.xpatpub.asynctasks.UserAsyncTask;
import com.han.xpatpub.dialog.PatronDialog;
import com.han.xpatpub.dialog.PubOwnerDialog;
import com.han.xpatpub.model.Action;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.User;
import com.han.xpatpub.utility.CameraUtility;

public class SettingActivity extends Activity {

	private Button btnPubOwner;
	private Button btnPatron;
		
	public static final int REQUEST_CODE_SEARCH_PATRON = 1001;
	public static final int REQUEST_CODE_INBOX = 1002;
	
	private Uri fileUri;
	
	private PubOwnerDialog dlgPubOwner;
	private PatronDialog dlgPatron;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
	    
		initWidget();
		initValue();
		initEvent();
		
//		Pub curPub = new Pub();
//		curPub = GlobalData.getPub(PubInfoActivity.curPubID);
		
		new UserAsyncTask(this).execute(Action.ACTION_USER_PUB);
//		new MyAsyncTask(this).execute(MyAsyncTask.ACTION_USER_MESSAGE);
	}
	
	private void initWidget() {
		btnPubOwner = (Button) findViewById(R.id.pub_owner_button);
		btnPatron = (Button) findViewById(R.id.patron_button);
		
		if (Integer.valueOf(GlobalData.currentUser.userType) == User.USER_TYPE_PATRON) {
//			btnPubOwner.setVisibility(View.GONE);
//			btnPatron.setVis
		}
	}
	
	private void initValue() {
		
	}
	
	private void initEvent() {
		btnPubOwner.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				dlgPubOwner = new PubOwnerDialog(SettingActivity.this);
				dlgPubOwner.show();
			}
        });
		
		btnPatron.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				dlgPatron = new PatronDialog(SettingActivity.this);
				dlgPatron.show();
			}
        });
	}
	
	public void startSearchPatron() {
		startActivityForResult(new Intent(this, SearchPatronActivity.class), REQUEST_CODE_SEARCH_PATRON);
	}
	
	public void startInbox() {
		startActivityForResult(new Intent(this, InboxActivity.class), REQUEST_CODE_INBOX);
	}
	
	public void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
 
        fileUri = CameraUtility.getOutputMediaFileUri(CameraUtility.MEDIA_TYPE_IMAGE);
 
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
 
        // start the image capture Intent
        startActivityForResult(intent, CameraUtility.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_SEARCH_PATRON) {
			if (resultCode == RESULT_OK)
				finish();
		}
		if (requestCode == CameraUtility.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
                
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
                
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
	}
	
	private void previewCapturedImage() {
        try { 
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();
 
            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;
 
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
 
            dlgPubOwner.setCapturedImage(bitmap);
            
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
