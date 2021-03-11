package com.android.misfinanzas.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.misfinanzas.databinding.LayoutTitleTextGroupViewBinding

class TitleTextGroupView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var binding: LayoutTitleTextGroupViewBinding

    init {
        inflate()
    }

    private fun inflate() {
        binding = LayoutTitleTextGroupViewBinding.inflate(LayoutInflater.from(context), this)
    }

    fun setupBody(title: String, text: String, limitLines: Boolean = true) = with(binding) {
        tvTitle.text = title
        tvText.text = text
        if (!limitLines) {
            tvText.maxLines = Integer.MAX_VALUE
        }
    }

}
