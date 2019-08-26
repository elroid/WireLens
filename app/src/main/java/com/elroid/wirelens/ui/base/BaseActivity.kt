package com.elroid.wirelens.ui.base


import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.elroid.wirelens.util.createString
import com.elroid.wirelens.util.printCallingMethod
import com.github.ajalt.timberkt.w
import timber.log.Timber
import kotlin.system.exitProcess

/**
 * Class: com.elroid.wirelens.ui.base.BaseActivity
 * Project: WireLens
 * Created Date: 17/01/2018 17:46
 *
 * @author [Elliot Long](mailto:e@elroid.com)
 * Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
abstract class BaseActivity:AppCompatActivity(), BaseActivityView {

	override fun getCtx():Context {
		return this
	}

	override fun toast(msg:Any, length:Int) {
		Timber.d("toast: %s", msg)
		Toast.makeText(this, createString(msg), length).show()
	}

	override fun showProgress() {
		toast("Please wait...")
		w { "showProgress() not implemented" }
	}

	override fun hideProgress() {
		w { "hideProgress() not implemented" }
	}

	override fun showError(msg:Any, severity:Severity?) {
		w { "showError($msg, $severity) not implemented" }
	}

	override fun quit() {
		exitProcess(0)
	}

	/**
	 * Marks the given function as to-be-completed
	 */
	fun todo(vararg args: Any?){
		var msg = printCallingMethod(4).replace("${javaClass.`package`?.name}.", "")
		if(args.isNotEmpty()) msg += "\nwith args:${args.joinToString(",")}\n"
		msg += " is not yet implemented"
		w{msg}
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
	}
}
