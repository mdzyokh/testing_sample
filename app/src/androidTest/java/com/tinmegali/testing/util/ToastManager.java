package com.tinmegali.testing.util;

import android.content.Context;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.CountingIdlingResource;
import android.view.View;
import android.widget.Toast;

import android.support.test.espresso.Espresso;

/**
 * ---------------------------------------------------
 * Created by Tin Megali on 06/04/16.
 * Project: testing
 * ---------------------------------------------------
 * <a href="http://www.tinmegali.com">tinmegali.com</a>
 * <a href="http://www.github.com/tinmegali>github</a>
 *
 * based on <a href="http://stackoverflow.com/a/32023568/4871489">stackoverflow answer</a>
 * ---------------------------------------------------
 */
public final class ToastManager {
    private static final CountingIdlingResource idlingResource = new CountingIdlingResource("toast");
    private static final View.OnAttachStateChangeListener listener = new View.OnAttachStateChangeListener() {
        @Override
        public void onViewAttachedToWindow(final View v) {
            idlingResource.increment();
        }

        @Override
        public void onViewDetachedFromWindow(final View v) {
            idlingResource.decrement();
        }
    };

    private ToastManager() { }

    public static Toast makeText(final Context context, final CharSequence text, final int duration) {
        Toast t = Toast.makeText(context, text, duration);
        t.getView().addOnAttachStateChangeListener(listener);
        return t;
    }

    // For testing
    public static IdlingResource getIdlingResource() {
        return idlingResource;
    }
}
