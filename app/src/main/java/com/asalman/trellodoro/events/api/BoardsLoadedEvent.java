package com.asalman.trellodoro.events.api;


import com.asalman.trellodoro.models.Board;
import java.util.List;

/**
 * Created by asalman on 1/9/16.
 */
public class BoardsLoadedEvent {

    List<Board> boardList;

    public BoardsLoadedEvent(List<Board> boardList){
        this.boardList = boardList;
    }

    public List<Board> getBoards(){
        return this.boardList;
    }

}
