package com.mozia.VmusicBox;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;


import com.handmark.pulltorefresh.library.internal.ViewCompat;

import java.util.jar.Attributes;

public class scrollHideToolbar extends FloatingActionButton.Behavior {
    public scrollHideToolbar(Context context, AttributeSet atrrs)
    {
        super(context,atrrs);
    }
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if(dyConsumed>0 && child.getVisibility()==View.VISIBLE)
        {
            child.hide();
        }
        else if(dyConsumed<0 && child.getVisibility()==View.GONE)
            child.show();

    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes==android.support.v4.view.ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}
