package com.example.clock0.Ui;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.example.clock0.R;

/**
 * Created by Yassine Abou on 2/28/2021.
 */
public class ViewAnimation {

    // one view goes to the left another view goes to the right by horizontal (button's effect of opening)
    public static void divergeTwoViewHorizontal(Context context, View view1, View view2) {
        view1.startAnimation(AnimationUtils.loadAnimation(
                context,
                R.anim.right_to_left_open_translate));
        view2.startAnimation(AnimationUtils.loadAnimation(
                context,
                R.anim.left_to_right_open_translate));
    }

    // both views go at each other by horizontal (button's effect of closing)
    public static void comeCloserTwoViewHorizontal(Context context, View view1, View view2) {
        view1.startAnimation(AnimationUtils.loadAnimation(
                context,
                R.anim.left_to_right_close_translate));
        view2.startAnimation(AnimationUtils.loadAnimation(
                context,
                R.anim.right_to_left_close_translate));
    }

    //move views too the top
    public static void moveToTheTopVertical(Context context, View... views) {
        for (View view : views) {
            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bottom_to_top_translate));
        }
    }



   //hide views
    public static void fadeView(Context context, View... views) {
      for (View view : views) {
          view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_alpha));
      }
    }
}
