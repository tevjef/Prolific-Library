package com.tevinjeffrey.prolificlibrary.ui;


import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.tevinjeffrey.prolificlibrary.FakeAppModule;
import com.tevinjeffrey.prolificlibrary.LibraryApplication;
import com.tevinjeffrey.prolificlibrary.R;
import com.tevinjeffrey.prolificlibrary.dagger.DaggerAppComponent;
import com.tevinjeffrey.prolificlibrary.data.RetroLibrary;
import com.tevinjeffrey.prolificlibrary.data.model.Book;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.Mockito.when;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BooksActivityTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ActivityTestRule<BooksActivity> mActivityTestRule = new ActivityTestRule<>(BooksActivity.class, true, false);

    @Mock
    RetroLibrary retroLibrary;

    @Before
    public void setUp() throws Exception {
        LibraryApplication application = (LibraryApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        application.setComponents(DaggerAppComponent.builder().appModule(new FakeAppModule(retroLibrary)).build());
    }

    @Test
    public void checkoutTest() {
        // Use fake data set
        when(retroLibrary.getBooks()).thenReturn(Observable.just(getFakeBooks()));

        // Manually launch activity
        mActivityTestRule.launchActivity(new Intent());

        onView(withId(R.id.books_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        ViewInteraction floatingActionButton = onView(allOf(withId(R.id.fab_checkout), isDisplayed()));

        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(allOf(withId(android.R.id.input), isDisplayed()));

        appCompatEditText.perform(replaceText("Chet"), closeSoftKeyboard());

        ViewInteraction mDButton = onView(
                allOf(withId(R.id.buttonDefaultPositive), withText("OK"), isDisplayed()));
        mDButton.perform(click());

    }

    public List<Book> getFakeBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book.Builder().id(1).author("John Grisham").title("The Whistler").build());
        books.add(new Book.Builder().id(2).author("Nicholas Sparks").title("Two by Two").build());
        books.add(new Book.Builder().id(3).author("Jodi Picoult").title("Small Great Things").build());
        books.add(new Book.Builder().id(4).author("John Sandford").title("Escape Clause").build());
        books.add(new Book.Builder().id(5).author("Vince Flynn").title("Order to Kill").build());
        books.add(new Book.Builder().id(6).author("The Girl on the Train").title("Paula Hawkins").build());
        books.add(new Book.Builder().id(7).author("Le Guin").title("The Lathe of Heaven").build());

        return books;
    }
}
