package com.elroid.wirelens;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import timber.log.Timber;

/**
 * Class: com.elroid.wirelens.FileUtils
 * Project: WireLens
 * Created Date: 18/12/2017 18:10
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2017 Elroid Ltd. All rights reserved.
 */
public class FileUtils
{
	private static final String DEFAULT_FOLDER_NAME = "images";

	static File pickedExistingPicture(@NonNull Context context, @Nullable Uri photoUri) throws IOException{
		Timber.d("pickedExistingPicture(context:%s, photoUri:%s)", context, photoUri);
		if(photoUri == null) return null;
		InputStream pictureInputStream = context.getContentResolver().openInputStream(photoUri);
		File directory = tempImageDirectory(context);
		File photoFile = new File(directory, UUID.randomUUID().toString() + "." + getMimeType(context, photoUri));
		photoFile.createNewFile();
		writeToFile(pictureInputStream, photoFile);
		return photoFile;
	}

	private static File tempImageDirectory(@NonNull Context context) {
		File privateTempDir = new File(context.getCacheDir(), DEFAULT_FOLDER_NAME);
		if (!privateTempDir.exists()) privateTempDir.mkdirs();
		return privateTempDir;
	}

	private static void writeToFile(InputStream in, File file) {
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

	/**
	 * To find out the extension of required object in given uri
	 * Solution by http://stackoverflow.com/a/36514823/1171484
	 */
	private static String getMimeType(@NonNull Context context, @NonNull Uri uri) {
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
