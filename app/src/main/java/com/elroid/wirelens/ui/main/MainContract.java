package com.elroid.wirelens.ui.main;

import android.graphics.Bitmap;

import com.elroid.wirelens.model.CredentialsImage;
import com.elroid.wirelens.model.WifiNetwork;
import com.elroid.wirelens.model.WifiState;
import com.karumi.dexter.PermissionToken;

import java.util.List;

/**
 * Class: com.elroid.wirelens.ui.main.MainContract
 * Project: WireLens
 * Created Date: 06/02/2018 15:17
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
interface MainContract
{
	interface View{
		void showQrCode(Bitmap qrCode);
		void showCurrentWifiData(WifiNetwork network, WifiState state);
		void showWifiList(List<WifiNetwork> list);

		void takePictureWithPermissions();
		//void showPicturePermissionsRationale(PermissionToken token);
		//void takePicture();

		void openGalleryWithPermissions();
		//boolean showGalleryPermissionsRationale();
		//void openGallerySelector();

		void startConnectorService(CredentialsImage image);
		void showConnectorStartedMessage();

		void showError(String message);
	}

	interface Presenter{
		void onCameraButtonClicked();
		void onGalleryButtonClicked();
		void onImageSelected(CredentialsImage image);
		void generateQrCode(WifiNetwork network);

		void loadCurrentWifiState();
		void loadSavedWifiNetworks();
	}
}
