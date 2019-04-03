package com.reservatiesysteem.lotte.reservatiesysteem;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.reservatiesysteem.lotte.reservatiesysteem.activity.LoginActivity;
import com.reservatiesysteem.lotte.reservatiesysteem.activity.RegisterActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.reservatiesysteem.lotte.reservatiesysteem.CustomViewAssertions.waitFor;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by Jasper on 07/03/2017.
 */

@RunWith(AndroidJUnit4.class)
public class LoginTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    @Test
    public void login(){
        //logintest
        onView(withId(R.id.txtUsername)).perform(typeText("fout"));
        onView(withId(R.id.txtPassword)).perform(typeText("fout"));
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withId(R.id.lblError)).check(matches(withText("Foutieve gebruikersnaam of passwoord")));

        onView(withId(R.id.txtUsername)).perform(replaceText("hello@leisurebooker.me"));
        onView(withId(R.id.txtPassword)).perform(replaceText("MySuperP@ssword!"));
        onView(withId(R.id.btnLogin)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withText("Login succesvol")).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

        //profiletests

        onView(withId(R.id.editFirstname)).check(matches(withText("leisure")));
        onView(withId(R.id.editSurname)).check(matches(withText("booker")));
        onView(withId(R.id.editEmail)).check(matches(withText("hello@leisurebooker.me")));

        //test save profile
        onView(withId(R.id.editFirstname)).perform(replaceText("Leisur"));
        onView(withId(R.id.editSurname)).perform(replaceText("booke"));
        onView(withId(R.id.btnSaveProfile)).perform(click());
        onView(isRoot()).perform(waitFor(3000));
        onView(withText("Gegevens zijn succesvol opgeslagen")).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

        //reset username to previous state
        onView(withId(R.id.editFirstname)).perform(replaceText("leisure"));
        onView(withId(R.id.editSurname)).perform(replaceText("booker"));
        onView(withId(R.id.btnSaveProfile)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withText("Gegevens zijn succesvol opgeslagen")).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

        //change password test
        onView(withId(R.id.btnChangePassword)).perform(click());

        onView(withHint("Old Password")).perform(typeText("MySuperP@ssword!"));
        onView(withHint("New Password")).perform(typeText("MySuperP@ssword!"));
        onView(withHint("Confirm Password")).perform(typeText("MySuperP@ssword!"));

        onView(withId(android.R.id.button1)).perform(click());
        onView(isRoot()).perform(waitFor(2000));
        onView(withText("Wachtwoord succesvol gewijzigd")).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }
}
