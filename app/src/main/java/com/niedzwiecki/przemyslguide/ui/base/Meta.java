package com.niedzwiecki.przemyslguide.ui.base;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Niedzwiecki on 11/11/2017.
 */

public class Meta extends BaseModel {
    public int next;

    public int previous;

    public int per_page;

    public int total;

    public int pages;

    public int page;

    public boolean hasNext() {
        return next != 0;
    }

    public Date last;

    @SerializedName("min_id")
    public long minId;
}
