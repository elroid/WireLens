package com.elroid.wirelens.util

import android.content.Context
import android.content.res.Resources
import android.util.Log.w
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.elroid.wirelens.R
import com.github.ajalt.timberkt.w
import java.security.InvalidParameterException
import java.util.regex.Pattern

/**
 * Class: com.elroid.wirelens.util.TextUtils
 * Project: WireLens
 * Created Date: 24/01/2018 09:28
 *
 * @author [Elliot Long](mailto:e@elroid.com)
 * Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
//this is a util class, methods should be public and may not yet be used
class TextUtils {

	companion object {
		private val HEX_DIGITS = Pattern.compile("[0-9A-Fa-f]+")

		@JvmStatic
		fun getValueFromLineStartingWith(label:String, lines:Array<String>):String? {
			for(i in lines.indices) {
				val line = lines[i]
				val index:Int = indexOfIgnoreCase(line, label)
				if(index != -1) {
					var tail = line.substring(index + label.length).trim { it <= ' ' }
					while(tail.startsWith(":"))
						tail = tail.substring(1)
					return if(tail.trim { it <= ' ' } == "") {
						//assume it is the next line
						if(i + 1 < lines.size) {
							lines[i + 1].trim { it <= ' ' }
						} else
							""
					} else tail.trim { it <= ' ' }
				}
			}
			return null
		}

		@JvmStatic
		fun indexOfIgnoreCase(str:String, tok:String):Int {
			val strUpper = str.toUpperCase()
			val tokUpper = tok.toUpperCase()
			return strUpper.indexOf(tokUpper)
		}

		@JvmStatic
		fun containsIgnoreCase(str:String, tok:String):Boolean {
			return str.toUpperCase().contains(tok.toUpperCase())
		}

		@JvmStatic
		fun equalsIgnoreCase(str:String, tok:String):Boolean {
			return str.toUpperCase() == tok.toUpperCase()
		}

		@JvmStatic
		fun isHex(str:String?):Boolean {
			return str != null && HEX_DIGITS.matcher(str).matches()
		}
	}
}

/**
 * Creates a string from just about any string-related structure
 */
fun Context.createString(msg: Any?, defaultValue: String? = null): String {
	return createStringOrNull(msg) ?: defaultValue ?: getString(R.string.err_unspecified)
}
fun Fragment.createString(msg: Any?, defaultValue: String? = null): String {
	return context?.createString(msg, defaultValue)?: context?.getString(R.string.err_unspecified) ?: "--"
}

fun Context.createStringOrNull(msg: Any?): String? {
	return try {
		when (msg) {
			is Int -> getString(msg)
			is String -> msg
			is StringVars -> getString(msg.msgId, *parseStringArgs(msg.args))
			null -> null
			else -> {
				InvalidParameterException("Invalid msg specified: $msg").also { w(it) { "Error: ${it.message}" } }
				null
			}
		}
	} catch (e: Exception) {
		w(e) { "couldn't create string" }
		null
	}
}

/**
 * Acts as a container for StringRes ids with arguments
 * e.g. StringVars(R.string.string_with_vars, var1, var2...)
 */
class StringVars(@StringRes val msgId: Int, vararg val args: Any)

/**
 * If stringRes has been passed as an arg, expand it to a string. Ignore others.
 */
fun Context.parseStringArgs(args: Array<out Any>): Array<out Any> {
	return args.map { arg ->
		if (arg is Int) {
			try {
				getString(arg)
			} catch (e: Resources.NotFoundException) {
				arg//this is an integer, not a @StringRes
			}
		} else arg
	}.toTypedArray()
}