package com.tinmegali.testing;

import android.os.Bundle;

import com.tinmegali.testing.common.StateMaintainer;
import com.tinmegali.testing.main.activity.MVP_Main;
import com.tinmegali.testing.main.activity.model.MainModel;
import com.tinmegali.testing.main.activity.presenter.MainPresenter;
import com.tinmegali.testing.main.activity.view.MainActivity;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * ---------------------------------------------------
 * Created by Tin Megali on 06/04/16.
 * Project: testing
 * ---------------------------------------------------
 * <a href="http://www.tinmegali.com">tinmegali.com</a>
 * <a href="http://www.github.com/tinmegali>github</a>
 * ---------------------------------------------------
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/src/main/AndroidManifest.xml")
public class MainActivityTest {
    private MainActivity activity;
    private ActivityController<MainActivity> controller;
    private MVP_Main.ProvidedPresenterOps presenterOps;
    private StateMaintainer stateMaintainer;


    @Before
    public void before(){
        controller = Robolectric.buildActivity(MainActivity.class);
        presenterOps = Mockito.mock(MVP_Main.ProvidedPresenterOps.class);
        stateMaintainer = Mockito.mock(StateMaintainer.class);
    }

    // testing Activity onCreate
    @Test
    public void testOnCreate(){
        activity = controller.get();

        when(stateMaintainer.firstTimeIn())
                .thenReturn(true);
        activity.mStateMaintainer = stateMaintainer;

        controller.create();

        Mockito.verify(stateMaintainer, VerificationModeFactory.atLeast(2))
                .put(Mockito.any(MainPresenter.class));
        Mockito.verify(stateMaintainer, VerificationModeFactory.atLeast(2))
                .put(Mockito.any(MainModel.class));

        assertNotNull(activity.mPresenter);

    }

    // testing Activity onCreate during a reconfiguration
    @Test
    public void testOnCreateReconfig(){
        controller = Robolectric.buildActivity(MainActivity.class);
        activity = controller.get();

        when(stateMaintainer.firstTimeIn())
                .thenReturn(false);
        when(stateMaintainer.get(MainPresenter.class.getName()))
                .thenReturn(presenterOps);
        activity.mStateMaintainer = stateMaintainer;

        Bundle savedInstanceState = new Bundle();
        controller
                .create(savedInstanceState)
                .start()
                .restoreInstanceState(savedInstanceState)
                .postCreate(savedInstanceState)
                .resume()
                .visible();

        verify(stateMaintainer).get(MainPresenter.class.getName());
        verify(presenterOps).setView(any(MVP_Main.RequiredViewOps.class));

    }

    // testing Activity onDestroy
    @Test
    public void testOnDestroy(){
        controller.create();
        activity = controller.get();
        activity.mPresenter = presenterOps;

        controller.destroy();
        Mockito.verify(presenterOps).onDestroy(Mockito.anyBoolean());

    }
}
