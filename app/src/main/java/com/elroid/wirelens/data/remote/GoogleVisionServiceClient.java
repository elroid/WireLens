package com.elroid.wirelens.data.remote;

import com.elroid.wirelens.domain.GoogleVisionRemoteRepository;
import com.elroid.wirelens.model.GoogleVisionResponse;

import java.io.File;

import io.reactivex.Single;

/**
 * Class: com.elroid.wirelens.data.remote.GoogleVisionServiceClient
 * Project: WireLens
 * Created Date: 22/01/2018 15:30
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class GoogleVisionServiceClient implements GoogleVisionRemoteRepository
{
	@Override public Single<GoogleVisionResponse> getVisionResponse(File imgFile){
		return Single.just(new GoogleVisionResponse("Your message here"));//todo
	}
}
