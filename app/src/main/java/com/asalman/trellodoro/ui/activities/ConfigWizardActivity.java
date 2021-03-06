package com.asalman.trellodoro.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asalman.trellodoro.R;
import com.asalman.trellodoro.application.MyApplication;
import com.asalman.trellodoro.bus.BusProvider;
import com.asalman.trellodoro.events.WizardPageFinishedEvent;
import com.asalman.trellodoro.preferences.Config;
import com.asalman.trellodoro.ui.fragments.BoardFragment;
import com.asalman.trellodoro.ui.fragments.ColumnsFragment;
import com.asalman.trellodoro.ui.fragments.OAuthFragment;
import com.asalman.trellodoro.utils.Analytics;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * Created by asalman on 1/3/16.
 */
public class ConfigWizardActivity extends AppCompatActivity {

    private final static String TAG = ConfigWizardActivity.class.getName();
    private MyPagerAdapter mAdapter;
    private ViewPager mPager;
    private Button mPreviousButton, mNextButton;
    private TextView mNavigator;
    private int mCurrentItem;
    private Bus bus = BusProvider.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard_config);

        mCurrentItem = 0;

        mPager = (ViewPager) findViewById(R.id.activity_wizard_universal_pager);
        mPreviousButton = (Button) findViewById(R.id.btn_back);
        mNextButton = (Button) findViewById(R.id.btn_next);
        mNavigator = (TextView) findViewById(R.id.lbl_position);

        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(mCurrentItem);

        setNavigator();

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int position) {
                // TODO Auto-generated method stub
                setNavigator();
            }
        });

        mPreviousButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MyApplication.getAnalytics().sendEvent(Analytics.AppCategories.CLICKS,
                        getResources().getResourceEntryName(v.getId()),
                        getResources().getResourceEntryName(v.getId()));
                if (mPager.getCurrentItem() != 0) {
                    mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                }
                setNavigator();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MyApplication.getAnalytics().sendEvent(Analytics.AppCategories.CLICKS,
                        getResources().getResourceEntryName(v.getId()),
                        getResources().getResourceEntryName(v.getId()));
                if (mPager.getCurrentItem() != (mPager.getAdapter().getCount() - 1)) {
                    mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                } else {
                    setResult(1);
                    ((ColumnsFragment)mAdapter.getItem(2)).finish();
                    finish();
                }
                setNavigator();
            }
        });

        setResult(0);

    }

    public void setNavigator() {
        String navigation = "";
        mPreviousButton.setVisibility(View.INVISIBLE);
        if (mPager.getCurrentItem() != 0) {
            mPreviousButton.setVisibility(View.VISIBLE);
        }
        if (mPager.getCurrentItem() == mAdapter.getCount() -1) {
            mNextButton.setText("{typcn-tick}");
        } else {
            mNextButton.setText("{typcn-chevron-right-outline}");
        }

        for (int i = 0; i < mAdapter.getCount(); i++) {
            if (i == mPager.getCurrentItem()) {
                navigation += " {typcn-media-record} ";
            } else {
                navigation +=   " {typcn-media-record-outline} ";
            }
        }
        mNavigator.setText(navigation);
    }

    public void setCurrentSlidePosition(int position) {
        this.mCurrentItem = position;
    }

    public int getCurrentSlidePosition() {
        return this.mCurrentItem;
    }

    @Subscribe
    public void onPageFinished(WizardPageFinishedEvent onPageFinishedEvent){
        if (onPageFinishedEvent.getPosition() == 0){
            mPager.setCurrentItem(1);
        } else if (onPageFinishedEvent.getPosition() == 1) {
            mPager.setCurrentItem(2);
        }
        setNavigator();
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getAnalytics().sendScreenView(TAG);
        bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {

        OAuthFragment oAuthFragment = OAuthFragment.newInstance(0);
        BoardFragment boardFragment = BoardFragment.newInstance(1);
        ColumnsFragment listsFragment = ColumnsFragment.newInstance(2);

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return oAuthFragment;
            } else if (position == 1) {
                return boardFragment;
            } else {
                return listsFragment;
            }
        }
    }
}