package com.elroid.wirelens.ui.main;

import com.elroid.wirelens.model.CredentialsImage;
import com.elroid.wirelens.model.WifiNetwork;
import com.elroid.wirelens.ui.base.BasePresenter;
import com.elroid.wirelens.ui.base.SchedulersFacade;
import com.jakewharton.rxrelay2.BehaviorRelay;

import java.util.Comparator;

import timber.log.Timber;

/**
 * Class: com.elroid.wirelens.ui.main.MainPresenter
 * Project: WireLens
 * Created Date: 06/02/2018 15:56
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter
{
	private final SchedulersFacade schedulersFacade;

	public MainPresenter(MainContract.View view, SchedulersFacade schedulersFacade){
		super(view);
		this.schedulersFacade = schedulersFacade;

		/*new Comparator<String>(){
			@Override
			public int compare(String o1, String o2){
				return 0;
			}
		}*/
	}

	@Override
	public void onImageSelected(CredentialsImage image){
		Timber.d("onImageSelected(image:%s)", image);
		view.startConnectorService(image);
		view.showConnectorStartedMessage();
	}

	@Override
	public void generateQrCode(WifiNetwork network){
		view.showError("Qr generator not yet implemented");
	}

	@Override
	public void loadCurrentWifiState(){

	}

	@Override
	public void loadSavedWifiNetworks(){

	}

	@Override
	public void onCameraButtonClicked(){

	}

	@Override
	public void onGalleryButtonClicked(){

	}
}
