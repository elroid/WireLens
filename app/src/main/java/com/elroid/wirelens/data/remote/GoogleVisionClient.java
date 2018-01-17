package com.elroid.wirelens.data.remote;

import retrofit2.http.POST;

/**
 * Class: com.elroid.wirelens.data.remote.GoogleVisionClient
 * Project: WireLens
 * Created Date: 04/01/2018 10:13
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2017 Elroid Ltd. All rights reserved.
 */
public interface GoogleVisionClient
{
	String ENDPOINT = "https://vision.googleapis.com/v1/";//images:annotate";

	/*@POST("images:annotate")
	Observable<OcrResponse> extractText();*/
}
