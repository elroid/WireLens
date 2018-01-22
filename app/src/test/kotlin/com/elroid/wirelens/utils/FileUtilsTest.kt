package com.elroid.wirelens.utils

import android.net.Uri
import com.elroid.wirelens.RoboelectricTest
import com.elroid.wirelens.util.FileUtils
import org.amshove.kluent.shouldEqual
import org.junit.Test

/**
 *
 * Class: com.elroid.wirelens.test.FileUtilsTest
 * Project: WireLens
 * Created Date: 22/01/2018 16:22
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
class FileUtilsTest : RoboelectricTest() {

  @Test //methodNameUnderTest_givenCondition_expectedBehavior
  fun getExtension_givenComplexUrl_returnsExtension() {
    val uri = Uri.parse("http://something.com/jpegImage.jpg?this=that");
    val expected = "jpg";
    val actual = FileUtils.getExtension(getCtx(), uri)
    actual shouldEqual expected
  }
}