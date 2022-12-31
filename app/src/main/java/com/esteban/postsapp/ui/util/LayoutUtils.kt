package com.esteban.postsapp.ui.util

import android.content.res.Resources

object LayoutUtils {

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}