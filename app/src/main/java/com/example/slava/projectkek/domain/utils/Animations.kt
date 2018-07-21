package com.example.slava.projectkek.domain.utils

import android.view.View

import kotlinx.android.synthetic.main.activity_main.*

object Animations {
    fun setAlpha(v: View, alpha: Float){
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

    fun showFromBottom(v: View, black_screen_container: View) {
        v.visibility = View.VISIBLE
        val animator = v.animate()
        animator.translationY(0F).duration = 200

        setAlpha(black_screen_container, 0.6f)

    }

    fun hideFromBottom(v: View, pos: Float, black_screen_container: View) {
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