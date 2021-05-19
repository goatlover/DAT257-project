package com.example.application.animations;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Class that holds different kinds of animations 
 */

public class Animations {

    //TODO add comment explaining method here
    public static void expand(View view) {
        Animation animation = expandAction(view);
        view.startAnimation(animation);
    }
    
    private static Animation expandAction(final View view) {

        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int actualheight = view.getMeasuredHeight();

        view.getLayoutParams().height = 0;
        view.setVisibility(View.VISIBLE);

        // TODO explain what happens here?
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                view.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : Math.max(1, (int) (actualheight * interpolatedTime));
                view.requestLayout();
            }
        };


        animation.setDuration((long) (actualheight / view.getContext().getResources().getDisplayMetrics().density));

        view.startAnimation(animation);

        return animation;


    }

    //TODO add comment explaining method here
    public static void collapse(final View view) {

        final int actualHeight = view.getMeasuredHeight();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                if (interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().height = actualHeight - (int) (actualHeight * interpolatedTime);
                    view.requestLayout();

                }
            }
        };

        animation.setDuration((long) (actualHeight/ view.getContext().getResources().getDisplayMetrics().density));
        view.startAnimation(animation);
    }



    //TODO add comment explaining method here
    public static boolean toggleArrow(View view, boolean isExpanded) {

        if (isExpanded) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }
}
