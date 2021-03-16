package com.android.misfinanzas.utils.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.misfinanzas.R
import com.android.misfinanzas.databinding.DialogInfoBinding
import com.android.misfinanzas.utils.gone
import com.android.misfinanzas.utils.visible

class CustomDialog(
    context: Context,
    private val builder: Builder
) : Dialog(context) {

    class Builder(private val context: Context) {
        var dialogType: DialogType? = null
            private set
        var title: String? = null
            private set
        var description: String? = null
            private set
        var descriptionGravity: Int = Gravity.START
            private set
        var actionText: String? = null
            private set
        var cancelText: String? = null
            private set
        var action: (() -> Unit?)? = null
            private set
        var cancelAction: (() -> Unit?)? = null
            private set
        var screenPercent: Double = context.getString(R.string.dialog_percent).toDouble()
            private set
        var screenPosition: Int = Gravity.BOTTOM
            private set
        var icon: Int? = null
            private set

        fun dialogType(dialogType: DialogType) = apply { this.dialogType = dialogType }
        fun title(title: String) = apply { this.title = title }
        fun description(description: String) = apply { this.description = description }
        fun descriptionGravity(gravity: Int) = apply { this.descriptionGravity = gravity }
        fun actionText(actionText: String) = apply { this.actionText = actionText }
        fun action(action: () -> Unit) = apply { this.action = action }
        fun cancelText(cancelText: String) = apply { this.cancelText = cancelText }
        fun cancelAction(cancelAction: () -> Unit) = apply { this.cancelAction = cancelAction }
        fun screenPercent(screenPercent: Double) = apply { this.screenPercent = screenPercent }
        fun screenPosition(screenPosition: Int) = apply { this.screenPosition = screenPosition }
        fun icon(@DrawableRes icon: Int) = apply { this.icon = icon }
        fun build() = CustomDialog(context, this)
    }

    private lateinit var binding: DialogInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogInfoBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setupView()
        setupEvents()
        setDialogWidth()
        setDialogScreenPosition()
    }

    private fun setupView() = with(binding) {
        when (builder.dialogType) {
            DialogType.ALERT_DIALOG -> tvTitle.setTextColor(context.getColor(android.R.color.black))
            DialogType.ERROR_DIALOG -> tvTitle.setTextColor(context.getColor(android.R.color.holo_red_dark))
            DialogType.SUCCESS_DIALOG -> tvTitle.setTextColor(context.getColor(android.R.color.black))
        }

        if (builder.icon == null) {
            ivIcon.gone()
        } else {
            ivIcon.setImageResource(builder.icon!!)
            ivIcon.visible()
        }

        if (builder.title.isNullOrEmpty()) {
            tvTitle.gone()
        } else {
            tvTitle.visible()
            tvTitle.text = builder.title
        }

        if (builder.description.isNullOrEmpty()) {
            tvDescription.gone()
        } else {
            tvDescription.visible()
            tvDescription.text = builder.description
            tvDescription.gravity = builder.descriptionGravity
        }

        if (builder.actionText.isNullOrEmpty()) {
            btnAction.gone()
        } else {
            btnAction.visible()
            btnAction.text = builder.actionText
        }

        if (builder.cancelText.isNullOrEmpty()) {
            btnCancel.gone()
        } else {
            btnCancel.visible()
            btnCancel.text = builder.cancelText
        }
    }

    private fun setupEvents() = with(binding) {
        btnAction.setOnClickListener {
            builder.action?.invoke()
            dismiss()
        }

        btnCancel.setOnClickListener {
            builder.cancelAction?.invoke()
            dismiss()
        }
    }

    private fun setDialogWidth() {
        val width = (context.resources.displayMetrics.widthPixels * builder.screenPercent).toInt()
        window?.setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT)
    }

    private fun setDialogScreenPosition() {
        val wlp: WindowManager.LayoutParams? = window?.attributes
        wlp?.gravity = builder.screenPosition
        wlp?.flags = wlp?.flags?.and(WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv())
        window?.attributes = wlp
    }

    enum class DialogType { ALERT_DIALOG, ERROR_DIALOG, SUCCESS_DIALOG }

}
