package com.android.misfinanzas.components

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue.COMPLEX_UNIT_PX
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.misfinanzas.databinding.LayoutImageTextGroupViewBinding
import com.android.misfinanzas.utils.dpToPx

class ImageTextGroupView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var binding: LayoutImageTextGroupViewBinding

    init {
        inflate()
    }

    private fun inflate() {
        binding = LayoutImageTextGroupViewBinding.inflate(LayoutInflater.from(context), this)
    }

    fun setupBodySmallImageView(@DrawableRes imageId: Int, text: String) {
        setupBody(imageId, 24, 24, text, 12)
    }

    fun setupBodyBold(@DrawableRes imageId: Int, text: String, limitLines: Boolean = true) {
        setupBody(imageId = imageId, text = text, bold = true, limitLines = limitLines)
    }

    fun setupBody(
        @DrawableRes imageId: Int,
        imageWidth: Int? = null,
        imageHeight: Int? = null,
        text: String,
        textSize: Int = 18,
        limitLines: Boolean = true,
        bold: Boolean = false
    ) = with(binding) {
        ivImage.setImageResource(imageId)
        imageWidth?.let { ivImage.layoutParams.width = context.dpToPx(it) }
        imageHeight?.let { ivImage.layoutParams.height = context.dpToPx(it) }
        tvText.text = text
        tvText.setTextSize(COMPLEX_UNIT_PX, context.dpToPx(textSize).toFloat())
        if (bold) {
            tvText.setTypeface(null, Typeface.BOLD)
        }
        if (!limitLines) {
            tvText.maxLines = Integer.MAX_VALUE
        }
    }

}
