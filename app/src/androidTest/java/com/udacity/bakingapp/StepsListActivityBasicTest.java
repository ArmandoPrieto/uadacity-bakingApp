package com.udacity.bakingapp;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.runner.AndroidJUnit4;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static com.udacity.bakingapp.StepListActivity.ARG_RECIPE_ID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StepsListActivityBasicTest {
    @Rule
    public  IntentsTestRule <RecipeListActivity> mActivityTestRule
            = new  IntentsTestRule <>(RecipeListActivity.class);

    @Test
    public void when_recipe_clicked_shows_recipe_ingredients_and_steps(){
        onView(ViewMatchers.withId(R.id.bt_ingredients)).check(doesNotExist());
        onView(ViewMatchers.withId(R.id.item_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
        onView(ViewMatchers.withId(R.id.bt_ingredients)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.rv_recipe_steps)).check(matches(isDisplayed()));

    }

    @Test
    public void when_screen_size_is_not_large_extra_is_passed_to_intent(){
        int screenLayoutSize = mActivityTestRule.getActivity()
                .getResources()
                .getConfiguration().screenLayout;
        onView(ViewMatchers.withId(R.id.bt_ingredients)).check(doesNotExist());
        onView(ViewMatchers.withId(R.id.item_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
            if(screenLayoutSize < SCREENLAYOUT_SIZE_LARGE){
                intended(hasExtraWithKey(ARG_RECIPE_ID));
            }
        onView(ViewMatchers.withId(R.id.bt_ingredients)).check(matches(isDisplayed()));
    }
}
