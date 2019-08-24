package com.elroid.wirelens.ui.main;

/**
 * Class: com.elroid.wirelens.ui.main.MainViewModel
 * Project: WireLens
 * Created Date: 06/02/2018 16:08
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */

import dagger.Binds;
import dagger.Module;

@Module
public abstract class MainViewModule
{
	@Binds
	abstract MainContract.View provideMainView(MainActivity activity);
}
