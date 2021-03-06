package com.elroid.wirelens.domain;

import com.elroid.wirelens.model.CredentialsImage;
import com.elroid.wirelens.model.OcrResponse;

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
	Single<OcrResponse> getVisionResponse(CredentialsImage image);
}
