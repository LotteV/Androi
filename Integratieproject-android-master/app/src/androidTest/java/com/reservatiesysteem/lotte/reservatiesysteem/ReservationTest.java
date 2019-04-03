package com.reservatiesysteem.lotte.reservatiesysteem;


import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;


import com.reservatiesysteem.lotte.reservatiesysteem.activity.StartActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static com.reservatiesysteem.lotte.reservatiesysteem.CustomViewAssertions.clickLastListItem;
import static com.reservatiesysteem.lotte.reservatiesysteem.CustomViewAssertions.setTextInTextView;
import static com.reservatiesysteem.lotte.reservatiesysteem.CustomViewAssertions.waitFor;

import static com.reservatiesysteem.lotte.reservatiesysteem.CustomViewAssertions.withListSize;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ReservationTest {
    @Rule
    public ActivityTestRule<StartActivity>mActivityRule = new ActivityTestRule<StartActivity>(StartActivity.class);

    @Test
    public void reservationTest() throws Exception {
        //SearchFragment tests
        onView(withId(R.id.searchCity)).perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.numberPersons)).perform(typeText("azerty2"),closeSoftKeyboard());
        onView(withId(R.id.txtTime)).perform(setTextInTextView("21:00"));
        onView(withId(R.id.txtDate)).perform(setTextInTextView("2018-05-02"));
        onView(withId(R.id.btnReserveer)).perform(click());
        onView(withId(R.id.lblError)).check(matches(withText("Gemeente incorrect, gelieve 1 uit de lijst te kiezen")));
        onView(withId(R.id.searchCity)).perform(replaceText("wommelgem"));
        onView(withId(R.id.btnReserveer)).perform(click());

        //ListFragment tests
        onView(isRoot()).perform(waitFor(2000));

        onView(withId(R.id.lytList)).check(matches(isEnabled()));
        onView(withId(R.id.lytSearch)).check(matches(isEnabled()));
        onView(withId(R.id.lytDetails)).check(matches(not(isEnabled())));

        onView (withId (R.id.listBranches)).check (matches (withListSize (0)));

        onData(anything())
                .inAdapterView(allOf(withId(R.id.listBranches), isCompletelyDisplayed()))
                .atPosition(0).perform(click());
        onView(withId(R.id.lytDetails)).check(matches(isEnabled()));

        //DetailsFragment tests
        onView(withId(R.id.btnReserveren)).perform(scrollTo()).check(matches(isEnabled()));
        onView(isRoot()).perform(waitFor(1000));

        //Review tests
        //onView (withId (R.id.lvReview)).check (matches (withListSize (0)));

        onView(withId(R.id.viewFoto)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.btnFotos)).perform(scrollTo()).perform(click());
        onView(withId(R.id.viewFoto)).check(matches(not(isDisplayed())));

        onView(withId(R.id.btnReserveren)).perform(scrollTo()).perform(click());

        onView(isRoot()).perform(waitFor(1000));

        onView(withId(R.id.dateRes)).check(matches(withText("2018-05-02")));
        onView(withId(R.id.timeRes)).check(matches(withText("21:00")));
        onView(withId(R.id.numberRes)).check(matches(withText("2 personen")));
        onView(withId(R.id.btnConfirmRes)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withText("Reservatie gelukt")).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

        //cancel reservation
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.myProfile)).perform(click());
        onView(withId(R.id.btnCheckRes)).perform(scrollTo()).perform(click());


        onView(isRoot()).perform(waitFor(1000));
        onView(withText("2018-05-02 om 21:00")).perform(click());

        onView(withId(R.id.btnCancel)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withText("Reservatie succesvol geannuleerd")).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));


        //

    }


}
