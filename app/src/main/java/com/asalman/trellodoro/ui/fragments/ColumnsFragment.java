package com.asalman.trellodoro.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import com.asalman.trellodoro.R;




public class ColumnsFragment extends Fragment {

    private static final String ARG_POSITION = "Position";

    private int mPosition;
    private RelativeLayout mLayout;


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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wizard_lists,
                container, false);
        mLayout = (RelativeLayout) rootView
                .findViewById(R.id.fragment_wizard_lists);



        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

}