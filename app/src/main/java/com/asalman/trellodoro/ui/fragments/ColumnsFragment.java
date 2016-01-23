package com.asalman.trellodoro.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;


import com.asalman.trellodoro.R;
import com.asalman.trellodoro.application.MyApplication;
import com.asalman.trellodoro.bus.BusProvider;
import com.asalman.trellodoro.events.api.ColumnsLoadedEvent;
import com.asalman.trellodoro.events.api.LoadColumnsEvent;
import com.asalman.trellodoro.models.Column;
import com.asalman.trellodoro.preferences.Config;
import com.asalman.trellodoro.ui.widgets.NothingSelectedSpinnerAdapter;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;


public class ColumnsFragment extends Fragment {

    private final static String TAG = BoardFragment.class.getName();
    private static final String ARG_POSITION = "Position";

    private int mPosition;
    private RelativeLayout mLayout;
    private ArrayList<Column> mColumnsList = new ArrayList<>();
    private ArrayAdapter<Column> todoAdapter,doingAdapter,doneAdapter;
    private Spinner spnrTodo,spnrDoing,spnrDone;
    private Bus mBus = BusProvider.getInstance();


    public static ColumnsFragment newInstance(int position) {
        ColumnsFragment f = new ColumnsFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);
    }

    public void populateLists(){

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


    @Subscribe
    public void onListsLoaded(ColumnsLoadedEvent event) {
        mColumnsList.clear();
        mColumnsList.addAll(event.getColumns());
        todoAdapter.notifyDataSetChanged();
        doneAdapter.notifyDataSetChanged();
        doingAdapter.notifyDataSetChanged();
    }

    public boolean finish(){
        if (spnrTodo.getSelectedItem() != null) {
            Config.setTodoListID(((Column)spnrTodo.getSelectedItem()).getId());
        }
        if (spnrDoing.getSelectedItem() != null) {
            Config.setDoingListID(((Column) spnrDoing.getSelectedItem()).getId());
        }
        if (spnrDone.getSelectedItem() != null) {
            Config.setDoneListID(((Column) spnrDone.getSelectedItem()).getId());
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wizard_lists,
                container, false);
        mLayout = (RelativeLayout) rootView
                .findViewById(R.id.fragment_wizard_lists);
        spnrTodo = (Spinner) rootView.findViewById(R.id.spinnertodo);
        spnrDoing = (Spinner) rootView.findViewById(R.id.spinnerdoing);
        spnrDone = (Spinner) rootView.findViewById(R.id.spinnerdone);

        //boardsAdapter = new ArrayAdapter<>(this.getActivity(),  android.R.layout.simple_spinner_item, boardsList);
        //boardsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        todoAdapter = new ArrayAdapter<>(this.getActivity(),  R.layout.spinner_item, mColumnsList);
        doingAdapter = new ArrayAdapter<>(this.getActivity(),  R.layout.spinner_item, mColumnsList);
        doneAdapter = new ArrayAdapter<>(this.getActivity(),  R.layout.spinner_item, mColumnsList);

        todoAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        doingAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        doneAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spnrTodo.setPrompt("Please select");
        spnrDoing.setPrompt("Please select");
        spnrDone.setPrompt("Please select");

        spnrTodo.setAdapter(new NothingSelectedSpinnerAdapter(todoAdapter, R.layout.spinner_item_noselect,
                this.getActivity()));
        spnrDoing.setAdapter(new NothingSelectedSpinnerAdapter(doingAdapter, R.layout.spinner_item_noselect,
                this.getActivity()));
        spnrDone.setAdapter(new NothingSelectedSpinnerAdapter(doneAdapter, R.layout.spinner_item_noselect,
                this.getActivity()));


        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

}