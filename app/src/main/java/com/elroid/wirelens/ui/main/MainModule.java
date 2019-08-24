package com.elroid.wirelens.ui.main;

import com.elroid.wirelens.ui.base.SchedulersFacade;

import dagger.Module;
import dagger.Provides;

/**
 * Class: com.elroid.wirelens.ui.main.MainModule
 * Project: WireLens
 * Created Date: 06/02/2018 16:04
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
@Module
public class MainModule
{
	@Provides
	MainPresenter provideMainPresenter(MainContract.View view, SchedulersFacade schedulersFacade){
		return new MainPresenter(view, schedulersFacade);
	}
}
