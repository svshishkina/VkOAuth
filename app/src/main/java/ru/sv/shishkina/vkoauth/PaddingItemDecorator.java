package ru.sv.shishkina.vkoauth;


import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class PaddingItemDecorator extends RecyclerView.ItemDecoration {
    private final int leftPadding;
    private final int topPadding;
    private final int rightPadding;
    private final int bottomPadding;
    private final int firstAndLast;

    public PaddingItemDecorator(@NonNull Resources res,
                                @DimenRes int verticalSpacing,
                                @DimenRes int horizontalSpacing,
                                @DimenRes int spacing) {
        this(res.getDimensionPixelSize(verticalSpacing),
                res.getDimensionPixelSize(horizontalSpacing), res.getDimensionPixelSize(spacing));
    }

    public PaddingItemDecorator(int leftPadding, int topPadding, int rightPadding, int bottomPadding, int firstAndLast) {
        super();
        this.leftPadding = leftPadding;
        this.topPadding = topPadding;
        this.rightPadding = rightPadding;
        this.bottomPadding = bottomPadding;
        this.firstAndLast = firstAndLast;
    }

    public PaddingItemDecorator(int vertical, int horizontal, int firstAndLast) {
        super();
        this.leftPadding = horizontal;
        this.topPadding = vertical;
        this.rightPadding = horizontal;
        this.bottomPadding = vertical;
        this.firstAndLast = firstAndLast;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = firstAndLast;
        } else {
            outRect.bottom = bottomPadding;
        }

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = firstAndLast;
        } else {
            outRect.top = topPadding;
        }

        outRect.left = leftPadding;
        outRect.right = rightPadding;
    }


}

