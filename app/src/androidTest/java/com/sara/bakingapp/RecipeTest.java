package com.sara.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.sara.bakingapp.IdlingResource.SimpleIdlingResource;
import com.sara.bakingapp.activity.RecipeActivity;
import com.sara.bakingapp.activity.RecipeDetailActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by sara on 4/26/2018.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeTest {

    public static final String RECIPE_NUTELLA = "Nutella Pie";
    public static final String RECIPE_BROWNIES = "Brownies";
    public static final String RECIPE_YELLOW = "Yellow Cake";
    public static final String RECIPE_CHEESE = "Cheesecake";

    IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<RecipeActivity> mRecipeTest
            = new IntentsTestRule<>(RecipeActivity.class);


    @Before
    public void registerIdlingResource() {
        mIdlingResource = mRecipeTest.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void checkText_MainActivity() {
        try {
            //Delay to have list available for test
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(ViewMatchers.withId(R.id.recipes_list)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(withText(RECIPE_BROWNIES)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

}