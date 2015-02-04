package com.han.xpatpub.utility;

import java.io.File;
import java.io.IOException;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class CameraUtility {
	
	private static final String IMAGE_DIRECTORY_NAME = "xPatpub";
	
	public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    
	public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    
    public static String PHOTO_FILE_NAME;
    
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    

	public static Uri getOutputMediaFileUri(int type) {
	    return Uri.fromFile(getOutputMediaFile(type));
	}
	
	private static File getOutputMediaFile(int type) {
		 
	    // External sdcard location
	    File mediaStorageDir = new File(
	            Environment
	                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
	            IMAGE_DIRECTORY_NAME);
	 
	    // Create the storage directory if it does not exist
	    if (!mediaStorageDir.exists()) {
	        if (!mediaStorageDir.mkdirs()) {
	            Log.e(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
	                    + IMAGE_DIRECTORY_NAME + " directory");
	            return null;
	        }
	    }
	 
	    // Create a media file name
	    String timeStamp = TimeUtility.getCurrentTime(TimeUtility.CAMERA_TIME_STAMP);
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator
	                + "IMG_" + timeStamp + ".jpg");
	    } else if (type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator
	                + "VID_" + timeStamp + ".mp4");
	    } else {
	        return null;
	    }
	 
	    return mediaFile;
	}
	
	public static File getPhotoFile() throws IOException {		 
		  String filePath = Environment.getExternalStorageDirectory().getPath();
		
		  String currentDateAndTime = TimeUtility.getCurrentTime(TimeUtility.CAMERA_TIME_STAMP);		    
		  String fileName = "image_" + currentDateAndTime + JPEG_FILE_SUFFIX;
		  
		  PHOTO_FILE_NAME = fileName;
		    
		  File tempFile = new File(filePath, fileName);
		  
		  return tempFile;
	}
}
