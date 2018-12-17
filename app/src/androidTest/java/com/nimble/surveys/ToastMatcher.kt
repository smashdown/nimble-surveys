package com.nimble.surveys

import android.view.WindowManager
import androidx.test.espresso.Root
import org.hamcrest.TypeSafeMatcher


class ToastMatcher : TypeSafeMatcher<Root>() {
    override fun describeTo(description: org.hamcrest.Description?) {
        description?.appendText("is toast")
    }

    override fun matchesSafely(root: Root?): Boolean {
        val type = root!!.getWindowLayoutParams().get().type
        if (type == WindowManager.LayoutParams.TYPE_TOAST) {
            val windowToken = root.getDecorView().getWindowToken()
            val appToken = root.getDecorView().getApplicationWindowToken()
            if (windowToken === appToken) {
                //means this window isn't contained by any other windows.
            }
        }
        return false
    }
}