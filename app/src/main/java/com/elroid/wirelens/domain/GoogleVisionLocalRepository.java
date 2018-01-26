package com.elroid.wirelens.domain;

import com.elroid.wirelens.model.CredentialsImage;
import com.elroid.wirelens.model.GoogleVisionResponse;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Class: com.elroid.wirelens.data.local.GoogleVisionDataStore
 * Project: WireLens
 * Created Date: 18/01/2018 13:23
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public interface GoogleVisionLocalRepository
{
	Completable saveVisionResponse(CredentialsImage image, GoogleVisionResponse response);

	Single<GoogleVisionResponse> getVisionResponse(CredentialsImage image);
}
