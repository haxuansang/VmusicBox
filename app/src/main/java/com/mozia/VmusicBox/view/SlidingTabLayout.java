package com.mozia.VmusicBox.view;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

public class SlidingTabLayout extends HorizontalScrollView {
    private static final int TAB_VIEW_PADDING_DIPS = 16;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 12;
    private static final int TITLE_OFFSET_DIPS = 24;
    private final SlidingTabStrip mTabStrip;
    private int mTabViewLayoutId;
    private int mTabViewTextViewId;
    private int mTitleOffset;
    private ViewPager mViewPager;
    private OnPageChangeListener mViewPagerPageChangeListener;

    private class TabClickListener implements OnClickListener {
        private TabClickListener() {
        }

        public void onClick(View v) {
            for (int i = 0; i < SlidingTabLayout.this.mTabStrip.getChildCount(); i++) {
                if (v == SlidingTabLayout.this.mTabStrip.getChildAt(i)) {
                    SlidingTabLayout.this.mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }

    public interface TabColorizer {
        int getDividerColor(int i);

        int getIndicatorColor(int i);
    }

    private class InternalViewPagerListener implements OnPageChangeListener {
        private int mScrollState;

        private InternalViewPagerListener() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = SlidingTabLayout.this.mTabStrip.getChildCount();
            if (tabStripChildCount != 0 && position >= 0 && position < tabStripChildCount) {
                SlidingTabLayout.this.mTabStrip.onViewPagerPageChanged(position, positionOffset);
                View selectedTitle = SlidingTabLayout.this.mTabStrip.getChildAt(position);
                SlidingTabLayout.this.scrollToTab(position, selectedTitle != null ? (int) (((float) selectedTitle.getWidth()) * positionOffset) : 0);
                if (SlidingTabLayout.this.mViewPagerPageChangeListener != null) {
                    SlidingTabLayout.this.mViewPagerPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }
        }

        public void onPageScrollStateChanged(int state) {
            this.mScrollState = state;
            if (SlidingTabLayout.this.mViewPagerPageChangeListener != null) {
                SlidingTabLayout.this.mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        public void onPageSelected(int position) {
            if (this.mScrollState == 0) {
                SlidingTabLayout.this.mTabStrip.onViewPagerPageChanged(position, 0.0f);
                SlidingTabLayout.this.scrollToTab(position, 0);
            }
            if (SlidingTabLayout.this.mViewPagerPageChangeListener != null) {
                SlidingTabLayout.this.mViewPagerPageChangeListener.onPageSelected(position);
            }
        }
    }

    public SlidingTabLayout(Context context) {
        this(context, null);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setHorizontalScrollBarEnabled(false);
        setFillViewport(true);
        this.mTitleOffset = (int) (24.0f * getResources().getDisplayMetrics().density);
        this.mTabStrip = new SlidingTabStrip(context);
        addView(this.mTabStrip, -1, -2);
    }

    public void setCustomTabColorizer(TabColorizer tabColorizer) {
        this.mTabStrip.setCustomTabColorizer(tabColorizer);
    }

    public void setSelectedIndicatorColors(int... colors) {
        this.mTabStrip.setSelectedIndicatorColors(colors);
    }

    public void setDividerColors(int... colors) {
        this.mTabStrip.setDividerColors(colors);
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mViewPagerPageChangeListener = listener;
    }

    public void setCustomTabView(int layoutResId, int textViewId) {
        this.mTabViewLayoutId = layoutResId;
        this.mTabViewTextViewId = textViewId;
    }

    public void setViewPager(ViewPager viewPager) {
        this.mTabStrip.removeAllViews();
        this.mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }

    protected TextView createDefaultTabView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(17);
        textView.setTextSize(2, 12.0f);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        if (VERSION.SDK_INT >= 11) {
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(16843534, outValue, true);
            textView.setBackgroundResource(outValue.resourceId);
        }
        if (VERSION.SDK_INT >= 14) {
            textView.setAllCaps(true);
        }
        int padding = (int) (16.0f * getResources().getDisplayMetrics().density);
        textView.setPadding(padding, padding, padding, padding);
        return textView;
    }

    private void populateTabStrip() {
        PagerAdapter adapter = this.mViewPager.getAdapter();
        OnClickListener tabClickListener = new TabClickListener();
        for (int i = 0; i < adapter.getCount(); i++) {
            View tabView = null;
            TextView tabTitleView = null;
            if (this.mTabViewLayoutId != 0) {
                tabView = LayoutInflater.from(getContext()).inflate(this.mTabViewLayoutId, this.mTabStrip, false);
                tabTitleView = (TextView) tabView.findViewById(this.mTabViewTextViewId);
            }
            if (tabView == null) {
                tabView = createDefaultTabView(getContext());
            }
            if (tabTitleView == null && TextView.class.isInstance(tabView)) {
                tabTitleView = (TextView) tabView;
            }
            tabTitleView.setText(adapter.getPageTitle(i));
            tabView.setOnClickListener(tabClickListener);
            this.mTabStrip.addView(tabView);
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mViewPager != null) {
            scrollToTab(this.mViewPager.getCurrentItem(), 0);
        }
    }

    private void scrollToTab(int tabIndex, int positionOffset) {
        int tabStripChildCount = this.mTabStrip.getChildCount();
        if (tabStripChildCount != 0 && tabIndex >= 0 && tabIndex < tabStripChildCount) {
            View selectedChild = this.mTabStrip.getChildAt(tabIndex);
            if (selectedChild != null) {
                int targetScrollX = selectedChild.getLeft() + positionOffset;
                if (tabIndex > 0 || positionOffset > 0) {
                    targetScrollX -= this.mTitleOffset;
                }
                scrollTo(targetScrollX, 0);
            }
        }
    }
}
