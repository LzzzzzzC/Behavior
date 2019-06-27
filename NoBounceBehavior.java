import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 解决AppbarLayout惯性回弹以及RecyclerView加载更多后惯性滑动
 * Created by chao.zheng on 2019/4/11.
 */
public class NoBounceBehavior extends AppBarLayout.Behavior {
    private boolean isFlinging;
    private boolean shouldBlockNestedScroll;

    public NoBounceBehavior() {
    }

    public NoBounceBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        shouldBlockNestedScroll = isFlinging;
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            isFlinging = true;
        }
        stopNestedScrollIfNecessary(dy, child, target, type);
        if (!shouldBlockNestedScroll) {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int
            dxUnconsumed, int dyUnconsumed, int type) {
        stopNestedScrollIfNecessary(dyUnconsumed, child, target, type);
        if (!shouldBlockNestedScroll) {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, abl, target, type);
        isFlinging = false;
        shouldBlockNestedScroll = false;
    }

    private void stopNestedScrollIfNecessary(int dy, AppBarLayout child, View target, int type) {
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            final int currentOffset = getTopAndBottomOffset();
            if ((dy < 0 && currentOffset == 0) || (dy > 0 && currentOffset == -child.getTotalScrollRange())) {
                ViewCompat.stopNestedScroll(target, ViewCompat.TYPE_NON_TOUCH);
            }
        }
    }
}
