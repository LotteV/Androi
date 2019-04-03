package com.reservatiesysteem.lotte.reservatiesysteem;

import android.support.test.rule.ActivityTestRule;

import com.reservatiesysteem.lotte.reservatiesysteem.activity.LoginActivity;

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
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.reservatiesysteem.lotte.reservatiesysteem.CustomViewAssertions.setTextInTextView;
import static com.reservatiesysteem.lotte.reservatiesysteem.CustomViewAssertions.waitFor;
import static com.reservatiesysteem.lotte.reservatiesysteem.CustomViewAssertions.withListSize;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by Jasper on 07/03/2017.
 */

public class FavoritesReservationTest {

    @Rule
    public ActivityTestRule<LoginActivity> mLoginActivityRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);


    @Before
    public void addfavorite(){
        onView(withId(R.id.txtUsername)).perform(replaceText("hello@leisurebooker.me"));
        onView(withId(R.id.txtPassword)).perform(replaceText("MySuperP@ssword!"));
        onView(withId(R.id.btnLogin)).perform(click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.reserveren)).perform(click());
        onView(withId(R.id.searchCity)).perform(typeText("9100"),closeSoftKeyboard());
        onView(withId(R.id.numberPersons)).perform(typeText("2"),closeSoftKeyboard());
        onView(withId(R.id.btnReserveer)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId (R.id.listBranches)).check (matches (withListSize (0)));
        onView(withText("Wima Sint-Niklaas")).perform(click());
        onView(withId(R.id.btnFavorites)).perform(scrollTo()).perform(click());

    }
    @Test
    public void testFavoriteReservation() {


        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.myFavorites)).perform(click());
        onView(isRoot()).perform(waitFor(1000));

        onView (withId (R.id.listBranches)).check (matches (withListSize (0)));

        onData(anything()).inAdapterView(allOf(withId(R.id.listBranches), isCompletelyDisplayed())).atPosition(0).perform(click());
        onView(withId(R.id.btnReserveren)).perform(scrollTo()).perform(click());

        onView(withId(R.id.txtTime)).perform(setTextInTextView("18:00"));
        onView(withId(R.id.txtDate)).perform(setTextInTextView("2018-05-02"));
        onView(withId(R.id.numberPersons)).perform(typeText("2"));
        onView(withId(R.id.btnReserveer)).perform(click());

        onView(isRoot()).perform(waitFor(1000));
        onView(withText("Reservatie gelukt")).inRoot(withDecorView(not(is(mLoginActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

        //cancel reservation
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.myProfile)).perform(click());
        onView(withId(R.id.btnCheckRes)).perform(scrollTo()).perform(click());

        onView(isRoot()).perform(waitFor(2000));
        onView(withText("2018-05-02 om 18:00")).perform(click());
        onView(withId(R.id.btnCancel)).perform(click());
    }
}
