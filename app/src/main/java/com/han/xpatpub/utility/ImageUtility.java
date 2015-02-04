package com.han.xpatpub.utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

public class ImageUtility {
	
	public static Bitmap getBitmapFromUrl(String src) {
		try {
	        Log.e("src", src);
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        Log.e("Bitmap", "returned");
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        Log.e("Exception",e.getMessage());
	        return null;
	    }
	}
	
	/**
	 * This caused a crash. 
	 * 
	 * Refer to Github's issue #113 or Crashlytics #3
	 * 
	 * @param imgView
	 * @return
	 */
	private static Bitmap getBmpFromImageView(ImageView imgView) {		
		Bitmap bitmap = null;
		Drawable drawable = imgView.getDrawable();
		
		if (drawable != null) {
			if (drawable instanceof BitmapDrawable) {
			    bitmap = ((BitmapDrawable) drawable).getBitmap();
			    
			} else {
			    bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
			    Canvas canvas = new Canvas(bitmap);
			    drawable.draw(canvas);
			}
		}
		
		return bitmap;
	}
	
//	public static Target loadBitmap(Context context, String url) {
//		Target target = new Target() {
//			@Override
//			public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {}
//
//			@Override
//			public void onBitmapFailed(Drawable arg0) {}
//
//			@Override
//			public void onPrepareLoad(Drawable arg0) {}
//		};
//		
//		Picasso.with(context).load(url).into(target);
//		return target;
//	}
	
	public static Bitmap getRoundCornerBmp(Bitmap src, float round) {
		  // Source image size
		  int width = src.getWidth();
		  int height = src.getHeight();
		  // create result bitmap output
		  Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		  // set canvas for painting
		  Canvas canvas = new Canvas(result);
		  canvas.drawARGB(0, 0, 0, 0);
		 
		  // configure paint
		  final Paint paint = new Paint();
		  paint.setAntiAlias(true);
		  paint.setColor(Color.BLACK);
		 
		  // configure rectangle for embedding
		  final Rect rect = new Rect(0, 0, width, height);
		  final RectF rectF = new RectF(rect);
		 
		  // draw Round rectangle to canvas
		  canvas.drawRoundRect(rectF, round, round, paint);
		 
		  // create Xfer mode
		  paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		  // draw source image to canvas
		  canvas.drawBitmap(src, rect, rect, paint);
		 
		  // return final image
		  return result;
	}
	
	public static Bitmap drawTextToBitmap(Context gContext, int gResId, String gText) {
		Resources resources = gContext.getResources();
		float scale = resources.getDisplayMetrics().density;
		Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);

		android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
		// set default bitmap configuration if none
		if (bitmapConfig == null) {
			bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		}
		// resource bitmaps are immutable,
		// so we need to convert it to mutable one
		bitmap = bitmap.copy(bitmapConfig, true);

		Canvas canvas = new Canvas(bitmap);
		// new antialiased Paint
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// text color - #3D3D3D
		paint.setColor(Color.rgb(0, 0, 0));		
		// text size in pixels
		int textSize = 21;
		paint.setTextSize((int) (textSize * scale));
		// text shadow	
		paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

		// draw text to the Canvas center
		Rect bounds = new Rect();
		paint.getTextBounds(gText, 0, gText.length(), bounds);
		int x = (bitmap.getWidth() - bounds.width()) / 2;
		int y = (int) ((bitmap.getHeight() + bounds.height()) * 7 / (textSize - 2));

		canvas.drawText(gText, x, y, paint);

		return bitmap;
	}
}
