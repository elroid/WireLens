package com.elroid.wirelens.uitest.framework

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
abstract class AcceptanceTest<T : Activity>(clazz: Class<T>) {

  @Rule @JvmField
  val testRule: ActivityTestRule<T> = MyIntentsTestRule(clazz)

  inner class MyIntentsTestRule(activityClass: Class<T>) : IntentsTestRule<T>(activityClass) {
    private val classToTest: Class<T> = activityClass

    override fun getActivityIntent(): Intent {
      return createIntent(InstrumentationRegistry.getTargetContext(), classToTest)
    }
  }

  /**
   * To be overridden if a custom intent is needed
   * [ctx] is the launching activity (or Application) context.
   * [activityClass] is the class of the activity being launched
   *
   * @return Intent to launch the activity with
   */
  open fun createIntent(ctx: Context, activityClass: Class<T>): Intent {
    return Intent(ctx, activityClass)
  }

  val checkThat: Matchers = Matchers()
  val events: Events = Events()
}

