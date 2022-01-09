package com.example.climalert;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.PreferenceMatchers.withTitle;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TraduccionConsejosEspressoTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void consejosCatalaTest() {
        onView(withId(R.id.navigation_settings)).perform(click());
        onView(withId(R.id.idioma)).perform(click());
        onView(withId(R.id.catala_button)).perform(click());
        onView(withId(R.id.navigation_info)).perform(click());
        onView(allOf(withId(R.id.button_calor_extremo), withText("Calor extrema")));
        onView(allOf(withId(R.id.button_granizo), withText("Calamarsa")));
        onView(allOf(withId(R.id.button_tormenta_invernal), withText("Tempesta hivernal")));
        onView(allOf(withId(R.id.button_tornado), withText("Tornado")));
        onView(allOf(withId(R.id.button_inundacion), withText("Inundació")));
        onView(allOf(withId(R.id.button_incendio_forestal), withText("Incendi forestal")));
        onView(allOf(withId(R.id.button_terremoto), withText("Terratrèmol")));
        onView(allOf(withId(R.id.button_tsunami), withText("Tsunami")));
        onView(allOf(withId(R.id.button_avalancha), withText("Allau")));
        onView(allOf(withId(R.id.button_lluvia_acida), withText("Pluja àcida")));
        onView(allOf(withId(R.id.button_erupcion_volcanica), withText("Erupció volcànica")));
        onView(allOf(withId(R.id.button_gota_fria), withText("Gota freda")));
        //onView(allOf(withId(R.id.button_tormenta_electrica), withText("Tempesta elèctrica")));
    }

    @Test
    public void consejosCastellanoTest() {
        onView(withId(R.id.navigation_settings)).perform(click());
        onView(withId(R.id.idioma)).perform(click());
        onView(withId(R.id.castellano_button)).perform(click());
        onView(withId(R.id.navigation_info)).perform(click());
        onView(allOf(withId(R.id.button_calor_extremo), withText("Calor extremo")));
        onView(allOf(withId(R.id.button_granizo), withText("Granizo")));
        onView(allOf(withId(R.id.button_tormenta_invernal), withText("Tormenta invernal")));
        onView(allOf(withId(R.id.button_tornado), withText("Tornado")));
        onView(allOf(withId(R.id.button_inundacion), withText("Inundación")));
        onView(allOf(withId(R.id.button_incendio_forestal), withText("Incendio forestal")));
        onView(allOf(withId(R.id.button_terremoto), withText("Terremoto")));
        onView(allOf(withId(R.id.button_tsunami), withText("Tsunami")));
        onView(allOf(withId(R.id.button_avalancha), withText("Avalancha")));
        onView(allOf(withId(R.id.button_lluvia_acida), withText("Lluvia ácida")));
        onView(allOf(withId(R.id.button_erupcion_volcanica), withText("Erupción volcánica")));
        onView(allOf(withId(R.id.button_gota_fria), withText("Gota fría")));
        //onView(allOf(withId(R.id.button_tormenta_electrica), withText("Tormenta eléctrica")));
    }

    @Test
    public void consejosEnglishTest() {
        onView(withId(R.id.navigation_settings)).perform(click());
        onView(withId(R.id.idioma)).perform(click());
        onView(withId(R.id.english_button)).perform(click());
        onView(withId(R.id.navigation_info)).perform(click());
        onView(allOf(withId(R.id.button_calor_extremo), withText("Extreme heat")));
        onView(allOf(withId(R.id.button_granizo), withText("Hail")));
        onView(allOf(withId(R.id.button_tormenta_invernal), withText("Winter storm")));
        onView(allOf(withId(R.id.button_tornado), withText("Tornado")));
        onView(allOf(withId(R.id.button_inundacion), withText("Flood")));
        onView(allOf(withId(R.id.button_incendio_forestal), withText("Wildfire")));
        onView(allOf(withId(R.id.button_terremoto), withText("Earthquake")));
        onView(allOf(withId(R.id.button_tsunami), withText("Tsunami")));
        onView(allOf(withId(R.id.button_avalancha), withText("Avalanche")));
        onView(allOf(withId(R.id.button_lluvia_acida), withText("Acid rain")));
        onView(allOf(withId(R.id.button_erupcion_volcanica), withText("Vulcanic eruption")));
        onView(allOf(withId(R.id.button_gota_fria), withText("Cold drop")));
        //onView(allOf(withId(R.id.button_tormenta_electrica), withText("Thunder storm")));
    }
}