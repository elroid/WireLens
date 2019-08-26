package com.elroid.wirelens.ui.base

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner

interface BaseActivityView:BaseView {
	fun getCtx():Context
}

interface BaseFragmentView:BaseView {
	fun getCtx():Context?
}

interface BaseView:LifecycleOwner {
	fun toast(msg:Any, length:Int = Toast.LENGTH_SHORT)
	fun showProgress()
	fun hideProgress()
	fun showError(msg:Any, severity:Severity? = null)
	fun quit()
}