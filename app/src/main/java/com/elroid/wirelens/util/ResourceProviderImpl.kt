package com.elroid.wirelens.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.elroid.wirelens.domain.ResourceProvider

class ResourceProviderImpl(val ctx:Context) :ResourceProvider {

	/*override fun getString(@StringRes id: Int, vararg args: Any): String {
		return if (args.size == 1) {
			val stringArg = ctx.createString(args[0])
			ctx.getString(id, stringArg)
		} else {
			val stringArgs = arrayOfNulls<String>(args.size)
			args.forEachIndexed { index, it ->
				stringArgs[index] = ctx.createString(it)
			}
			return ctx.getString(id, *stringArgs)
		}
	}

	override fun getString(@StringRes resId: Int): String = ctx.getString(resId)*/

	override fun getString(msg:Any?):String = ctx.createString(msg)
	override fun getString(@StringRes resId: Int, vararg args:Any):String
		= ctx.createString(StringVars(resId, args))

	@ColorInt
	override fun getColor(@ColorRes resId: Int): Int = ContextCompat.getColor(ctx, resId)

	override fun getDrawable(@DrawableRes resId: Int): Drawable? = ContextCompat.getDrawable(ctx, resId)
}