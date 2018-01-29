package com.elroid.wirelens.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.elroid.wirelens.util.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Class: com.elroid.wirelens.model.Image
 * Project: WireLens
 * Created Date: 25/01/2018 17:29
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class CredentialsImage
{
	private Bitmap bitmap;
	private File imageFile;

	public CredentialsImage(Bitmap image){
		this.bitmap = image;
	}

	public CredentialsImage(File imageFile){
		this.imageFile = imageFile;
	}

	public Bitmap getBitmap() throws IOException{
		if(bitmap == null)
			bitmap = FileUtils.createBitmapFromFile(imageFile);
		return bitmap;
	}

	public File getFile(Context ctx) throws IOException{
		if(imageFile == null) //throw new RuntimeException("Have not yet written bitmap->file conversion");
			imageFile = FileUtils.createFile(ctx, bitmap);
		return imageFile;
	}
}
