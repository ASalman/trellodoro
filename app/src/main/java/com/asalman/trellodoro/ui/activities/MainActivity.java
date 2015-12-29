package com.asalman.trellodoro.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.asalman.trellodoro.R;
import com.asalman.trellodoro.models.Board;
import com.asalman.trellodoro.restapi.RestClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements Callback<List<Board>> {

    Callback<List<com.asalman.trellodoro.models.List>> listCallback = new Callback<List<com.asalman.trellodoro.models.List>>() {
        @Override
        public void success(List<com.asalman.trellodoro.models.List> lists, Response response) {
            for (com.asalman.trellodoro.models.List list : lists){
                Log.d("List name: ", list.getName());
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d("List Error", error.getCause().toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RestClient.getBoardServices().getAllBoards(this);
    }

    @Override
    public void success(List<Board> boards, Response response) {
        for (Board board : boards){
            Log.d("Board", board.getName());
            RestClient.getListServices().getBoardLists(board.getId(), listCallback);
        }
    }

    @Override
    public void failure(RetrofitError error) {
        Log.d("Board Error", error.getCause().toString());
    }
}
