package com.asalman.trellodoro.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;


import com.asalman.trellodoro.R;
import com.asalman.trellodoro.application.MyApplication;
import com.asalman.trellodoro.bus.BusProvider;
import com.asalman.trellodoro.events.api.LoadCardsEvent;
import com.asalman.trellodoro.preferences.Config;
import com.asalman.trellodoro.ui.fragments.TabTasksListFragment;
import com.asalman.trellodoro.utils.Analytics;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.squareup.otto.Bus;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    private final static String TAG = MainActivity.class.getName();
    private MyPagerAdapter adapter;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Bus mBus = BusProvider.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Trello Pomodoro");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_trellodoro);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);


        adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);
        viewPager.setCurrentItem(0);

        if (Config.getDoingListID().equals("")){
            Intent intent = new Intent(MainActivity.this, ConfigWizardActivity.class);
            startActivityForResult(intent,0);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // @// TODO: this must be done on more proper way now I just want to go to sleep
        mBus.post(new LoadCardsEvent(Config.getTodoListID()));
        mBus.post(new LoadCardsEvent(Config.getDoingListID()));
        mBus.post(new LoadCardsEvent(Config.getDoingListID()));
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getAnalytics().sendScreenView(TAG);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        updateNotification(menu.findItem(R.id.action_notification));
        menu.findItem(R.id.action_settings).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_cog)
                        .colorRes(R.color.icons)
                        .actionBarSize());
        return true;
    }

    private void updateNotification(MenuItem item){
        if (item == null)
            return;
        MyApplication.getAnalytics().sendEvent(Analytics.AppCategories.CLICKS,
                getResources().getResourceEntryName(item.getItemId()),
                item.getTitle().toString(),
                Config.isNotificationEnabled() ? 0 : 1);
        item.setIcon(
                new IconDrawable(this, Config.isNotificationEnabled() ? FontAwesomeIcons.fa_bell_o :
                        FontAwesomeIcons.fa_bell_slash_o)
                        .colorRes(R.color.icons)
                        .actionBarSize());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, ConfigWizardActivity.class);
            startActivity(intent);
            MyApplication.getAnalytics().sendEvent(Analytics.AppCategories.CLICKS,
                    getResources().getResourceEntryName(id),
                    item.getTitle().toString(),
                    id);
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //        .setAction("Action", null).show();
        } else if (id == R.id.action_notification) {
            Config.setNotificationEnabled(!Config.isNotificationEnabled());
            updateNotification(item);
        }

        return super.onOptionsItemSelected(item);
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