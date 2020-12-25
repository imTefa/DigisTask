package com.a.digistask.ui

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar

/**
 *
 *Created by Atef on 25/12/20
 *
 */
class UIUtils {}

@BindingAdapter("floatText")
fun TextView.setFloatText(value: Float?) {
    text = value?.toString() ?: ""
}

@BindingAdapter("progressColor")
fun setProgressColor(view: TextRoundCornerProgressBar, res: Int?) {
    if (res != null)
        view.progressColor = ContextCompat.getColor(view.context, res)
}

@BindingAdapter("rsrpProgress")
fun setRsrpProgress(view: TextRoundCornerProgressBar, value: Float?) {
    if (value != null)
        view.progress = getRSRPProgress(value)
}

@BindingAdapter("rsrqProgress")
fun setRsrqProgress(view: TextRoundCornerProgressBar, value: Float?) {
    if (value != null)
        view.progress = getRSRQProgress(value)
}

@BindingAdapter("sinrProgress")
fun setSinrProgress(view: TextRoundCornerProgressBar, value: Float?) {
    if (value != null)
        view.progress = getSINRProgress(value)
}


//Misunderstanding requirements
// so dummy progress

//Consider range from -150 to 0
fun getRSRPProgress(rsrp: Float) = ((rsrp + 150) / 150) * 100

//Consider range from -50 to 50
fun getRSRQProgress(rsrq: Float) = ((rsrq + 50) / 100) * 100

//Consider range from -30 to 70
fun getSINRProgress(sinr: Float) = ((sinr + 30) / 100) * 100