package com.reservatiesysteem.lotte.reservatiesysteem;

import android.support.test.rule.ActivityTestRule;

import com.reservatiesysteem.lotte.reservatiesysteem.activity.LoginActivity;
import com.reservatiesysteem.lotte.reservatiesysteem.activity.StartActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.reservatiesysteem.lotte.reservatiesysteem.CustomViewAssertions.waitFor;
import static com.reservatiesysteem.lotte.reservatiesysteem.CustomViewAssertions.withListSize;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by Jasper on 13/03/2017.
 */

public class FavoriteAddRemoveTest {

    @Rule
    public ActivityTestRule<LoginActivity> mLoginActivityRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    @Before
    public void login(){
        onView(withId(R.id.txtUsername)).perform(replaceText("hello@leisurebooker.me"));
        onView(withId(R.id.txtPassword)).perform(replaceText("MySuperP@ssword!"));
        onView(withId(R.id.btnLogin)).perform(click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.reserveren)).perform(click());
    }

    @Test
    public void addRemoveFavoriteTest(){
        onView(withId(R.id.searchCity)).perform(typeText("wommelgem"),closeSoftKeyboard());
        onView(withId(R.id.numberPersons)).perform(typeText("2"),closeSoftKeyboard());
        onView(withId(R.id.btnReserveer)).perform(click());

        onView(isRoot()).perform(waitFor(1000));
        onView(withId (R.id.listBranches)).check (matches (withListSize (0)));
        onView(withText("Bowling Stones Wommelgem")).perform(click());

        onView(withId(R.id.btnFavorites)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withText("Favoriet succesvol toegevoegd")).inRoot(withDecorView(not(is(mLoginActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));


        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.myFavorites)).perform(click());

        onView(withText("Bowling Stones Wommelgem")).perform(click());
        onView(withId(R.id.btnFavorites)).perform(scrollTo()).perform(click());
    }
}
