package com.zhangke.shizhong.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 上下左右完全没有边距的 ItemDecoration
 * <p>
 * Created by ZhangKe on 2018/4/22.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    public SpacesItemDecoration() {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = 0;
        outRect.right = 0;
        outRect.bottom = 0;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = 0;
        }
    }
}
