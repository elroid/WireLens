package com.elroid.wirelens.model

/**
 * Class: com.elroid.wirelens.model.WifiState
 * Project: WireLens
 * Created Date: 06/02/2018 15:28
 *
 * @author [Elliot Long](mailto:e@elroid.com)
 * Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
class WifiState(val state:Int) {
	companion object {
		const val UNKNOWN = 0
		const val SAVED = 1
		const val CONNECTING = 2
		const val CONNECTED = 3
		const val AUTH_ERROR = 4
	}
}
