package com.tinmegali.testing;

import android.os.Bundle;

import com.tinmegali.testing.common.StateMaintainer;
import com.tinmegali.testing.main.activity.MVP_Main;
import com.tinmegali.testing.main.activity.view.MainActivity;

import org.mockito.Mockito;

/**
 * ---------------------------------------------------
 * Created by Tin Megali on 06/04/16.
 * Project: testing
 * ---------------------------------------------------
 * <a href="http://www.tinmegali.com">tinmegali.com</a>
 * <a href="http://www.github.com/tinmegali>github</a>
 * ---------------------------------------------------
 */
public class MainActivityTestVersion extends MainActivity {
    public final StateMaintainer mStateMaintainer =
            Mockito.mock(StateMaintainer.class);
    public MVP_Main.ProvidedPresenterOps mPresenter =
            Mockito.mock(MVP_Main.ProvidedPresenterOps.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
