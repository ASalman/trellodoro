package com.asalman.trellodoro.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;


import com.asalman.trellodoro.R;
import com.asalman.trellodoro.application.MyApplication;
import com.asalman.trellodoro.bus.BusProvider;
import com.asalman.trellodoro.events.WizardPageFinishedEvent;
import com.asalman.trellodoro.events.api.BoardsLoadedEvent;
import com.asalman.trellodoro.events.api.LoadBoardEvent;
import com.asalman.trellodoro.models.Board;
import com.asalman.trellodoro.preferences.Config;
import com.asalman.trellodoro.ui.widgets.NothingSelectedSpinnerAdapter;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;


public class BoardFragment extends Fragment {

    private final static String TAG = BoardFragment.class.getName();
    private static final String ARG_POSITION = "Position";

    private int mPosition;
    private RelativeLayout mLayout;
    private ArrayList<Board> boardsList = new ArrayList<>();
    private ArrayAdapter<Board> boardsAdapter;
    private Spinner spinner;
    private Bus mBus = BusProvider.getInstance();

    public static BoardFragment newInstance(int mPosition) {
        BoardFragment f = new BoardFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, mPosition);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);
    }

    @Subscribe
    public void onBoardsLoaded(BoardsLoadedEvent event) {
        boardsList.clear();
        boardsList.addAll(event.getBoards());
        boardsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getAnalytics().sendScreenView(TAG);
        mBus.register(this);
        mBus.post(new LoadBoardEvent());
    }

    @Override
    public void onPause() {
        super.onPause();
        mBus.unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wizard_board,
                container, false);
        mLayout = (RelativeLayout) rootView
                .findViewById(R.id.fragment_wizard_board);
        spinner = (Spinner) rootView.findViewById(R.id.spinnerstate);
        boardsAdapter = new ArrayAdapter<>(this.getActivity(),  R.layout.spinner_item, boardsList);
        boardsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setPrompt("Please select");
        spinner.setAdapter(new NothingSelectedSpinnerAdapter(boardsAdapter, R.layout.spinner_item_noselect,
                this.getActivity()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int spinnerPosition, long id) {
                if (spinner.getSelectedItem() != null) {
                    Config.setDoardID(((Board) spinner.getSelectedItem()).getId());
                    mBus.post(new WizardPageFinishedEvent(mPosition, BoardFragment.this));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

}