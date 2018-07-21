package com.example.slava.projectkek.domain.utils

import android.view.MotionEvent
import android.view.View


class Animations(menu: View , black_screen_container: View) {
    private var toTop = 0F
    private var ignoreTouch = false
    private var wereGesture = false
    private var firstTouch = 0F
    private var positionMenu = 0F

    var listener = View.OnTouchListener(function = {_, motionEvent ->

        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
            toTop = motionEvent.rawY - menu.y
            ignoreTouch = false
            wereGesture = false
            firstTouch = motionEvent.rawY
            positionMenu = menu.y
        }

        if (motionEvent.action == MotionEvent.ACTION_MOVE) {
            if (!ignoreTouch) {
                wereGesture = true
                if (motionEvent.rawY - toTop >= black_screen_container.height - menu.height) {
                    menu.y = motionEvent.rawY - toTop
                } else {
                    menu.y = (black_screen_container.height - menu.height).toFloat()
                }
            }
        }

        if (motionEvent.action == MotionEvent.ACTION_UP){
            if (motionEvent.rawY - firstTouch > 50) {
                hideFromBottom(menu, positionMenu, black_screen_container)
                wereGesture = false
            } else {
                showFromBottom(menu, black_screen_container)
            }
        }

        true
    })

    fun setAlpha(v: View, alpha: Float){
        if (alpha == 0.6f)
            v.elevation = 1f

        val animator = v.animate()
        animator.alpha(alpha).duration = 200
        if (alpha == 0f)
            animator.withEndAction {
                v.elevation = -1f
            }
        else
            animator.withEndAction {
                v.elevation = 1f
            }
    }

    private fun showFromBottom(v: View, black_screen_container: View) {
        v.visibility = View.VISIBLE
        val animator = v.animate()
        animator.translationY(0F).duration = 200

        setAlpha(black_screen_container, 0.6f)

    }

    private fun hideFromBottom(v: View, pos: Float, black_screen_container: View) {
        val animator = v.animate()
        animator.translationY(v.y)
                .setDuration(200)
                .withEndAction {
                    v.y = pos
                    v.visibility = View.INVISIBLE
                }

        setAlpha(black_screen_container, 0f)

    }
}