package com.niedzwiecki.przemyslguide.ui.base;

import java.util.List;

/**
 * Created by Niedzwiecki on 11/11/2017.
 */

class PageList<T> extends BaseModel {
    public static final int FIRST_PAGE = 1;
    public static final int ITEMS_PER_PAGE = 20;

    public List<T> items;

    public Meta meta;

}
