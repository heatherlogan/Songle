package com.example.heatherlogan.songle;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Created by heatherlogan on 11/12/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GameActivityButtonsTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void goToMapActivity(){
        onView(withId(R.id.view_map))
                .perform(click());
        onView(allOf(withId(R.id.google_map), isDisplayed()));

    }







}
