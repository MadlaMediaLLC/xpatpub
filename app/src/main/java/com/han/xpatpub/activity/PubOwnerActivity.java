package com.han.xpatpub.activity;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.han.xpatpub.R;
import com.han.xpatpub.asynctasks.FileAsyncTask;
import com.han.xpatpub.model.Action;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.utility.CameraUtility;
import com.han.xpatpub.utility.DialogUtility;
import com.squareup.picasso.Picasso;

public class PubOwnerActivity extends Activity {
	
	private Button btnSearchPatrons;
	private Button btnChangePubIcons;
	private Button bLogout;
	
	private TextView txtPubOwnerName;
	private ImageView imgPubIcon;
	private ImageView imgProfile;
	private TextView txtUserName;
	private Button btnBack;
	
	private Uri fileUri;
	public static final int REQUEST_CODE_SEARCH_PATRON = 1001;
	public static final int SELECT_FROM_GALLERY = 0x1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pub_owner);
	    
		initWidget();
		initValue();
		initEvent();
	}
	
	public void initWidget() {
		txtUserName = (TextView) findViewById(R.id.myname_textView);
		imgProfile = (ImageView) findViewById(R.id.my_profile_imageView);
		btnBack = (Button) findViewById(R.id.pub_patron_back_button);
		bLogout = (Button) findViewById(R.id.bLogout);
		
		txtPubOwnerName = (TextView) findViewById(R.id.row_name_textView);
		imgPubIcon = (ImageView) findViewById(R.id.pub_owner_icon_imageView);
		
		btnSearchPatrons = (Button) findViewById(R.id.pub_owner_search_patrons_button);
		btnChangePubIcons = (Button) findViewById(R.id.change_pub_owner_icon_button);
	}
	
	public void initValue() {
//		txtUserName.setText(GlobalData.currentUser.firstName + " " + GlobalData.currentUser.lastName);
		txtUserName.setText(GlobalData.currentUser.userCoupons + " coupons remaining.");
		imgProfile.setImageBitmap(GlobalData.bmpProfile);
		
		txtPubOwnerName.setText(GlobalData.ownPub.name);
		Picasso.with(PubOwnerActivity.this).load(GlobalData.ownPub.iconUrl).into(imgPubIcon);
	}
	
	public void initEvent() {
		btnSearchPatrons.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(PubOwnerActivity.this, SearchPatronActivity.class), REQUEST_CODE_SEARCH_PATRON);
			}
        });
		
		btnChangePubIcons.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				openGallery();
//				captureImage();
			}
        });
		
		btnBack.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
        });
		
		bLogout.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogUtility.showLogoutDialog(PubOwnerActivity.this);
			}
        });
	}
	
	public void captureImage() {
		File f = null;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
 
        fileUri = CameraUtility.getOutputMediaFileUri(CameraUtility.MEDIA_TYPE_IMAGE);
        
        try {
//            f = CameraUtility.getPhotoFile();
//            fileUri = Uri.fromFile(f);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        } catch (Exception e) {
        	e.printStackTrace();
        	f = null;
        }
 
        startActivityForResult(intent, CameraUtility.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
	
	private void openGallery() {
		Intent i = new Intent(Intent.ACTION_PICK);
		i.setType("image/*");
		startActivityForResult(i, SELECT_FROM_GALLERY);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_SEARCH_PATRON) {
			if (resultCode == RESULT_OK) {
				finish();
			}
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
		
		if (resultCode != RESULT_OK) {
			return;
		}
		
		if (requestCode == SELECT_FROM_GALLERY) {
			if (resultCode == RESULT_OK) {
				Uri selectedImage = data.getData();
	            String[] filePathColumn = { MediaStore.Images.Media.DATA };

	            Cursor cursor = getContentResolver().query(
	                               selectedImage, filePathColumn, null, null, null);
	            cursor.moveToFirst();

	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String filePath = cursor.getString(columnIndex);
	            cursor.close();

	            try {
		            Bitmap icon = BitmapFactory.decodeFile(filePath);		
		            imgPubIcon.setImageBitmap(icon);
		            
	            } catch (Throwable e) {
	            	Toast.makeText(this, "Can't show a picture at the moment", Toast.LENGTH_SHORT).show();
	            	
	            	Log.e("PubOwnerActivity", "Couldn't load an image", e);
	            }
	            
	            new FileAsyncTask(this).execute(Action.FILE_UPDATE_PUB_ICON, filePath, Integer.toString(GlobalData.ownPub.id));
	            
//	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	            icon.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//	            byte[] imageBytes = baos.toByteArray();
//	            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//	            new FileAsyncTask(this).execute(Action.FILE_UPDATE_PUB_ICON, encodedImage, Integer.toString(GlobalData.ownPub.id));
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void previewCapturedImage() {
        try {        	
        	BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 8;
 
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
 
            imgPubIcon.setImageBitmap(bitmap);
            Log.e("Image File path", fileUri.getPath());
            
            new FileAsyncTask(this).execute(Action.FILE_UPDATE_PUB_ICON, fileUri.getPath(), Integer.toString(GlobalData.ownPub.id));
            
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        initValue();
    }
}
