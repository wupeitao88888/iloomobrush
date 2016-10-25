package com.iloomo.widget.imgscroll;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MyImgScroll extends ViewPager {
    private Activity mActivity;
    private List<View> mListViews;
    private int mScrollTime = 0;
    private Timer timer;
    private int oldIndex = 0;
    private int curIndex = 0;

    public MyImgScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void start(Activity mainActivity, List<View> imgList,
                      int scrollTime, LinearLayout ovalLayout, int ovalLayoutId,
                      int ovalLayoutItemId, int focusedId, int normalId) {
        mActivity = mainActivity;
        mListViews = imgList;
        mScrollTime = scrollTime;

        setOvalLayout(ovalLayout, ovalLayoutId, ovalLayoutItemId, focusedId,
                normalId);
        this.setAdapter(new MyPagerAdapter());
        if (scrollTime != 0 && imgList.size() > 1) {

            new FixedSpeedScroller(mActivity).setDuration(this, 700);

            startTimer();

            this.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        startTimer();
                    } else {
                        stopTimer();
                    }
                    return false;
                }
            });
        }
        if (mListViews.size() > 1) {
            this.setCurrentItem((Integer.MAX_VALUE / 2)
                    - (Integer.MAX_VALUE / 2) % mListViews.size());
        }
    }


    private void setOvalLayout(final LinearLayout ovalLayout, int ovalLayoutId,
                               final int ovalLayoutItemId, final int focusedId, final int normalId) {
        if (ovalLayout != null) {
            ovalLayout.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            for (int i = 0; i < mListViews.size(); i++) {
                ovalLayout.addView(inflater.inflate(ovalLayoutId, null));

            }
            try {
                ovalLayout.getChildAt(0).findViewById(ovalLayoutItemId)
                        .setBackgroundResource(focusedId);
            } catch (Exception e) {

            }
            this.setOnPageChangeListener(new OnPageChangeListener() {
                public void onPageSelected(int i) {
                    curIndex = i % mListViews.size();

                    ovalLayout.getChildAt(oldIndex).findViewById(ovalLayoutItemId)
                            .setBackgroundResource(normalId);

                    ovalLayout.getChildAt(curIndex).findViewById(ovalLayoutItemId)
                            .setBackgroundResource(focusedId);
                    oldIndex = curIndex;
                }

                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                public void onPageScrollStateChanged(int arg0) {
                }
            });
        }
    }

    public int getCurIndex() {
        return curIndex;
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    public void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        MyImgScroll.this.setCurrentItem(MyImgScroll.this
                                .getCurrentItem() + 1);
                    }
                });
            }
        }, mScrollTime, mScrollTime);
    }


    private class MyPagerAdapter extends PagerAdapter {
        public void finishUpdate(View arg0) {
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        public int getCount() {
            if (mListViews.size() == 1) {
                return mListViews.size();
            }
            return Integer.MAX_VALUE;
        }

        public Object instantiateItem(View v, int i) {
            try {
                if (((ViewPager) v).getChildCount() == mListViews.size()) {

                    ((ViewPager) v)
                            .removeView(mListViews.get(i % mListViews.size()));

                }
                ((ViewPager) v).addView(mListViews.get(i % mListViews.size()), 0);
            } catch (Exception e) {
            }
            try {
                return mListViews.get(i % mListViews.size());
            }catch (Exception e){
                return null;
            }

        }


        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        public Parcelable saveState() {
            return null;
        }

        public void startUpdate(View arg0) {
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
        }
    }


}
