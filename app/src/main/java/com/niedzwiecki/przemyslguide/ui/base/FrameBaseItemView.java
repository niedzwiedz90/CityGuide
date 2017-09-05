package com.niedzwiecki.przemyslguide.ui.base;

import android.content.Context;
import android.util.AttributeSet;


public abstract class FrameBaseItemView<T> extends FrameBaseView implements SetData<T> {

    public FrameBaseItemView(Context context) {
        super(context);
    }

    public FrameBaseItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameBaseItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}
