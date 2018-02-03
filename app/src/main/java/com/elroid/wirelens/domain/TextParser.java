package com.elroid.wirelens.domain;

import com.elroid.wirelens.model.OcrResponse;
import com.elroid.wirelens.model.TextParserResponse;

import io.reactivex.Observable;

/**
 * Class: com.elroid.wirelens.domain.TextParser
 * Project: WireLens
 * Created Date: 23/01/2018 15:00
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public interface TextParser
{
	Observable<TextParserResponse> parseResponse(OcrResponse gvr);
}
