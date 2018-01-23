package com.elroid.wirelens.domain;

import android.graphics.Bitmap;

import com.elroid.wirelens.model.GoogleVisionResponse;

import java.io.File;

import io.reactivex.Single;

/**
 * Class: com.elroid.wirelens.domain.GoogleVisionRemoteService
 * Project: WireLens
 * Created Date: 22/01/2018 15:25
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public interface GoogleVisionRemoteRepository
{
	Single<GoogleVisionResponse> getVisionResponse(File imgFile);
	Single<GoogleVisionResponse> getVisionResponse(Bitmap bitmap);
}
