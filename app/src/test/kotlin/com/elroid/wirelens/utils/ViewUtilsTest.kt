package com.elroid.wirelens.utils

import com.elroid.wirelens.RoboelectricTest
import com.elroid.wirelens.util.ViewUtils
import org.junit.Test
import kotlin.test.assertEquals

/**
 *
 * Class: com.elroid.wirelens.test.FileUtilsTest
 * Project: WireLens
 * Created Date: 22/01/2018 16:22
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
class ViewUtilsTest : RoboelectricTest() {

	@Test
	fun testDipPx() {
		val dp = 123.0f
		val px = ViewUtils.dpToPx(dp)
		val newdp = ViewUtils.pxToDp(px)
		assertEquals(dp, newdp)
	}

}