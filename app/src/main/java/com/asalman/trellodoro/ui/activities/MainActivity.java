package com.asalman.trellodoro.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;


import com.asalman.trellodoro.R;
import com.asalman.trellodoro.bus.BusProvider;
import com.asalman.trellodoro.rest.Config;
import com.asalman.trellodoro.ui.fragments.TabTasksListFragment;
import com.astuetz.PagerSlidingTabStrip;
import com.squareup.otto.Bus;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    private MyPagerAdapter adapter;
    private Toolbar toolbar;
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private Bus mBus = BusProvider.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Trello Pomodoro App");
        setSupportActionBar(toolbar);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.activity_tab_universal_tabs);
        pager = (ViewPager) findViewById(R.id.activity_tab_universal_pager);

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        pager.setCurrentItem(0);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ConfigWizardActivity.class);
                startActivity(intent);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (Config.getDoingListID().equals("")){
            //Intent intent = new Intent(MainActivity.this, ConfigWizardActivity.class);
            //startActivityForResult(intent,0);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBus.unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        TabTasksListFragment todoFragment = TabTasksListFragment.newInstance(0),
                doningFragment = TabTasksListFragment.newInstance(1),
                doneFragment = TabTasksListFragment.newInstance(2);

        private final ArrayList<String> tabNames = new ArrayList<String>() {{
            add("To Do");
            add("Doing");
            add("Done");
        }};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabNames.get(position);
        }

        @Override
        public int getCount() {
            return tabNames.size();
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return todoFragment;
            } else if (position == 1) {
                return doningFragment;
            } else  {
                return doneFragment;
            }
        }
    }
}