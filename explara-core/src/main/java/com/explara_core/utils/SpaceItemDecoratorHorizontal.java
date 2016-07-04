package com.explara_core.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Kausthubh on 7/25/15.
 */
public class SpaceItemDecoratorHorizontal extends RecyclerView.ItemDecoration{
    private int space;

    public SpaceItemDecoratorHorizontal(int space){
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect,View view,RecyclerView parent,RecyclerView.State state){

        if(parent.getChildAdapterPosition(view)  == 0){
            outRect.left = space;
            outRect.right = space;

        }else if(parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount()-1){
            outRect.right = space;
        }else{
            outRect.right = space;
        }


    }




}