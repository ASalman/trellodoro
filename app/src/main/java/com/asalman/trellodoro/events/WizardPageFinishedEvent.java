package com.asalman.trellodoro.events;

import android.support.v4.app.Fragment;

/**
 * Created by asalman on 1/9/16.
 */
public class WizardPageFinishedEvent {

    Fragment mFragment;
    int mPosition;


    public WizardPageFinishedEvent(int position, Fragment fragment){
        this.mFragment = fragment;
        this.mPosition = position;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public int getPosition() {
        return mPosition;
    }
}
