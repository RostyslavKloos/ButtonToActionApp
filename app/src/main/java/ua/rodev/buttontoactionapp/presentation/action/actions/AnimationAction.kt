package ua.rodev.buttontoactionapp.presentation.action.actions

import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import ua.rodev.buttontoactionapp.presentation.action.ActionUi

class AnimationAction(
    private val button: Button,
    private val duration: Long = DURATION,
) : ActionUi {

    private val linearInterpolator = LinearInterpolator()

    override fun perform() {
        val rotate = RotateAnimation(
            FROM_DEGREES,
            TO_DEGREES,
            Animation.RELATIVE_TO_SELF,
            PIVOT_X_VALUE,
            Animation.RELATIVE_TO_SELF,
            PIVOT_X_VALUE
        )
        rotate.duration = duration
        rotate.interpolator = linearInterpolator
        button.startAnimation(rotate)
    }

    companion object {
        private const val FROM_DEGREES = 0f
        private const val TO_DEGREES = 360f
        private const val PIVOT_X_VALUE = 0.5f
        private const val DURATION = 2000L
    }
}
