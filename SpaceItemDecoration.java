package com.example.zhai.test.ui;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lwj on 2017/8/22.
 * lwjfork@gmail.com
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int mHSpace;// 水平间距
    private int mVSpace;// 竖直间距

    private int topEdge; // 上边距
    private int bottomEdge;  // 下边距
    private int leftEdge;   // 左边距
    private int rightEdge; // 有边距


    public SpaceItemDecoration() {
    }


    public static class Builder {
        private SpaceItemDecoration spaceItemDecoration;

        public Builder() {
            spaceItemDecoration = new SpaceItemDecoration();
        }

        public Builder setHSpace(int mHSpace) {
            spaceItemDecoration.setHSpace(mHSpace);
            return this;
        }

        public Builder setVSpace(int mVSpace) {
            spaceItemDecoration.setVSpace(mVSpace);
            return this;
        }

        public Builder setTopEdge(int topEdge) {
            spaceItemDecoration.setTopEdge(topEdge);
            return this;
        }

        public Builder setBottomEdge(int bottomEdge) {
            spaceItemDecoration.setBottomEdge(bottomEdge);
            return this;
        }

        public Builder setLeftEdge(int leftEdge) {
            spaceItemDecoration.setLeftEdge(leftEdge);
            return this;
        }

        public Builder setRightEdge(int rightEdge) {
            spaceItemDecoration.setRightEdge(rightEdge);
            return this;
        }

        public SpaceItemDecoration build() {
            return spaceItemDecoration;
        }
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        int childPosition = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter().getItemCount();
        if (manager != null) {
            if (manager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
                setGridOffset(gridLayoutManager.getOrientation(), gridLayoutManager.getSpanCount(), outRect, childPosition, itemCount);
                return;
            } else if (manager instanceof LinearLayoutManager) {
                setLinearOffset(((LinearLayoutManager) manager).getOrientation(), outRect, childPosition, itemCount);
                return;
            }
        }
        super.getItemOffsets(outRect, view, parent, state);
    }

    private void setGridOffset(int orientation, int spanCount, Rect outRect, int childPosition, int itemCount) {

        if (orientation == GridLayoutManager.VERTICAL) { // 竖直
            gridVOffset(orientation, spanCount, outRect, childPosition, itemCount);
        } else if (orientation == GridLayoutManager.HORIZONTAL) { // 水平方向
            gridHOffset(orientation, spanCount, outRect, childPosition, itemCount);
        }
    }

    public void gridVOffset(int orientation, int spanCount, Rect outRect, int childPosition, int itemCount) {
        int column = childPosition % spanCount; // 第几列
        float totalHSpace = leftEdge + rightEdge + mHSpace * (spanCount - 1);// 水平方向总space
        float eachHSpace = totalHSpace / spanCount;
        float topSpace = 0;
        float bottomSpace = 0;
        float leftSpace = 0;
        float rightSpace = 0;
        // 初始状态
        topSpace = 0;
        bottomSpace = 0;
        if (topEdge > 0) { // 上边距
            // 第一行
            if (isFirstRow(orientation, spanCount, childPosition, itemCount)) {
                topSpace = topEdge;
            }
        }
        // 最后一行 // 下边距
        if (isLastRow(orientation, spanCount, childPosition, itemCount)) {
            if (bottomEdge > 0) {
                bottomSpace = bottomEdge;
            }
        } else {
            bottomSpace = mVSpace;
        }
        if (spanCount == 1) {
            leftSpace = leftEdge;
        } else {
            leftSpace = (mHSpace - eachHSpace) * column + leftEdge;
        }

        rightSpace = eachHSpace - leftSpace;
        outRect.set((int) leftSpace, (int) topSpace, (int) rightSpace, (int) bottomSpace);
    }


    public void gridHOffset(int orientation, int spanCount, Rect outRect, int childPosition, int itemCount) {
        int row = childPosition % spanCount; // 第几行
        float totalVSpace = topEdge + bottomEdge + mVSpace * (spanCount - 1);// 竖直方向总space
        float eachVSpace = totalVSpace / spanCount;
        float topSpace = 0;
        float bottomSpace = 0;
        float leftSpace = 0;
        float rightSpace = 0;
        // 初始状态
        leftSpace = 0;
        rightSpace = 0;
        if (leftEdge > 0) {
            // 第一列
            if (isFirstColumn(orientation, spanCount, childPosition, itemCount)) {
                leftSpace = leftEdge;
            }
        }

        // 最后一列
        if (isLastColumn(orientation, spanCount, childPosition, itemCount)) {
            if (rightEdge > 0) {
                rightSpace = rightEdge;
            }
        } else {
            rightSpace = mHSpace;
        }
        if (spanCount == 1) {
            topSpace = topEdge;
        } else {
            topSpace = (mVSpace - eachVSpace) * row + topEdge;
        }
        bottomSpace = eachVSpace - topSpace;
        outRect.set((int) leftSpace, (int) topSpace, (int) rightSpace, (int) bottomSpace);
    }


    private void setLinearOffset(int orientation, Rect outRect, int childPosition, int itemCount) {
        if (orientation == LinearLayoutManager.HORIZONTAL) {
            linearHOffset(outRect, childPosition, itemCount);
        } else {
            linearVOffset(outRect, childPosition, itemCount);
        }
    }

    private void linearVOffset(Rect outRect, int childPosition, int itemCount) {
        if (childPosition == 0) {
            // 第一个要设置PaddingTop
            outRect.set(0, topEdge, 0, mVSpace);
        } else if (childPosition == itemCount - 1) {
            // 最后一个要设置PaddingBottom
            outRect.set(0, 0, 0, bottomEdge);
        } else {
            outRect.set(0, 0, 0, mVSpace);
        }
        float totalVSpace = topEdge + bottomEdge + mVSpace * (itemCount - 1);// 水平方向总space
        float eachVSpace = totalVSpace / itemCount;
        float topSpace = 0;
        float bottomSpace = 0;
        float leftSpace = leftEdge;
        float rightSpace = rightEdge;

        topSpace = (mVSpace - eachVSpace) * childPosition + topEdge;
        bottomSpace = eachVSpace - topSpace;
        outRect.set((int) leftSpace, (int) topSpace, (int) rightSpace, (int) bottomSpace);
    }

    private void linearHOffset(Rect outRect, int childPosition, int itemCount) {
        float totalHSpace = leftEdge + rightEdge + mHSpace * (itemCount - 1);// 水平方向总space
        float eachHSpace = totalHSpace / itemCount;
        float topSpace = topEdge;
        float bottomSpace = bottomEdge;
        float leftSpace = 0;
        float rightSpace = 0;

        leftSpace = (mHSpace - eachHSpace) * childPosition + leftEdge;
        rightSpace = eachHSpace - leftSpace;
        outRect.set((int) leftSpace, (int) topSpace, (int) rightSpace, (int) bottomSpace);
    }

    /**
     * 是否是第一行
     *
     * @param orientation   方向
     * @param spanCount     行／列 数
     * @param childPosition 索引
     * @param itemCount     item 数量
     * @return 是否是第一行
     */
    public boolean isFirstRow(int orientation, int spanCount, int childPosition, int itemCount) {
        if (orientation == GridLayoutManager.VERTICAL) {
            if (childPosition < spanCount) {
                return true;
            }
        } else if (orientation == GridLayoutManager.HORIZONTAL) {
            if (childPosition % spanCount == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是最后一行
     *
     * @param orientation   方向
     * @param spanCount     行／列 数
     * @param childPosition 索引
     * @param itemCount     item 数量
     * @return 是否是第一行
     */
    public boolean isLastRow(int orientation, int spanCount, int childPosition, int itemCount) {
        if (orientation == GridLayoutManager.VERTICAL) {
            int offset = 0;
            if (itemCount % spanCount == 0) {
                offset = spanCount;
            } else {
                offset = itemCount % spanCount;
            }
            if (childPosition >= (itemCount - offset)) {
                return true;
            }
        } else if (orientation == GridLayoutManager.HORIZONTAL) {
            if (childPosition % spanCount == (spanCount - 1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是第一列
     *
     * @param orientation   方向
     * @param spanCount     行／列 数
     * @param childPosition 索引
     * @param itemCount     item 数量
     * @return 是否是第一行
     */
    public boolean isFirstColumn(int orientation, int spanCount, int childPosition, int itemCount) {
        if (orientation == GridLayoutManager.VERTICAL) {
            if (childPosition % spanCount == 0) {
                return true;
            }
        } else if (orientation == GridLayoutManager.HORIZONTAL) {
            if (childPosition < spanCount) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是最后一列
     *
     * @param orientation   方向
     * @param spanCount     行／列 数
     * @param childPosition 索引
     * @param itemCount     item 数量
     * @return 是否是第一行
     */
    public boolean isLastColumn(int orientation, int spanCount, int childPosition, int itemCount) {
        if (orientation == GridLayoutManager.VERTICAL) {

            if (childPosition % spanCount == (spanCount - 1)) {
                return true;
            }
        } else if (orientation == GridLayoutManager.HORIZONTAL) {
            int offset = 0;
            if (itemCount % spanCount == 0) {
                offset = spanCount;
            } else {
                offset = itemCount % spanCount;
            }
            if (childPosition >= (itemCount - offset)) {
                return true;
            }
        }
        return false;
    }


    public void setHSpace(int mHSpace) {
        this.mHSpace = mHSpace;
    }

    public void setVSpace(int mVSpace) {
        this.mVSpace = mVSpace;
    }

    public void setTopEdge(int topEdge) {
        this.topEdge = topEdge;
    }

    public void setBottomEdge(int bottomEdge) {
        this.bottomEdge = bottomEdge;
    }

    public void setLeftEdge(int leftEdge) {
        this.leftEdge = leftEdge;
    }

    public void setRightEdge(int rightEdge) {
        this.rightEdge = rightEdge;
    }
}
