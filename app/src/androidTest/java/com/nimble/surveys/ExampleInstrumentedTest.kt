package com.nimble.surveys

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.nimble.surveys.api.SurveysApi
import com.nimble.surveys.di.createMoshi
import com.nimble.surveys.di.createOkHttpClient
import com.nimble.surveys.di.createWebService
import com.nimble.surveys.ui.main.MainActivity
import com.nimble.surveys.utils.RecyclerViewItemCountAssertion
import com.nimble.surveys.utils.TestFileUtils
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class ExampleInstrumentedTest {
    @get:Rule
    val mActivityRule = ActivityTestRule(MainActivity::class.java, true, false)

    private val server: MockWebServer = MockWebServer()
    val api = createWebService<SurveysApi>(createOkHttpClient(), createMoshi())

    @Before
    fun setup() {
        server.start()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(
                    TestFileUtils.getStringFromFile(
                        getInstrumentation().context, "surveys_0.json"
                    )
                )
        )

        val intent = Intent()
        mActivityRule.launchActivity(intent)
    }

    @Test
    fun testOnClickMenu() {
        onView(withId(R.id.action_menu))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withText(R.string.menu_clicked))
            .inRoot(withDecorView(not(`is`(mActivityRule.activity.window.decorView))))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testOnLoadMore() {
        onView(withId(R.id.recyclerView)).perform(ViewActions.swipeUp())

//        onView(withId(android.R.id.home))
//            .check(matches(isDisplayed()))
//            .perform(click())

//        onView(withId(R.id.swipeRefreshLayout))
//            .check(matches(isDisplayed()))

        // mActivityRule.activity.binding.swipeRefreshLayout
//        onView(withText(R.string.menu_clicked))
//            .inRoot(withDecorView(not(`is`(mActivityRule.activity.window.decorView))))
//            .check(matches(isDisplayed()))
        // onView(withId(R.id.recyclerView)).check(RecyclerViewItemCountAssertion(20))
    }

    @Test
    fun testOnRefresh() {
        onView(withContentDescription(R.string.abc_action_bar_up_description))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.recyclerView)).check(RecyclerViewItemCountAssertion(10))
    }
}

