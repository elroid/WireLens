package com.elroid.wirelens.injection;

import com.elroid.wirelens.ui.importer.ImporterActivity;

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

	/*@ContributesAndroidInjector(modules = {LobbyViewModule.class, LobbyModule.class})
	abstract LobbyActivity bindLobbyActivity();*/

	@ContributesAndroidInjector
	abstract ImporterActivity bindImporterActivity();

	// Add bindings for other sub-components here
}