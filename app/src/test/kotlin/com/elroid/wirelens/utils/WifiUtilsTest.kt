package com.elroid.wirelens.utils

import com.elroid.wirelens.framework.RoboelectricTest
import com.elroid.wirelens.util.WifiUtils
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 *
 * Class: com.elroid.wirelens.test.FileUtilsTest
 * Project: WireLens
 * Created Date: 22/01/2018 16:22
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
class WifiUtilsTest : RoboelectricTest() {

	@Test
	fun testIsSameSsid() {
		assertTrue(WifiUtils.isSameSsid("It's a Trap", "\"It's a Trap\""))
		assertTrue(WifiUtils.isSameSsid("\"It's a Trap\"", "It's a Trap"))
		assertTrue(WifiUtils.isSameSsid("\"It's a Trap\"", "It's a Trap"))
		assertTrue(WifiUtils.isSameSsid("\"IT'S A TRAP\"", "It's a Trap"))
		assertFalse(WifiUtils.isSameSsid("\"It's a Trap\"", "It's a Trap "))
	}

}