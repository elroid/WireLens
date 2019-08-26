package com.elroid.wirelens.ui.base

interface BasePres {
	fun onStart()
	fun onResume()
	fun onPause()
	fun onStop()
	fun error(exception:Throwable, log:Boolean = false, errorMessage:Any? = null, severity: Severity? = null)
}

enum class Severity{
	NONE, BACK, CLOSE_ACTIVITY, CLOSE_APP
}