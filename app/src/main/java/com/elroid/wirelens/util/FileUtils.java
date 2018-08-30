package com.elroid.wirelens.util;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.media.ExifInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import timber.log.Timber;

/**
 * Class: com.elroid.wirelens.util.FileUtils
 * Project: WireLens
 * Created Date: 18/12/2017 18:10
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2017 Elroid Ltd. All rights reserved.
 */
@SuppressWarnings({"WeakerAccess", "SameParameterValue", "unused"})
//this is a util class, methods should be public and may only be used once or not at all
public class FileUtils
{
	private static final String DEFAULT_FOLDER_NAME = "images";

	public static File pickedExistingPicture(@NonNull Context context, @Nullable Uri photoUri) throws IOException{
		Timber.d("pickedExistingPicture(context:%s, photoUri:%s)", context, photoUri);
		if(photoUri == null) return null;
		InputStream pictureInputStream = context.getContentResolver().openInputStream(photoUri);
		File directory = tempImageDirectory(context);
		File photoFile = new File(directory, UUID.randomUUID().toString() + "." + getExtension(context, photoUri));
		if(!photoFile.createNewFile())
			Timber.w("Create file failed!");
		writeToFile(pictureInputStream, photoFile);
		return photoFile;
	}

	public static File tempImageDirectory(@NonNull Context context) throws IOException{
		File privateTempDir = new File(context.getCacheDir(), DEFAULT_FOLDER_NAME);
		if(!privateTempDir.exists()) {
			if(!privateTempDir.mkdirs())
				throw new IOException("Unable to create dirs!");
		}
		return privateTempDir;
	}

	public static void writeToFile(InputStream in, File file){
		try{
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while((len = in.read(buf)) > 0){
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void writeToFile(Bitmap bitmap, File file){
		try{
			OutputStream out = new FileOutputStream(file);
			//ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			//byte[] imageBytes = byteArrayOutputStream.toByteArray();

			/*byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}*/
			out.close();
			//in.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public static Bitmap createBitmapFromFile(File imgFile) throws IOException{
		Timber.d("createBitmapFromFile(imgFile:%s)", imgFile);
		/*Uri photoUri = FileProvider.getUriForFile(ctx, ctx.getPackageName() + ".provider", imgFile);
		return MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), photoUri);*/
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		return BitmapFactory.decodeFile(imgFile.getAbsolutePath(), bmOptions);
	}

	public static Bitmap getBitmapFromURL(String urlStr) throws IOException{
		try{
			URL url = new URL(urlStr);
			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			return BitmapFactory.decodeStream(input);
		}
		catch(IOException e){
			Timber.w(e, "Error getting bitamp from: %s", urlStr);
			return null;
		}
	}

	public static File createFile(Context ctx, Bitmap bitmap) throws IOException{
		File directory = tempImageDirectory(ctx);
		File photoFile = new File(directory, UUID.randomUUID().toString() + ".jpg");
		if(!photoFile.createNewFile())
			throw new IOException("Unable to create file: " + photoFile);
		OutputStream out = new FileOutputStream(photoFile);
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
		return photoFile;
	}


	/**
	 * To find out the extension of required object in given uri
	 * Solution by http://stackoverflow.com/a/36514823/1171484
	 */
	public static String getExtension(@NonNull Context context, @NonNull Uri uri){
		String extension;

		//Check uri format to avoid null
		if(uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)){
			//If scheme is a content
			final MimeTypeMap mime = MimeTypeMap.getSingleton();
			extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
		}
		else{
			//If scheme is a File
			//This will replace white spaces with %20 and also other special characters.
			// This will avoid returning null values on file name with spaces and special characters.
			extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

		}

		return extension;
	}

	public static Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension){

		int originalWidth = bitmap.getWidth();
		int originalHeight = bitmap.getHeight();
		int resizedWidth = maxDimension;
		int resizedHeight = maxDimension;

		if(originalHeight > originalWidth){
			resizedHeight = maxDimension;
			resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
		}
		else if(originalWidth > originalHeight){
			resizedWidth = maxDimension;
			resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
		}
		else if(originalHeight == originalWidth){
			resizedHeight = maxDimension;
			resizedWidth = maxDimension;
		}
		return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
	}

	public static Bitmap scaleBitmapToWidth(Bitmap bitmap, int maxWidth){

		int originalWidth = bitmap.getWidth();
		int originalHeight = bitmap.getHeight();
		int resizedWidth = Math.min(maxWidth, originalWidth);
		int resizedHeight = GenUtils.getHeightAtWidth(originalWidth, originalHeight, resizedWidth);

		if(resizedWidth == originalWidth) return bitmap;
		else return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
	}

	/*public static void correctRotation(CredentialsImage img, Context ctx) throws IOException{
		File file = img.getFile(ctx);
		Bitmap bitmap = img.getBitmap();
		ExifInterface exif = new ExifInterface(file.getAbsolutePath());
		int exifOrientation = exif.getAttributeInt(
			ExifInterface.TAG_ORIENTATION,
			ExifInterface.ORIENTATION_NORMAL);

		int rotate = 0;

		switch (exifOrientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
		}

		if (rotate != 0) {
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();

			// Setting pre rotate
			Matrix mtx = new Matrix();
			mtx.preRotate(rotate);

			// Rotating Bitmap & convert to ARGB_8888, required by tess
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
		}
		img.setBitmap(bitmap.copy(Bitmap.Config.ARGB_8888, true));
	}*/

	public static Bitmap getResizedBitmapFromURL(String urlStr, File localFile, int maxWidth) throws IOException{
		Timber.v("getResizedBitmapFromURL(urlStr:%s, localFile:%s, maxWidth:%s)", urlStr, localFile, maxWidth);

		URL url = new URL(urlStr);
		URLConnection connection = url.openConnection();
		connection.setDoInput(true);
		connection.connect();
		InputStream input = connection.getInputStream();
		writeToFile(input, localFile);
		FileInputStream fin = new FileInputStream(localFile);

		//check rotation
		ExifInterface exif = new ExifInterface(localFile.getAbsolutePath());
		int exifOrientation = exif.getAttributeInt(
			ExifInterface.TAG_ORIENTATION,
			ExifInterface.ORIENTATION_NORMAL);

		int rotate = 0;

		switch(exifOrientation){
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
		}

		Bitmap bitmap = BitmapFactory.decodeStream(fin);

		if(rotate != 0){
			Timber.v("rotating to %s", rotate);
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();

			// Setting pre rotate
			Matrix mtx = new Matrix();
			mtx.preRotate(rotate);

			// Rotating Bitmap & convert to ARGB_8888, required by tess
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
		}

		if(maxWidth > 0){
			//resize
			bitmap = scaleBitmapToWidth(bitmap, maxWidth);
		}

		return bitmap.copy(Bitmap.Config.ARGB_8888, true);
	}
}
