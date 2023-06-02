package com.example.myapplication.GlobalFunctions

import android.R
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.view.View
import java.util.*


class GlobalFunctions {

    /**
     * This method is used to set properties to the views
     *
     * @param view                  is the view of the widget
     * @param CornerRadiusOfTheView is used to set the corners of the view
     * @param Shape                 is used to set the shape of the view
     * Shape ==0  means no shape
     * Shape == 1 means OVAL
     */
    fun SetConstraintLayoutProperties(
        view: View?,
        BackgroundColorOfTheView: Int,
        CornerRadiusOfTheView: Float,
        Shape: Int,
        strokeWidth: Int,
        StrokeColor: Int,
    ) {
        try {
            if (view != null && Shape == 0) {
                val gradientDrawable = GradientDrawable()
                // i_top_or_bottom ==1 means show corner on the top only
                gradientDrawable.cornerRadii = floatArrayOf(
                    CornerRadiusOfTheView, CornerRadiusOfTheView,
                    CornerRadiusOfTheView, CornerRadiusOfTheView,
                    CornerRadiusOfTheView, CornerRadiusOfTheView,
                    CornerRadiusOfTheView, CornerRadiusOfTheView
                )

                if (BackgroundColorOfTheView != 0) {
                    gradientDrawable.setColor(BackgroundColorOfTheView)
                }
                view.setBackground(gradientDrawable);
            } else if (view != null) {
                val gradientDrawable = GradientDrawable()
                // SHape == 1 means Oval Shape
                if (Shape == 1) {
                    gradientDrawable.shape = GradientDrawable.OVAL
                }
                gradientDrawable.setStroke(strokeWidth, StrokeColor)
                if (BackgroundColorOfTheView != 0) {
                    gradientDrawable.setColor(BackgroundColorOfTheView)
                }
                view.setBackground(gradientDrawable)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Set the color or the states defined
     * state_pressed,state_focused and state_activated - given Pressed color
     *
     * @param normalColor
     * @param pressedColor
     * @return - ColorStateList
     */
    fun getPressedColorSelector(normalColor: Int, pressedColor: Int): ColorStateList? {
        return try {
            ColorStateList(
                arrayOf(
                    intArrayOf(R.attr.state_pressed), intArrayOf(R.attr.state_focused), intArrayOf(
                        R.attr.state_activated
                    ), intArrayOf()
                ), intArrayOf(
                    pressedColor,
                    pressedColor,
                    pressedColor,
                    normalColor
                )
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }


}