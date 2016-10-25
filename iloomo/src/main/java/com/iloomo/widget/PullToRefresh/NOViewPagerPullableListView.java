package com.iloomo.widget.PullToRefresh;





import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class NOViewPagerPullableListView extends ListView implements Pullable {

    public NOViewPagerPullableListView(Context context) {
        super(context);

        setFadingEdgeLength(0);
    }

    public NOViewPagerPullableListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setFadingEdgeLength(0);
    }

    public NOViewPagerPullableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFadingEdgeLength(0);
    }

    @Override
    public boolean canPullDown() {
        if (getCount() == 0) {
            // 没有item的时候也可以下拉刷新
            return true;
        } else if (getFirstVisiblePosition() == 0
                && getChildAt(0).getTop() >= 0) {
            // 滑到ListView的顶部了
            return true;
        } else
            return false;
    }

    @Override
    public boolean canPullUp() {
        try {

            if (getCount() == 0) {
                // 没有item的时候也可以上拉加载
                return true;
            } else if (getLastVisiblePosition() == (getCount() - 1)) {
                // 滑倒底部
                if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                        && getChildAt(
                        getLastVisiblePosition()
                                - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
                    return true;
            }
        } catch (Exception e) {

        }
        return false;
    }


}
