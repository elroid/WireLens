package com.elroid.wirelens.utils

import com.elroid.wirelens.model.GoogleVisionResponse
import com.elroid.wirelens.util.TextUtils
import org.junit.Assert.assertNull
import org.junit.Test
import kotlin.test.assertEquals

/**
 *
 * Class: com.elroid.wirelens.TextParserTest
 * Project: WireLens
 * Created Date: 23/01/2018 15:39
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
class TextUtilsTest {

    @Test
    fun getValueFromLineStartingWith_givenText_returnsToken(){
        val gvr = GoogleVisionResponse(
            """
            |some:line1
            |thing: line2
            |Else    line3
			|entirely:
			|  line4
            """.trimMargin()
        )

        assertEquals("line1", TextUtils.getValueFromLineStartingWith("some", gvr.lines))
        assertEquals("line2", TextUtils.getValueFromLineStartingWith("thing", gvr.lines))
        assertEquals("line3", TextUtils.getValueFromLineStartingWith("else", gvr.lines))
		assertEquals("line4", TextUtils.getValueFromLineStartingWith("entirely", gvr.lines))
		assertNull(TextUtils.getValueFromLineStartingWith("altogether", gvr.lines))
    }



}