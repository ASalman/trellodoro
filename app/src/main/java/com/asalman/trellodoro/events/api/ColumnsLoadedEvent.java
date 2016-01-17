package com.asalman.trellodoro.events.api;

import com.asalman.trellodoro.models.Column;
import java.util.List;

/**
 * Created by asalman on 1/9/16.
 */
public class ColumnsLoadedEvent {

    List<Column> columnList;

    public ColumnsLoadedEvent(List<Column> listList){
        this.columnList = listList;
    }

    public List<Column> getColumns(){
        return this.columnList;
    }

}
