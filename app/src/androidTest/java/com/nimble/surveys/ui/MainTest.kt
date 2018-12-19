package com.nimble.surveys.ui

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.nimble.surveys.R
import com.nimble.surveys.api.SurveysApi
import com.nimble.surveys.di.NetworkProperties
import com.nimble.surveys.ui.main.MainActivity
import com.nimble.surveys.ui.main.MainFragment
import com.squareup.moshi.Moshi
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.inject
import org.koin.test.KoinTest
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val roomTestModule = module {
    single(override = true) { createOkHttpClient() }
    single(override = true) { createWebService<SurveysApi>(get(), get()) }
}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    return OkHttpClient.Builder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .writeTimeout(60L, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor).build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, moshi: Moshi): T {
    return Retrofit.Builder()
        .baseUrl(NetworkProperties.SERVER_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build().create(T::class.java)
}

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainTest : KoinTest {
    private var activity: MainActivity? = null
    private var resource: IdlingResource? = null
    private val okHttpClient: OkHttpClient by inject()

    @Before
    fun launchActivity() {
        loadKoinModules(roomTestModule)

        InstrumentationRegistry.getInstrumentation().targetContext.deleteDatabase("surveys-db")

        ActivityScenario.launch(MainActivity::class.java).onActivity {
            resource = OkHttp3IdlingResource.create("OkHttp", okHttpClient)
            IdlingRegistry.getInstance().register(resource)

            this.activity = it
        }
    }

    @After
    fun clear() {
        IdlingRegistry.getInstance().unregister(resource)
    }

    @Test
    fun testOnClickMenu() {
        onView(withId(R.id.action_menu))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withText(containsString(getApplicationContext<Context>().resources.getString(R.string.menu_clicked))))
            .inRoot(withDecorView(not(`is`(activity!!.window.decorView))))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testOnRefreshAndLoadMore() {
        onView(withContentDescription(R.string.abc_action_bar_up_description))
            .check(matches(isDisplayed()))
            .perform(click())

        Thread.sleep(1000) // even we're using idling resource, need to wait for layout update.

        // to scroll 10 times to get bottom of list
        onView(withId(R.id.viewPager)).perform(ViewActions.swipeUp())
        onView(withId(R.id.viewPager)).perform(ViewActions.swipeUp())
        onView(withId(R.id.viewPager)).perform(ViewActions.swipeUp())
        onView(withId(R.id.viewPager)).perform(ViewActions.swipeUp())
        onView(withId(R.id.viewPager)).perform(ViewActions.swipeUp())
        onView(withId(R.id.viewPager)).perform(ViewActions.swipeUp())
        onView(withId(R.id.viewPager)).perform(ViewActions.swipeUp())
        onView(withId(R.id.viewPager)).perform(ViewActions.swipeUp())
        onView(withId(R.id.viewPager)).perform(ViewActions.swipeUp())
        onView(withId(R.id.viewPager)).perform(ViewActions.swipeUp())

        Thread.sleep(1000) // even we're using idling resource, need to wait for layout update.

        val mainFragment = activity!!.supportFragmentManager.findFragmentById(R.id.mainFragment) as MainFragment?
        Assert.assertThat(mainFragment!!.viewModel.adapter.items.size, `is`(20))
    }
}

