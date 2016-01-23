package com.asalman.trellodoro.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.asalman.trellodoro.R;
import com.asalman.trellodoro.application.MyApplication;
import com.asalman.trellodoro.models.Card;
import com.asalman.trellodoro.pomodoro.Pomodoro;
import com.asalman.trellodoro.ui.activities.PomodoroActivity;
import com.asalman.trellodoro.utils.Analytics;
import com.asalman.trellodoro.utils.DateTimeUtils;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by asalman on 01/10/16.
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder>
        implements View.OnClickListener {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<Card> mCards;

    static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitle;
        public final TextView mTotalTime;
        public final TextView mTotalPomodoros;
        public final Button mTestButton;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.txt_title);
            mTotalTime = (TextView) view.findViewById(R.id.txt_totaltime);
            mTotalPomodoros = (TextView) view.findViewById(R.id.txt_totalpomodoros);
            mTestButton = (Button) view.findViewById(R.id.btn_test);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitle.getText();
        }
    }

    public TaskListAdapter(Context context, List<Card> items) {
        this.mContext = context;
        this.mCards = items;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_tasks, parent, false);
        TaskListAdapter.ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mTestButton.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Card card = mCards.get(position);

        holder.mView.setTag(position);
        holder.mTitle.setText(card.getName());
        DateTime dateTime = new DateTime(card.getTotalSpentTime());
        String totalTimerFormmated = DateTimeUtils.getTimeFormatted(dateTime);
        holder.mTotalTime.setText(String.format(mContext.getString(R.string.txt_total_time), totalTimerFormmated));
        holder.mTotalPomodoros.setText(String.format(mContext.getString(R.string.txt_total_pomos), card.getSpentPomodoros()));
        holder.mTestButton.setTag(position);
    }

    @Override
    public int getItemCount() {
        return (null != mCards ? mCards.size() : 0);
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Card item = mCards.get(position);
        switch (v.getId()) {
            case R.id.btn_test:
                Pomodoro pomodoro = MyApplication.getPomodoro();
                if (pomodoro != null) {
                    if (pomodoro.getState() != Pomodoro.States.NONE &&
                            !pomodoro.getID().equals(item.getId())) {
                        Toast.makeText(mContext, item.getName(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Toast.makeText(mContext, item.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, PomodoroActivity.class);
                intent.putExtra(PomodoroActivity.EXTRA_CARD_ID, item.getId());
                this.mContext.startActivity(intent);
                MyApplication.getAnalytics().sendEvent(Analytics.AppCategories.CLICKS,
                        "list_click",
                        "Card List Click");
                break;
        }
    }
}
