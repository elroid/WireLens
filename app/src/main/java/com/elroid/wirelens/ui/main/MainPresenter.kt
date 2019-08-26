package com.elroid.wirelens.ui.main

import android.net.Uri
import android.net.wifi.WifiManager
import com.elroid.wirelens.domain.ResourceProvider
import com.elroid.wirelens.model.CredentialsImage
import com.elroid.wirelens.model.WifiNetwork
import com.elroid.wirelens.ui.base.BasePresenter
import com.elroid.wirelens.util.FileUtils
import timber.log.Timber
import java.io.File

/**
 * Class: com.elroid.wirelens.ui.main.MainPresenter
 * Project: WireLens
 * Created Date: 06/02/2018 15:56
 *
 * @author [Elliot Long](mailto:e@elroid.com)
 * Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
class MainPresenter(
	view:MainContract.View,
	resProvider:ResourceProvider,
	val fileUtils:FileUtils,
	val wifiManager:WifiManager
):BasePresenter<MainContract.View>(view, resProvider), MainContract.Presenter {

	private fun getTmpFile():File {
		return _tmpFile?.let { it } ?: run {
			val file = fileUtils.createTmpFile()
			_tmpFile = file
			file
		}
	}

	private var _tmpFile:File? = null
	private fun resetTmp() {
		_tmpFile = null
	}

	override fun onImageSelected(image:CredentialsImage) {
		Timber.d("onImageSelected(image:%s)", image)
		view.startConnectorService(image)
		view.showConnectorStartedMessage()
	}

	override fun generateQrCode(network:WifiNetwork) {
		view.showError("Qr generator not yet implemented")
	}

	override fun loadCurrentWifiState() {

	}

	override fun loadSavedWifiNetworks() {

	}

	override fun onCameraButtonClicked() {
		view.takePictureWithPermissions(getTmpFile())
	}

	override fun onGalleryButtonClicked() {
		view.openGalleryWithPermissions()
	}

	override fun onQrButtonClicked() {
		view.startQrScanner()
	}

	override fun onGalleryImageSelected(uri:Uri) {
		fileUtils.copyToFile(uri, getTmpFile())
		startConnector()
	}


	override fun onCameraImageSelected() {
		startConnector()
	}

	private fun startConnector() {
		view.startConnectorService(CredentialsImage(getTmpFile()))
		view.showConnectorStartedMessage()
		resetTmp()
	}
}
