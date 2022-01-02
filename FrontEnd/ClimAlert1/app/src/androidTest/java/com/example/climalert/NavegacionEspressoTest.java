package com.example.climalert;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class NavegacionEspressoTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void botonesNavegacionTest() {
        onView(withId(R.id.navigation_call)).perform(click());
        onView(withId(R.id.navigation_info)).perform(click());
        onView(withId(R.id.navigation_settings)).perform(click());
        onView(withId(R.id.navigation_home)).perform(click());
    }

    @Test
    public void navegacionInfoTest() {
        onView(withId(R.id.navigation_info)).perform(click());
        onView(withId(R.id.button_calor_extremo)).perform(click());
        onView(isRoot()).perform(pressBack());
        onView(withId(R.id.button_granizo)).perform(click());
        onView(isRoot()).perform(pressBack());
        onView(withId(R.id.button_tormenta_invernal)).perform(click());
        onView(isRoot()).perform(pressBack());
        onView(withId(R.id.button_tornado)).perform(click());
        onView(isRoot()).perform(pressBack());
        onView(withId(R.id.button_inundacion)).perform(click());
        onView(isRoot()).perform(pressBack());
        onView(withId(R.id.button_incendio_forestal)).perform(click());
        onView(isRoot()).perform(pressBack());
        onView(withId(R.id.button_terremoto)).perform(click());
        onView(isRoot()).perform(pressBack());
        onView(withId(R.id.button_tsunami)).perform(click());
        onView(isRoot()).perform(pressBack());
        onView(withId(R.id.button_avalancha)).perform(click());
        onView(isRoot()).perform(pressBack());
        onView(withId(R.id.button_lluvia_acida)).perform(click());
        onView(isRoot()).perform(pressBack());
        onView(withId(R.id.button_erupcion_volcanica)).perform(click());
        onView(isRoot()).perform(pressBack());
        onView(withId(R.id.button_gota_fria)).perform(click());
        onView(isRoot()).perform(pressBack());
        //onView(withId(R.id.button_tormenta_electrica)).perform(scrollTo(), click());
    }

    @Test
    public void navegacionAjustesTest() {
        onView(withId(R.id.navigation_settings)).perform(click());
        onView(withId(R.id.perfil_usuario)).perform(click());
        onView(isRoot()).perform(pressBack());
        onView(withId(R.id.idioma)).perform(click());
    }
}