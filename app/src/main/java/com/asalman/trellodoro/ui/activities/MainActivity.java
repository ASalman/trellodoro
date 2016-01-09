package com.asalman.trellodoro.ui.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.asalman.trellodoro.R;
import com.asalman.trellodoro.models.Board;
import com.asalman.trellodoro.models.Card;
import com.asalman.trellodoro.models.Column;
import com.asalman.trellodoro.rest.RestClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements Callback<List<Board>> {

    Callback<List<Column>> listCallback = new Callback<List<Column>>() {
        @Override
        public void success(List<Column> lists, Response response) {
            for (Column list : lists){
                Log.d("List name: ", list.getName());
                RestClient.getCardServices().getListCards(list.getId(), cardCallback);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d("List Error", error.getCause().toString());
        }
    };

    Callback<List<Card>> cardCallback = new Callback<List<Card>>() {
        @Override
        public void success(List<Card> cards, Response response) {
            for (Card card : cards) {
                Log.d("Card name: ", card.getName());
                if ("567d2dab9e398c90a1f32663".equals(card.getId())){
                    RestClient.getCardServices().updateCardList(card.getId(), "5659c2f09d3e7726b3478680" , cardUpdateCallback);
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d("Card list Error", error.getCause().toString());
        }
    };

    Callback<Card> cardUpdateCallback = new Callback<Card>() {
        @Override
        public void success(Card card, Response response) {
            Log.d("Update card done", card.getId().toString());
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d("Update card error", error.getCause().toString());
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RestClient.getBoardServices().getAllBoards(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ConfigWizardActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void success(List<Board> boards, Response response) {
        for (Board board : boards){
            Log.d("Board", board.getName());
            RestClient.getColumnServices().getBoardLists(board.getId(), listCallback);
        }
    }

    @Override
    public void failure(RetrofitError error) {
        Log.d("Board Error", error.getCause().toString());
    }
}
