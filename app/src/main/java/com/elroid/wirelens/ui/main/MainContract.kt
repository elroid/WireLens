package com.elroid.wirelens.ui.main

import android.graphics.Bitmap
import android.net.Uri

import com.elroid.wirelens.model.CredentialsImage
import com.elroid.wirelens.model.WifiNetwork
import com.elroid.wirelens.model.WifiState
import com.karumi.dexter.PermissionToken

/**
 * Class: com.elroid.wirelens.ui.main.MainContract
 * Project: WireLens
 * Created Date: 06/02/2018 15:17
 *
 * @author [Elliot Long](mailto:e@elroid.com)
 * Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
internal interface MainContract {
	interface View {
		fun showQrCode(qrCode:Bitmap)
		fun showCurrentWifiData(network:WifiNetwork, state:WifiState)
		fun showWifiList(list:List<WifiNetwork>)

		fun takePictureWithPermissions(tmpFileUri:Uri):Any?
		//void showPicturePermissionsRationale(PermissionToken token);
		//void takePicture();

		fun openGalleryWithPermissions()
		//boolean showGalleryPermissionsRationale();
		//void openGallerySelector();

		fun startConnectorService(image:CredentialsImage)
		fun showConnectorStartedMessage()

		fun showError(message:String)
	}

	interface Presenter {
		fun onCameraButtonClicked()
		fun onGalleryButtonClicked()
		fun onImageSelected(image:CredentialsImage)
		fun generateQrCode(network:WifiNetwork)

		fun onGalleryImageSelected(uri:Uri)
		fun onCameraImageSelected()

		fun loadCurrentWifiState()
		fun loadSavedWifiNetworks()
	}
}
