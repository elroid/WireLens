package com.elroid.wirelens.domain

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface ResourceProvider {
	fun getString(msg:Any?): String

	fun getString(@StringRes resId: Int, vararg args: Any): String

	@ColorInt
	fun getColor(@ColorRes resId: Int): Int

	fun getDrawable(@DrawableRes resId: Int): Drawable?
}