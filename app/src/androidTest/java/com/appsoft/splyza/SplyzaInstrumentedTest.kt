package com.appsoft.splyza

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.appsoft.splyza.ui.activity.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class SplyzaInstrumentedTest {
    lateinit var activityScenarioRule: ActivityScenario<MainActivity>

    @Before
    fun setup() {

    }

    @After
    fun tearDown() {
        activityScenarioRule.close()
    }

    @Test
    fun landing_fragment_test() {
        activityScenarioRule = ActivityScenario.launch(MainActivity::class.java)
        Espresso.onView(ViewMatchers.withText("Invite")).check(matches(isDisplayed()))
    }

    @Test
    fun main_fragment_test() {
        activityScenarioRule = ActivityScenario.launch(MainActivity::class.java)
        Espresso.onView(ViewMatchers.withText("Invite")).perform(click())
        Espresso.onView(ViewMatchers.withText("Back")).check(matches(isDisplayed()))
        Espresso.onView(ViewMatchers.withText("Invite Members")).check(matches(isDisplayed()))
    }

    @Test
    fun main_fragment_back_button_click_test() {
        activityScenarioRule = ActivityScenario.launch(MainActivity::class.java)
        Espresso.onView(ViewMatchers.withText("Invite")).perform(click())
        Espresso.onView(ViewMatchers.withText("Back")).perform(click())
        Espresso.onView(ViewMatchers.withText("Invite")).check(matches(isDisplayed()))
    }

    @Test
    fun main_fragment_copy_link_btn_visibility_test() {
        activityScenarioRule = ActivityScenario.launch(MainActivity::class.java)
        Espresso.onView(ViewMatchers.withText("Invite")).perform(click())
        Espresso.onView(ViewMatchers.withText("Copy Link")).check(matches(isDisplayed()))
    }

    @Test
    fun main_fragment_share_qr_code_btn_visibility_test() {
        activityScenarioRule = ActivityScenario.launch(MainActivity::class.java)
        Espresso.onView(ViewMatchers.withText("Invite")).perform(click())
        Espresso.onView(ViewMatchers.withText("Share QR Code")).check(matches(isDisplayed()))
    }

    @Test
    fun main_fragment_share_qr_code_btn_test() {
        activityScenarioRule = ActivityScenario.launch(MainActivity::class.java)
        Espresso.onView(ViewMatchers.withText("Invite")).perform(click())
        Espresso.onView(ViewMatchers.withText("Share QR Code")).perform(click())
        Espresso.onView(ViewMatchers.withText("My QR Code")).check(matches(isDisplayed()))
        Espresso.onView(ViewMatchers.withText("Back")).perform(click())
        Espresso.onView(ViewMatchers.withText("Invite Members")).check(matches(isDisplayed()))
    }

    @Test
    fun main_fragment_permission_level_dlg_test() {
        activityScenarioRule = ActivityScenario.launch(MainActivity::class.java)
        Espresso.onView(ViewMatchers.withText("Invite")).perform(click())
        Espresso.onView(ViewMatchers.withText("Coach")).perform(click())
        Espresso.onView(ViewMatchers.withText("Player")).check(matches(isDisplayed()))
        Espresso.onView(ViewMatchers.withText("Coach")).check(matches(isDisplayed()))
        Espresso.onView(ViewMatchers.withText("Player Coach")).check(matches(isDisplayed()))
    }
}