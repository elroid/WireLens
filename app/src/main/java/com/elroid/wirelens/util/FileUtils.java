package com.elroid.wirelens.util;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
		photoFile.createNewFile();
		writeToFile(pictureInputStream, photoFile);
		return photoFile;
	}

	private static File tempImageDirectory(@NonNull Context context) {
		File privateTempDir = new File(context.getCacheDir(), DEFAULT_FOLDER_NAME);
		if (!privateTempDir.exists()) privateTempDir.mkdirs();
		return privateTempDir;
	}

	public static void writeToFile(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeToFile(Bitmap bitmap, File file) {
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Bitmap createBitmapFromFile(File imgFile) throws IOException{
		Timber.d("createBitmapFromFile(imgFile:%s)", imgFile);
		/*Uri photoUri = FileProvider.getUriForFile(ctx, ctx.getPackageName() + ".provider", imgFile);
		return MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), photoUri);*/
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		return BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
	}

	/**
	 * To find out the extension of required object in given uri
	 * Solution by http://stackoverflow.com/a/36514823/1171484
	 */
	public static String getExtension(@NonNull Context context, @NonNull Uri uri) {
		String extension;

		//Check uri format to avoid null
		if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
			//If scheme is a content
			final MimeTypeMap mime = MimeTypeMap.getSingleton();
			extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
		} else {
			//If scheme is a File
			//This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
			extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

		}

		return extension;
	}
}
