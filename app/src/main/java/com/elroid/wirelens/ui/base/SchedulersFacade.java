package com.elroid.wirelens.ui.base;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Class: com.elroid.wirelens.ui.base.SchedulersFacade
 * Project: WireLens
 * Created Date: 17/01/2018 16:47
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public class SchedulersFacade
{
	@Inject
	public SchedulersFacade() {
	}

	/**
	 * IO thread pool scheduler
	 */
	public Scheduler io() {
		return Schedulers.io();
	}

	/**
	 * Computation thread pool scheduler
	 */
	public Scheduler computation() {
		return Schedulers.computation();
	}

	/**
	 * Main Thread scheduler
	 */
	public Scheduler ui() {
		return AndroidSchedulers.mainThread();
	}
}
