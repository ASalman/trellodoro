package com.asalman.trellodoro.ui.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.asalman.trellodoro.R;
import com.asalman.trellodoro.bus.BusProvider;
import com.asalman.trellodoro.events.api.CardsLoadedEvent;
import com.asalman.trellodoro.events.api.LoadCardsEvent;
import com.asalman.trellodoro.models.Card;
import com.asalman.trellodoro.preferences.Config;
import com.asalman.trellodoro.ui.adapters.TaskListAdapter;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;


/**
 * Created by asalman on 01/10/16.
 */
public class TabTasksListFragment extends Fragment {

    private static final String ARG_POSITION = "position";


    private int position;
    private TaskListAdapter taskListAdapter;
    private Bus bus = BusProvider.getInstance();
    private ArrayList<Card> cardsList = new ArrayList<>();
    private String listID = "";


    public static TabTasksListFragment newInstance(int position) {
        TabTasksListFragment f = new TabTasksListFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Subscribe
    public void onCardsLoaded(CardsLoadedEvent event) {
        if (!listID.equals(event.getColumnID())){
            return;
        }
        cardsList.clear();
        cardsList.addAll(event.getCards());
        taskListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
        bus.post(new LoadCardsEvent(listID));
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_tasks_list,
                container, false);
        ViewCompat.setElevation(rootView, 50);

        ListView listView = (ListView) rootView.findViewById(R.id.list_view);

        taskListAdapter = new TaskListAdapter(this.getActivity(), cardsList);


        listView.setClipToPadding(false);
        listView.setDivider(null);
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                8, r.getDisplayMetrics());
        listView.setDividerHeight(px);
        listView.setFadingEdgeLength(0);
        listView.setFitsSystemWindows(true);
        px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                r.getDisplayMetrics());
        listView.setPadding(px, px, px, px);
        listView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
        listView.setAdapter(taskListAdapter);

        if (position == 0){
            listID = Config.getTodoListID();
        } else if (position == 1) {
            listID = Config.getDoingListID();
        } else if (position == 2) {
            listID = Config.getDoneListID();
        }

        return rootView;
    }

}