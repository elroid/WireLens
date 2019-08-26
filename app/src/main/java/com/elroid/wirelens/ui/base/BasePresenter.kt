package com.elroid.wirelens.ui.base

import com.elroid.wirelens.domain.ResourceProvider
import com.elroid.wirelens.exception.FatalException
import com.github.ajalt.timberkt.e
import com.github.ajalt.timberkt.i
import com.github.ajalt.timberkt.w
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus

/**
 * Class: com.elroid.wirelens.ui.base.BasePresenter
 * Project: WireLens
 * Created Date: 17/01/2018 16:44
 *
 * @author [Elliot Long](mailto:e@elroid.com)
 * Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
abstract class BasePresenter<out V:BaseView> constructor(
	val view:V,
	private val resProvider:ResourceProvider
):BasePres {

	private val disposables = CompositeDisposable()

	protected fun schedulers() = SchedulersFacade()

	open val eventBusEnabled = false

	/**
	 * Contains common startup actions needed for all presenters, if any.
	 * Subclasses may override this.
	 */
	override fun onStart() {
		i { "Lifecycle: ${name()}.start" }
	}

	/**
	 * Actions needed when user resumes activity (default presume start)
	 */
	override fun onResume() {
		i { "Lifecycle: ${name()}.onResume" }
		if(eventBusEnabled) EventBus.getDefault().register(this)
	}

	/**
	 * Contains common cleanup actions needed for all presenters, if any.
	 * Subclasses may override this.
	 */
	override fun onPause() {
		i { "Lifecycle: ${name()}.onPause" }
		if(eventBusEnabled) EventBus.getDefault().unregister(this)
	}

	override fun onStop() {
		i { "Lifecycle: ${name()}.onStop" }
		disposables.clear()
	}

	open fun name():String {
		return javaClass.simpleName
	}

	override fun error(exception:Throwable, log:Boolean, errorMessage:Any?,
					   severity:Severity?) {
		view.hideProgress()//just in case

		e(exception) { "Error encountered: ${exception.message}" }

		if(errorMessage != null)
			view.showError(resProvider.getString(errorMessage), severity = severity)
		else {
			val err = exception.message ?: exception.toString()
			view.showError(msg = err, severity = severity)
		}
		if(log || (exception is FatalException)) {
			w{"Error shown to user:$exception"}
			//logError(exception, exception.message ?: "Error shown to user")
		}
	}

	protected fun add(disposable:Disposable) {
		disposables.add(disposable)
	}
}

