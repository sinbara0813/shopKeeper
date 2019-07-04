package com.d2cmall.shopkeeper.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by Administrator on 2017/3/14.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItemDecoration(Context context, int space) {
        this.space = (int) (space * context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        //判断是GridLayoutManager还是LinearLayoutManager
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            outRect.top = parent.getChildLayoutPosition(view) < spanCount ? space : 0;
            outRect.bottom=space;
        }else if (parent.getLayoutManager() instanceof StaggeredGridLayoutManager){
            int spanCount = ((StaggeredGridLayoutManager) parent.getLayoutManager()).getSpanCount();
            outRect.top = parent.getChildLayoutPosition(view) < spanCount ? space : 0;
            outRect.bottom=space;
        } else {
            outRect.top = parent.getChildLayoutPosition(view) == 0 ? 0 : space;
        }
    }
}
