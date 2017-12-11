package com.example.heatherlogan.songle;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.assertEquals;

/**
 * Created by heatherlogan on 11/12/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityButtonsTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void goToNewGameActivity() {
        onView(withId(R.id.newGameButton))
                .perform(click());
        onView(allOf(withId(R.id.yesButton), isDisplayed()))
                .perform(click());
        onView(allOf(withId(R.id.veryEasyRB), isDisplayed()))
                .perform(click());
        onView(allOf(withId(R.id.playButton7), isDisplayed()))
                .perform(click());
        onView(allOf(withId(R.id.playingSongleTV), isDisplayed()));
    }

    @Test
    public void goToScoreboardActivity() {
        onView(withId(R.id.scoreboardButton))
                .perform(click());
        onView(allOf(withId(R.id.textView3), isDisplayed()));
    }

    @Test
    public void goToExtrasActivity() {
        onView(withId(R.id.extrasButton))
                .perform(click());
        onView(allOf(withId(R.id.textView2), isDisplayed()));
        }

}

