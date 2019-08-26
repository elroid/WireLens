package com.elroid.wirelens.ui.main

import android.graphics.Bitmap
import android.net.Uri

import com.elroid.wirelens.model.CredentialsImage
import com.elroid.wirelens.model.WifiNetwork
import com.elroid.wirelens.model.WifiState
import com.elroid.wirelens.ui.base.BaseActivityView
import com.elroid.wirelens.ui.base.BasePres
import java.io.File

/**
 * Class: com.elroid.wirelens.ui.main.MainContract
 * Project: WireLens
 * Created Date: 06/02/2018 15:17
 *
 * @author [Elliot Long](mailto:e@elroid.com)
 * Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
interface MainContract {
	interface View:BaseActivityView {
		fun showQrCode(qrCode:Bitmap)
		fun showCurrentWifiData(network:WifiNetwork, state:WifiState)
		fun showWifiList(list:List<WifiNetwork>)

		fun takePictureWithPermissions(tmpFile:File):Any?
		fun openGalleryWithPermissions():Any?
		fun startQrScanner()

		fun startConnectorService(image:CredentialsImage)
		fun showConnectorStartedMessage()

		//fun showError(message:String)
	}

	interface Presenter:BasePres {
		fun onCameraButtonClicked()
		fun onGalleryButtonClicked()
		fun onQrButtonClicked()
		fun onImageSelected(image:CredentialsImage)
		fun generateQrCode(network:WifiNetwork)

		fun onGalleryImageSelected(uri:Uri)
		fun onCameraImageSelected()

		fun loadCurrentWifiState()
		fun loadSavedWifiNetworks()
	}
}
