package com.asalman.trellodoro.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.asalman.trellodoro.R;
import com.asalman.trellodoro.ui.fragments.BoardFragment;
import com.asalman.trellodoro.ui.fragments.ListsFragment;
import com.asalman.trellodoro.ui.fragments.OAuthFragment;

/**
 * Created by asalman on 1/3/16.
 */
public class ConfigWizardActivity extends AppCompatActivity {
    private MyPagerAdapter mAdapter;
    private ViewPager mPager;
    private TextView mPreviousButton;
    private TextView mNextButton;
    private TextView mNavigator;
    private int mCurrentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard_config);

        mCurrentItem = 0;

        mPager = (ViewPager) findViewById(R.id.activity_wizard_universal_pager);
        mPreviousButton = (TextView) findViewById(R.id.activity_wizard_universal_previous);
        mNextButton = (TextView) findViewById(R.id.activity_wizard_universal_next);
        mNavigator = (TextView) findViewById(R.id.activity_wizard_universal_possition);

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
                // TODO Auto-generated method stub
				/*if (mPager.getCurrentItem() != 0) {
					mPager.setCurrentItem(mPager.getCurrentItem() - 1);
				}
				setNavigator();*/
                Toast.makeText(ConfigWizardActivity.this, "Skip",
                        Toast.LENGTH_SHORT).show();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mPager.getCurrentItem() != (mPager.getAdapter().getCount() - 1)) {
                    mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                } else {
                    Toast.makeText(ConfigWizardActivity.this, "Finish",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                setNavigator();
            }
        });

        setResult(0);

    }

    public void setNavigator() {
        String navigation = "";
        for (int i = 0; i < mAdapter.getCount(); i++) {
            if (i == mPager.getCurrentItem()) {
                navigation += " .  ";
            } else {
                navigation +=   " o ";
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



    public class MyPagerAdapter extends FragmentPagerAdapter {

        OAuthFragment oAuthFragment = OAuthFragment.newInstance(0);
        BoardFragment boardFragment = BoardFragment.newInstance(1);
        ListsFragment listsFragment = ListsFragment.newInstance(2);

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