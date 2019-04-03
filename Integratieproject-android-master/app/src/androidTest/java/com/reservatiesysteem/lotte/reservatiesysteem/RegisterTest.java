package com.reservatiesysteem.lotte.reservatiesysteem;

import android.graphics.Color;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.reservatiesysteem.lotte.reservatiesysteem.activity.LoginActivity;
import com.reservatiesysteem.lotte.reservatiesysteem.activity.RegisterActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Jasper on 24/02/2017.
 */

@RunWith(AndroidJUnit4.class)
public class RegisterTest {
    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule = new ActivityTestRule<RegisterActivity>(RegisterActivity.class);

    @Test
    public void register(){
        onView(withId(R.id.btnRegister)).perform(click());
        onView(withId(R.id.lblError)).check(matches(withText("Voornaam mag niet leeg zijn")));

        onView(withId(R.id.txtFirstName)).perform(typeText("Jasper"), closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());
        onView(withId(R.id.lblError)).check(matches(withText("Achternaam mag niet leeg zijn")));

        onView(withId(R.id.txtLastName)).perform(typeText("Van Grieken"), closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());
        onView(withId(R.id.lblError)).check(matches(withText("E-mail incorrect of leeg")));


        onView(withId(R.id.txtPhoneNumber)).perform(replaceText("0473228041"),closeSoftKeyboard());

        onView(withId(R.id.txtEmail)).perform(typeText("jasper.vangrieken@student.kdg.be"), closeSoftKeyboard());
        onView(withId(R.id.txtPassword)).perform(typeText("Test"),closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());
        onView(withId(R.id.lblError)).check(matches(withText("passwoord moet minstens 6 tekens lang zijn")));

        onView(withId(R.id.txtPassword)).perform(replaceText("Testing"),closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());
        onView(withId(R.id.lblError)).check(matches(withText("passwoord moet minstens 1 getal bevatten")));



        onView(withId(R.id.txtPassword)).perform(replaceText("Test123"),closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());
        onView(withId(R.id.lblError)).check(matches(withText("passwoord moet 1 van volgende tekens bevatten !@#$%^&*_")));

        onView(withId(R.id.txtPassword)).perform(replaceText("Test@124"),closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());
        onView(withId(R.id.lblError)).check(matches(withText("Wachtwoord en Herhaal wachtwoord moeten gelijk zijn")));
    }
}
