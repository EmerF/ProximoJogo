package br.com.proximojogo.proximojogo;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import br.com.proximojogo.proximojogo.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TesteEditarTime {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testeEditarTime() {
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(60000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatImageButton = onView(
allOf(withContentDescription("Open navigation drawer"),
childAtPosition(
allOf(withId(R.id.toolbar),
childAtPosition(
withClassName(is("android.support.design.widget.AppBarLayout")),
0)),
1),
isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
allOf(childAtPosition(
allOf(withId(R.id.design_navigation_view),
childAtPosition(
withId(R.id.nav_view),
0)),
2),
isDisplayed()));
        navigationMenuItemView.perform(click());

         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(60000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatImageButton2 = onView(
allOf(withContentDescription("Open navigation drawer"),
childAtPosition(
allOf(withId(R.id.toolbar),
childAtPosition(
withClassName(is("android.support.design.widget.AppBarLayout")),
0)),
1),
isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction navigationMenuItemView2 = onView(
allOf(childAtPosition(
allOf(withId(R.id.design_navigation_view),
childAtPosition(
withId(R.id.nav_view),
0)),
3),
isDisplayed()));
        navigationMenuItemView2.perform(click());

        ViewInteraction appCompatButton = onView(
allOf(withId(R.id.bt_listar_times), withText("Listar"),
childAtPosition(
allOf(withId(R.id.linear_botoes_agenda),
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
3)),
1),
isDisplayed()));
        appCompatButton.perform(click());

        DataInteraction linearLayout = onData(anything())
.inAdapterView(allOf(withId(R.id.list_view_times),
childAtPosition(
withId(R.id.activity_lista_atletas),
0)))
.atPosition(0);
        linearLayout.perform(click());

        ViewInteraction textInputEditText = onView(
allOf(withId(R.id.responsavel_time), withText("Teste"),
childAtPosition(
childAtPosition(
withId(R.id.lb_responsavel),
0),
0),
isDisplayed()));
        textInputEditText.perform(replaceText(""));

        ViewInteraction textInputEditText2 = onView(
allOf(withId(R.id.responsavel_time),
childAtPosition(
childAtPosition(
withId(R.id.lb_responsavel),
0),
0),
isDisplayed()));
        textInputEditText2.perform(closeSoftKeyboard());

        ViewInteraction textInputEditText3 = onView(
allOf(withId(R.id.responsavel_time),
childAtPosition(
childAtPosition(
withId(R.id.lb_responsavel),
0),
0),
isDisplayed()));
        textInputEditText3.perform(click());

        ViewInteraction textInputEditText4 = onView(
allOf(withId(R.id.responsavel_time),
childAtPosition(
childAtPosition(
withId(R.id.lb_responsavel),
0),
0),
isDisplayed()));
        textInputEditText4.perform(click());

        ViewInteraction textInputEditText5 = onView(
allOf(withId(R.id.responsavel_time),
childAtPosition(
childAtPosition(
withId(R.id.lb_responsavel),
0),
0),
isDisplayed()));
        textInputEditText5.perform(replaceText("Felipe"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
allOf(withId(R.id.bt_salvar_time), withText("Salvar"),
childAtPosition(
allOf(withId(R.id.linear_botoes_agenda),
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
3)),
0),
isDisplayed()));
        appCompatButton2.perform(click());

        }

        private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
