package com.esteban.postsapp.ui.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.display.DisplayManager
import android.view.Display
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.esteban.postsapp.R
import kotlin.math.abs

class SwipeToFavDeleteCallback(
    val context: Context,
    val listener: Listener
) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    interface Listener {
        fun onSwipedRight(position: Int)
        fun onSwipedLeft(position: Int)
    }


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = true

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.absoluteAdapterPosition

        when (direction) {
            ItemTouchHelper.RIGHT -> listener.onSwipedRight(position)
            ItemTouchHelper.LEFT -> listener.onSwipedLeft(position)
        }
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        val defaultDisplay =
            ContextCompat.getSystemService(context, DisplayManager::class.java)?.getDisplay(
                Display.DEFAULT_DISPLAY
            )

        val width = defaultDisplay?.width ?: 0

        var background = ColorDrawable(context.resources.getColor(R.color.background_gray))
        val itemView = viewHolder.itemView
        val backgroundCornerOffset: Int = 20

        when {
            abs(dX) < width / 3 -> {
                background.setBounds(0, 0, 0, 0)
            }
            dX > width / 3 -> {
                background = ColorDrawable(Color.GREEN)
                background.setBounds(
                    itemView.left,
                    itemView.top,
                    itemView.left + dX.toInt() + backgroundCornerOffset,
                    itemView.bottom
                )
            }
            else -> {
                background = ColorDrawable(Color.RED)
                background.setBounds(
                    itemView.right + dX.toInt() - backgroundCornerOffset,
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
            }
        }
        background.draw(canvas)

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}