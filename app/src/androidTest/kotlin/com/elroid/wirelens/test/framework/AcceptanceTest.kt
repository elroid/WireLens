package com.elroid.wirelens.test.framework

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
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

	/*@Before
	fun setupLogger(){
		Timber.plant(Timber.DebugTree())
	}*/

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

