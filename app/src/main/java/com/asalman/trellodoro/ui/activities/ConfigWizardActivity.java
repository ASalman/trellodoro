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
    private MyPagerAdapter adapter;
    private ViewPager pager;
    private TextView previousButton;
    private TextView nextButton;
    private TextView navigator;
    private int currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard_config);

        currentItem = 0;

        pager = (ViewPager) findViewById(R.id.activity_wizard_universal_pager);
        previousButton = (TextView) findViewById(R.id.activity_wizard_universal_previous);
        nextButton = (TextView) findViewById(R.id.activity_wizard_universal_next);
        navigator = (TextView) findViewById(R.id.activity_wizard_universal_possition);

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setCurrentItem(currentItem);

        setNavigator();

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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

        previousButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
				/*if (pager.getCurrentItem() != 0) {
					pager.setCurrentItem(pager.getCurrentItem() - 1);
				}
				setNavigator();*/
                Toast.makeText(ConfigWizardActivity.this, "Skip",
                        Toast.LENGTH_SHORT).show();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (pager.getCurrentItem() != (pager.getAdapter().getCount() - 1)) {
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
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
        for (int i = 0; i < adapter.getCount(); i++) {
            if (i == pager.getCurrentItem()) {
                navigation += " .  ";
            } else {
                navigation +=   " o ";
            }
        }
        navigator.setText(navigation);
    }

    public void setCurrentSlidePosition(int position) {
        this.currentItem = position;
    }

    public int getCurrentSlidePosition() {
        return this.currentItem;
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