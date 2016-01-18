package com.asalman.trellodoro.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.asalman.trellodoro.R;
import com.asalman.trellodoro.application.MyApplication;
import com.asalman.trellodoro.models.Card;
import com.asalman.trellodoro.pomodoro.Pomodoro;
import com.asalman.trellodoro.ui.activities.PomodoroActivity;

import java.util.List;

/**
 * Created by asalman on 01/10/16.
 */
public class TaskListAdapter extends ArrayAdapter<Card>
        implements View.OnClickListener {

    private LayoutInflater mInflater;

    public TaskListAdapter(Context context, List<Card> items) {
        super(context, 0, items);
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public long getItemId(int position) {
        return (long)(getItem(position).getPos() * 1000 );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.list_item_tasks, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView
                    .findViewById(R.id.txt_title);
            holder.test = (TextView) convertView
                    .findViewById(R.id.btn_test);
            holder.test.setOnClickListener(this);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Card item = getItem(position);
        holder.title.setText(item.getName());
        holder.test.setTag(position);

        return convertView;
    }

    private static class ViewHolder {
        public TextView title;
        public TextView test;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Card item = getItem(position);
        switch (v.getId()) {
            case R.id.btn_test:
                Pomodoro pomodoro = MyApplication.getPomodoro();
                if (pomodoro != null) {
                    if (pomodoro.getState() != Pomodoro.States.NONE &&
                            !pomodoro.getID().equals(item.getId())) {
                        Toast.makeText(getContext(), item.getName(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Toast.makeText(getContext(), item.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this.getContext(), PomodoroActivity.class);
                intent.putExtra(PomodoroActivity.EXTRA_CARD_ID, item.getId());
                this.getContext().startActivity(intent);
                break;
        }
    }
}
