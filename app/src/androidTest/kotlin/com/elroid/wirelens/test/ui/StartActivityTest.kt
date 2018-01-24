package com.elroid.wirelens.test.ui

import com.elroid.wirelens.ui.start.StartActivity
import com.elroid.wirelens.test.framework.AcceptanceTest
import org.junit.Assert.assertTrue
import org.junit.Test

class StartActivityTest : AcceptanceTest<StartActivity>(StartActivity::class.java) {

  @Test
  fun emptyTestTodo(){
    assertTrue(true)
  }
  /*@Test //methodNameUnderTest_givenCondition_expectedBehavior
  fun onCreate_givenNothing_shouldOpenStartActivity() {

    checkThat.nextOpenActivityIs(MainActivity::class.java)
  }*/

  /*@Test
  fun shouldDisplayAction() {
    events.clickOnView(R.id.fab)
    checkThat.viewIsVisibleAndContainsText(R.string.action)
  }*/
}
