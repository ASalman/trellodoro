package com.asalman.trellodoro.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.asalman.trellodoro.models.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asalman on 1/18/16.
 */
public class PomodoroDAL {

    // Table Names
    private static final String TABLE_NAME = "pomodoro";
    // Logcat tag
    private static final String LOG = PomodoroDAL.class.getName();

    // column names
    private static class Columns {
        public static final String ID = "id";
        public static final String LIST_ID = "id_list";
        public static final String NAME = "name";
        public static final String POSITION = "position";
        public static final String PREVIOUS_STATE = "previous_state";
        public static final String CURRENT_STATE = "current_state";
        public static final String REMAINING_TIME = "remaining_time";
        public static final String LAST_POMODORO_TIME = "last_pomodoro_time";
        public static final String NEXT_POMODORO_TIME = "next_pomodoro_time";
        public static final String SPENT_POMODOROS = "spent_pomodoros";
        public static final String TOTAL_SPENT_TIME = "total_spent_time";
    }

    private final SQLiteOpenHelper db;

    public PomodoroDAL(SQLiteOpenHelper db) {
        this.db = db;
    }

    private SQLiteDatabase getWritableDatabase(){
        return this.db.getWritableDatabase();
    }

    private SQLiteDatabase getReadableDatabase() {
        return this.db.getReadableDatabase();
    }


    // Table Create Statements
    // Pomodoro table create statement
    private static final String CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + "( " + Columns.ID + " TEXT PRIMARY KEY NOT NULL,"
            + Columns.LIST_ID + " TEXT,"
            + Columns.NAME + " TEXT,"
            + Columns.POSITION + " REAL,"
            + Columns.PREVIOUS_STATE + " INTEGER,"
            + Columns.CURRENT_STATE + " INTEGER,"
            + Columns.REMAINING_TIME + " INTEGER,"
            + Columns.LAST_POMODORO_TIME + " INTEGER,"
            + Columns.NEXT_POMODORO_TIME + " INTEGER,"
            + Columns.SPENT_POMODOROS + " INTEGER,"
            + Columns.TOTAL_SPENT_TIME + " INTEGER" + " )";


    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public static void updateTable(SQLiteDatabase db, int oldVersion, int newVersion) {
        //// on upgrade drop older tables
        //// for this version just do nothing
        //this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //// create new tables
        //createTable();
    }

    public Card sync(Card card){
        Card result = null;
        result = this.get(card.getId());
        if (result == null) {
            result = card;
            this.create(result);
        } else {
            result.setName(card.getName());
            result.setIdList(card.getIdList());
            result.setPos(card.getPos());
            this.update(result);
        }
        return result;
    }

    /*
     * Creating a card
     */
    public void create(Card card) {

        ContentValues values = getContentValues(card, false);

        // insert row
        this.getWritableDatabase().insert(TABLE_NAME, null, values);
    }

    /*
     * Updating a card
     */
    public int update(Card card) {
        ContentValues values = getContentValues(card, true);

        // updating row
        return this.getWritableDatabase().update(TABLE_NAME, values, Columns.ID + " = ? ",
                new String[]{card.getId()});
    }

    /*
     * Deleting a card
     */
    public void delete(String id) {
        this.getWritableDatabase().delete(TABLE_NAME, Columns.ID + " = ? ",
                new String[]{id});
    }

    /*
     * get single card
     */
    public Card get(String card_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE "
                + Columns.ID + " = '" + card_id + "' ";

        Log.e(LOG, selectQuery);

        Cursor c = this.getReadableDatabase().rawQuery(selectQuery, null);
        Card card = this.buildResult(c);

        return card;
    }

    /**
     * getting all cards
     */
    public List<Card> getAll() {
        List<Card> cards = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = this.getReadableDatabase().rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Card card = buildResult(c);
                cards.add(card);
            } while (c.moveToNext());
        }

        return cards;
    }

    private ContentValues getContentValues(Card card, boolean update) {
        ContentValues values = new ContentValues();
        values.put(Columns.ID, card.getId());
        values.put(Columns.LIST_ID, card.getIdList());
        values.put(Columns.NAME, card.getName());
        values.put(Columns.POSITION, card.getPos());
        values.put(Columns.PREVIOUS_STATE, card.getPreviousState());
        values.put(Columns.CURRENT_STATE, card.getCurrentState());
        values.put(Columns.REMAINING_TIME, card.getRemainingTime());
        values.put(Columns.LAST_POMODORO_TIME, card.getLastPomodoroTime());
        values.put(Columns.NEXT_POMODORO_TIME, card.getNextPomodoroTime());
        values.put(Columns.SPENT_POMODOROS, card.getSpentPomodoros());
        values.put(Columns.TOTAL_SPENT_TIME, card.getTotalSpentTime());
        return values;
    }

    private Card buildResult(Cursor c) {
        Card result = null;
        if (c != null && !c.isAfterLast()) {
            if (c.isBeforeFirst())
                c.moveToFirst();

            result = new Card();
            result.setId(c.getString(c.getColumnIndex(Columns.ID)));
            result.setIdList(c.getString(c.getColumnIndex(Columns.LIST_ID)));
            result.setName(c.getString(c.getColumnIndex(Columns.NAME)));
            result.setPos(c.getDouble(c.getColumnIndex(Columns.POSITION)));
            result.setPreviousState(c.getInt(c.getColumnIndex(Columns.PREVIOUS_STATE)));
            result.setCurrentState(c.getInt(c.getColumnIndex(Columns.CURRENT_STATE)));
            result.setRemainingTime(c.getInt(c.getColumnIndex(Columns.REMAINING_TIME)));
            result.setLastPomodoroTime(c.getInt(c.getColumnIndex(Columns.LAST_POMODORO_TIME)));
            result.setNextPomodoroTime(c.getInt(c.getColumnIndex(Columns.NEXT_POMODORO_TIME)));
            result.setSpentPomodoros(c.getInt(c.getColumnIndex(Columns.SPENT_POMODOROS)));
            result.setTotalSpentTime(c.getInt(c.getColumnIndex(Columns.TOTAL_SPENT_TIME)));

        }
        return result;
    }
}