package com.yanivsos.mixological.ui.utils

import androidx.constraintlayout.motion.widget.MotionLayout
import timber.log.Timber

open class MyTransitionListener :
    MotionLayout.TransitionListener {

    override fun onTransitionTrigger(
        motionLayout: MotionLayout,
        triggerId: Int,
        positive: Boolean,
        progress: Float
    ) {
    }

    override fun onTransitionStarted(motionLayout: MotionLayout, startId: Int, endId: Int) {
    }

    override fun onTransitionChange(
        motionLayout: MotionLayout,
        startId: Int,
        endId: Int,
        progress: Float
    ) {
    }

    override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
    }
}

@Suppress("unused")
open class TimberTransitionListener: MyTransitionListener() {
    override fun onTransitionTrigger(
        motionLayout: MotionLayout,
        triggerId: Int,
        positive: Boolean,
        progress: Float
    ) {
        super.onTransitionTrigger(motionLayout, triggerId, positive, progress)
        Timber.d("onTransitionTrigger: ")
    }

    override fun onTransitionStarted(motionLayout: MotionLayout, startId: Int, endId: Int) {
        super.onTransitionStarted(motionLayout, startId, endId)
        Timber.d("onTransitionStarted")
    }

    override fun onTransitionChange(
        motionLayout: MotionLayout,
        startId: Int,
        endId: Int,
        progress: Float
    ) {
        super.onTransitionChange(motionLayout, startId, endId, progress)
        Timber.d("onTransitionChange: startId[$startId], endId[$endId], progress[$progress]")
    }

    override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
        super.onTransitionCompleted(motionLayout, currentId)
        Timber.d("onTransitionCompleted: currentId[$currentId]")
    }
}
