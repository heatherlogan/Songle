package com.example.heatherlogan.songle;

import android.util.Log;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameActivityTest {


    @Test
    public void formatTime() throws Exception {

        int timeElapsed = 3000;
        String formattedTime;
        String expectedAns = "03 seconds";

        GameActivity gameActivity = new GameActivity();

        formattedTime = gameActivity.formatTime(timeElapsed);

        assertEquals(expectedAns, formattedTime);

    }

    @Test
    public void mapToStringDifficulty() throws Exception {

        int difficulty = 4;
        String output;
        String expected = "Easy";

        GameActivity gameActivity = new GameActivity();

        output = gameActivity.mapToStringDifficulty(difficulty);

        assertEquals(expected, output);

    }

/*
    @Test
    public void scoring() throws Exception {

        User user = new User("Jack", "Easy", 1, 100);

        String expected = "first";
        String output;

        GameActivity gameActivity = new GameActivity();
        output = gameActivity.scoring(user);

        assertEquals(expected, output);

    }
*/
}