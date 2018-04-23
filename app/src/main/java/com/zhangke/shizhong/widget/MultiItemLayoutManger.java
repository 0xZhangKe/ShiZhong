package com.zhangke.shizhong.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 具有多种 ItemViewType 的 LayoutManager
 * <p>
 * Created by ZhangKe on 2018/4/8.
 */
public class MultiItemLayoutManger extends RecyclerView.LayoutManager {

    public static final int BANNER_ITEM_TYPE = 0;
    public static final int TITLE_ITEM_TYPE = 1;
    public static final int MENU_ITEM_TYPE = 2;

    private final int spanCount;

    private int verticalScrollOffset = 0;
    private int totalHeight = 0;

    public MultiItemLayoutManger(int spanCount) {
        this.spanCount = spanCount;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0 || state.isPreLayout()) {
            return;
        }
        detachAndScrapAttachedViews(recycler);

        int curWidth = 0, curLineTop = 0;
        int horizontalCount = 0;//横向已经摆放的个数
        int widthDivider = -1;//横向间隔
        int lastViewType = MENU_ITEM_TYPE;
        int lastHeight = 0;
        for (int i = 0; i < getItemCount(); i++) {
            View view = recycler.getViewForPosition(i);
            addView(view);

            measureChildWithMargins(view, 0, 0);

            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);

            int viewType = getItemViewType(view);

            if (viewType == TITLE_ITEM_TYPE || viewType == BANNER_ITEM_TYPE) {
                if (i != 0) {
                    curLineTop += lastHeight;
                }
                layoutDecorated(view, 0, curLineTop, width, curLineTop + height);
                horizontalCount = 0;
                curWidth = 0;
                lastHeight = height;
                lastViewType = viewType;
            } else {
                if (widthDivider == -1) {
                    widthDivider = (getWidth() - width * spanCount) / (spanCount + 1);
                }
                if (horizontalCount >= spanCount) {
                    curLineTop += lastHeight;
                    layoutDecorated(view, widthDivider, curLineTop, widthDivider + width, curLineTop + height);
                    horizontalCount = 0;
                    curWidth = width + widthDivider * 2;
                    lastHeight = height;
                    lastViewType = viewType;
                } else {
                    if (curWidth == 0) {
                        curWidth = widthDivider;
                    }
                    if (i != 0 && lastViewType != MENU_ITEM_TYPE) {
                        curLineTop += lastHeight;
                    }
                    layoutDecorated(view, curWidth, curLineTop, curWidth + width, curLineTop + height);
                    curWidth += width + widthDivider;
                    horizontalCount++;
                    lastHeight = height;
                    lastViewType = viewType;
                }
            }
            if (i == getItemCount() - 1) {
                curLineTop += lastHeight;
            }
        }
        totalHeight = Math.max(curLineTop, getVerticalSpace());
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int travel = dy;
        if (verticalScrollOffset + dy < 0) {
            travel = -verticalScrollOffset;
        } else if (verticalScrollOffset + dy > totalHeight - getVerticalSpace()) {//如果滑动到最底部
            travel = totalHeight - getVerticalSpace() - verticalScrollOffset;
        }
        verticalScrollOffset += travel;
        offsetChildrenVertical(-travel);
        return travel;
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }
}
