package com.elroid.wirelens.util

import android.content.Intent
import android.os.Looper
import com.github.ajalt.timberkt.w

/**
 * Created Date: 01/06/2018 16:31
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
fun Intent.toLog(): String {
	var result = this.toString()
	if(this.data != null)
		result += "\nData: " + this.data
	try {
		val b = this.extras
		if(b != null && !b.isEmpty) {
			result += " "
			val keys = b.keySet()
			val it = keys.iterator()
			var firstPrinted = false
			while(it.hasNext()) {
				val key = it.next()
				val o = b.get(key)
				if(firstPrinted)
					result += ","
				firstPrinted = true
				result += "$key=${o?.toString()})"
			}
		}
	} catch(e: Exception) {
		result += "\nError: " + e.message
		w(e) { "Error printing intent" }
	}
	return result
}

private fun print(varName: String, varValue: Any?, addNewline: Boolean = true): String {
	val nl = if(addNewline) "\n" else ""
	return if(varValue == null)
		""
	else if(varValue is CharSequence && varValue != "")
		"$nl$varName=$varValue"
	else if(varValue is Int && varValue != 0)
		"$nl$varName=$varValue"
	else
		"$nl$varName=$varValue"
}

fun printCallingMethod(start:Int = 3, levels:Int = 0): String {
	return printMethod(start, levels)
}

private fun printMethod(start: Int, levels: Int): String {
	try {
		throw Exception("Stack trace: (" + Thread.currentThread() + ")")
	} catch(e: Exception) {
		val elems = e.stackTrace
		return if(levels == 0)
			elems[start].toString()
		else {
			val r = StringBuilder("Methods:")
			var i = start
			while(i < elems.size && i < start + levels) {
				val elem = elems[i]
				r.append("\n[").append(i).append("]: ").append(elem)
				i++
			}
			r.toString()
		}
	}
}

fun isUiThread(): Boolean =
	Looper.getMainLooper().thread === Thread.currentThread()
