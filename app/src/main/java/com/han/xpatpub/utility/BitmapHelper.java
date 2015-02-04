package com.han.xpatpub.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapHelper {

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;

		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
			int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path, options);
	}

	public static Bitmap decodePhotoFromFile(String path) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(path, options);

		int width = options.outWidth / 2;
		int height = options.outHeight / 2;

		if (width <= height) {
			options.inSampleSize = calculateInSampleSize(options, width, height);
		} else {
			options.inSampleSize = calculateInSampleSize(options, height, width);
		}
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path, options);
	}
}
