package com.elroid.wirelens.ui.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Class: com.elroid.wirelens.ui.base.BasePresenter
 * Project: WireLens
 * Created Date: 17/01/2018 16:44
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
public abstract class BasePresenter<V> {

	public enum RequestState {
		IDLE,
		LOADING,
		COMPLETE,
		ERROR
	}

	protected final V view;

	private CompositeDisposable disposables = new CompositeDisposable();

	protected BasePresenter(V view) {
		this.view = view;
	}

	/**
	 * Contains common setup actions needed for all presenters, if any.
	 * Subclasses may override this.
	 */
	public void start() {
	}

	/**
	 * Contains common cleanup actions needed for all presenters, if any.
	 * Subclasses may override this.
	 */
	public void stop() {
		disposables.clear();
	}

	protected void addDisposable(Disposable disposable) {
		disposables.add(disposable);
	}
}

