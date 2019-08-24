package com.elroid.wirelens.injection;

import com.elroid.wirelens.data.local.ConnectorService;
import com.elroid.wirelens.ui.importer.ImporterActivity;
import com.elroid.wirelens.ui.main.MainActivity;
import com.elroid.wirelens.ui.main.MainModule;
import com.elroid.wirelens.ui.main.MainViewModule;
import com.elroid.wirelens.ui.test.TestActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Class: com.elroid.wirelens.injection.BuildersModule
 * Project: WireLens
 * Created Date: 17/01/2018 16:54
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
@Module
public abstract class BuildersModule {

	@ContributesAndroidInjector(modules = {MainModule.class, MainViewModule.class})
	abstract MainActivity bindMainActivity();

	@ContributesAndroidInjector
	abstract ImporterActivity bindImporterActivity();

	@ContributesAndroidInjector
	abstract TestActivity bindTestActivity();

	@ContributesAndroidInjector
	abstract ConnectorService bindConnectorService();
}
