package com.shaji.kotlina.view.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Spanned
import android.text.SpannedString
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.transition.Slide
import com.shaji.kotlina.R
import com.shaji.kotlina.framework.generic.OnDialogClickListener
import kotlinx.android.synthetic.main.dialog_base.*

/**
 * Created by Shafik on 1/17/2015.
 */
class MaterialDialog(internal val context: Context, private val mBuilder: Builder) : Dialog(context, R.style.AppTheme_Dialog_Translucent) {

    private var onDialogCreatedListener: OnDialogCreatedListener? = null

    var message: String
        get() = mBuilder.message.toString()
        set(message) = this.setMessage(SpannedString(message))

    var title: String?
        get() = mBuilder.title.toString()
        set(title) {
            this.mBuilder.title = SpannedString(title)
            if (title == null)
                tvTitle.visibility = View.GONE
            else {
                tvTitle.visibility = View.VISIBLE
                tvTitle.text = title
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_base)

        clContent.setOnClickListener{

        }

        clRoot.setOnClickListener {
            if (mBuilder.cancelOnTouchOutside) {
                dismiss()
            }
        }

        if (mBuilder.onDismissListener != null) {
            setOnDismissListener(mBuilder.onDismissListener)
        }

        if (mBuilder.onCancelListener != null) {
            setOnCancelListener(mBuilder.onCancelListener)
        }

        tvTitle.gravity = mBuilder.titleGravity
        if (mBuilder.title != null) {
            tvTitle.visibility = View.VISIBLE
            tvTitle.text = mBuilder.title
        } else {
            tvTitle.visibility = View.GONE
        }

        tvMessage.gravity = mBuilder.messageGravity
        if (mBuilder.message != null) {
            tvMessage.visibility = View.VISIBLE
            tvMessage.text = mBuilder.message
        } else {
            tvMessage.visibility = View.GONE
        }

        var hasCustomView = false
        val customView: View?
        if (mBuilder.customView != null) {
            customView = mBuilder.customView
            hasCustomView = true
        } else if (mBuilder.customLayoutId > 0) {
            customView = LayoutInflater.from(context).inflate(mBuilder.customLayoutId, null)
            hasCustomView = true
        } else {
            customView = null
            hasCustomView = false
        }

        val custom = findViewById<FrameLayout>(R.id.flCustom)
        if (hasCustomView) {
            custom.addView(customView, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))
            tvMessage.visibility = View.GONE
        } else {
            custom.visibility = View.GONE
        }

        btnPositive.text = mBuilder.positiveBtnText
        if (mBuilder.showPositiveBtn) {
            btnPositive.setOnClickListener { v ->
                if (mBuilder.positiveOnClickListener != null) {
                    if (mBuilder.positiveOnClickListener!!.invoke(this@MaterialDialog, v.id)) dismiss()
                } else {
                    dismiss()
                }
            }
        } else {
            btnPositive.visibility = View.GONE
        }

        btnNegative.text = mBuilder.negativeBtnText
        if (mBuilder.showNegativeBtn) {
            btnNegative.setOnClickListener { v ->
                if (mBuilder.negativeOnClickListener != null) {
                    if (mBuilder.negativeOnClickListener!!.invoke(this@MaterialDialog, v.id)) dismiss()
                } else {
                    dismiss()
                }
            }
        } else {
            btnNegative.visibility = View.GONE
        }

        btnCancel.text = mBuilder.cancelBtnText
        if (mBuilder.showCancelBtn) {
            btnCancel.setOnClickListener { v ->
                if (mBuilder.cancelOnClickListener != null) {
                    if (mBuilder.cancelOnClickListener!!.invoke(this@MaterialDialog, v.id)) dismiss()
                } else {
                    dismiss()
                }
            }
        } else {
            btnCancel.visibility = View.GONE
        }

        btnNeutral.text = mBuilder.neutralBtnText
        if (mBuilder.showNeutralBtn) {
            btnNeutral.setOnClickListener { v ->
                if (mBuilder.neutralOnClickListener != null) {
                    if (mBuilder.neutralOnClickListener!!.invoke(this@MaterialDialog, v.id)) dismiss()
                } else {
                    dismiss()
                }
            }
        } else {
            btnNeutral.visibility = View.GONE
        }

        setCanceledOnTouchOutside(mBuilder.cancelOnTouchOutside)

        if (onDialogCreatedListener != null) onDialogCreatedListener!!.onDialogCreated(this)
    }

    override fun show() {
        super.show()
        // set dialog enter animations
        clContent.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_main_show_amination))
        clRoot.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_root_show_amin))
    }

    fun setMessage(message: Spanned) {
        this.mBuilder.message = message
        tvMessage.text = message
    }

    fun setTitleGravity(@Slide.GravityFlag titleGravity: Int) {
        this.mBuilder.titleGravity = titleGravity
        tvTitle.gravity = titleGravity
    }

    fun setMessageGravity(@Slide.GravityFlag messageGravity: Int) {
        this.mBuilder.messageGravity = messageGravity
        tvMessage.gravity = messageGravity
    }

    override fun onBackPressed() {
        if (!mBuilder.isModal) {
            super.onBackPressed()
        }
    }

    fun setTitleColor(color: Int) {
        this.tvTitle.setTextColor(color)
    }

    fun setPositiveButtonText(textId: Int) {
        this.setPositiveButtonText(context.getString(textId))
    }

    fun setPositiveButtonText(text: String?) {
        var text = text
        if (text != null) {
            text = text.toUpperCase()
        } else {
            text = ""
        }
        mBuilder.positiveBtnText = text
        btnPositive.text = text
    }

    fun setPositiveButtonEnabled(enabled: Boolean) {
        if (btnPositive != null) btnPositive.isEnabled = enabled
    }

    fun setCancelButtonText(textId: Int) {
        this.setCancelButtonText(context.getString(textId))
    }

    fun setCancelButtonText(text: String?) {
        var text = text
        if (text != null) {
            text = text.toUpperCase()
        } else {
            text = ""
        }
        mBuilder.cancelBtnText = text
        btnCancel.text = text
    }

    fun setCancelButtonEnabled(enabled: Boolean) {
        if (btnCancel != null) btnCancel.isEnabled = enabled
    }

    fun setOnPositiveButtonClickListener(onAcceptButtonClickListener: View.OnClickListener) {
        if (btnPositive != null)
            btnPositive.setOnClickListener(onAcceptButtonClickListener)
    }

    fun setOnCancelButtonClickListener(onCancelButtonClickListener: View.OnClickListener) {
        if (btnCancel != null)
            btnCancel.setOnClickListener(onCancelButtonClickListener)
    }

    fun setOnDialogCreatedListener(onDialogCreatedListener: OnDialogCreatedListener) {
        this.onDialogCreatedListener = onDialogCreatedListener
    }


    override fun dismiss() {
        if (mBuilder.immediateDismiss) {
            super.dismiss()
            return
        }

        val anim = AnimationUtils.loadAnimation(context, R.anim.dialog_main_hide_amination)
        anim.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                clContent.post {
                    try {
                        if (super@MaterialDialog.isShowing()) {
                            super@MaterialDialog.dismiss()
                        }
                    } catch (e: Exception) {
                    }
                }

            }
        })
        val backAnim = AnimationUtils.loadAnimation(context, R.anim.dialog_root_hide_amin)

        clContent.startAnimation(anim)
        clRoot.startAnimation(backAnim)
    }


    class Builder(internal var mContext: Context) {
        internal var titleGravity = Gravity.CENTER_HORIZONTAL
        internal var messageGravity = Gravity.CENTER_HORIZONTAL
        internal var title: Spanned? = null
        internal var message: Spanned? = null
        internal var cancelBtnText: String? = null
        internal var neutralBtnText: String? = null
        internal var negativeBtnText: String? = null
        internal var positiveBtnText: String? = null
        internal var showCancelBtn: Boolean = false
        internal var showNeutralBtn: Boolean = false
        internal var showNegativeBtn: Boolean = false
        internal var showPositiveBtn: Boolean = false
        internal var cancelOnClickListener: OnDialogClickListener? = null
        internal var neutralOnClickListener: OnDialogClickListener? = null
        internal var negativeOnClickListener: OnDialogClickListener? = null
        internal var positiveOnClickListener: OnDialogClickListener? = null
        internal var onDismissListener: DialogInterface.OnDismissListener? = null
        internal var onCancelListener: DialogInterface.OnCancelListener? = null
        internal var cancelOnTouchOutside = true
        internal var immediateDismiss = false

        internal var customLayoutId: Int = 0
        internal var customView: View? = null

        internal var isModal: Boolean = false

        fun setTitleGravity(@Slide.GravityFlag titleGravity: Int) {
            this.titleGravity = titleGravity
        }

        fun setMessageGravity(@Slide.GravityFlag messageGravity: Int) {
            this.messageGravity = messageGravity
        }

        fun setTitle(titleId: Int): Builder {
            this.setTitle(mContext.getString(titleId))
            return this
        }

        fun setTitle(title: String): Builder {
            this.setTitle(SpannedString(title))
            return this
        }

        fun setTitle(title: Spanned): Builder {
            this.title = title
            return this
        }

        fun setMessage(messageId: Int): Builder {
            return this.setMessage(mContext.getString(messageId))
        }

        fun setMessage(message: String): Builder {
            this.message = SpannedString(message)
            return this
        }

        fun setMessage(message: Spanned): Builder {
            this.message = message
            return this
        }

        fun setModal(isModal: Boolean): Builder {
            this.isModal = isModal
            this.cancelOnTouchOutside = false
            return this
        }

        fun setCustomView(layoutId: Int): Builder {
            this.customLayoutId = layoutId
            return this
        }

        fun setCustomView(customView: View): Builder {
            customLayoutId = -1
            this.customView = customView
            return this
        }

        fun setPositiveButton(@StringRes textId: Int, onClickListener: OnDialogClickListener? = null): Builder {
            return this.setPositiveButton(this.mContext.getString(textId), onClickListener)
        }

        fun setPositiveButton(text: String?, onClickListener: OnDialogClickListener? = null): Builder {
            var text = text
            this.showPositiveBtn = true
            if (text != null) text = text.toUpperCase()
            this.positiveBtnText = text
            this.positiveOnClickListener = onClickListener

            return this
        }

        fun setCancelButton(@StringRes textId: Int, onClickListener: OnDialogClickListener? = null): Builder {
            return this.setCancelButton(this.mContext.getString(textId), onClickListener)
        }

        fun setCancelButton(text: String, onClickListener: OnDialogClickListener? = null): Builder {
            var btnText = text
            this.showCancelBtn = true
            btnText = btnText.toUpperCase()
            this.cancelBtnText = btnText
            this.cancelOnClickListener = onClickListener

            return this
        }

        fun setNegativeButton(@StringRes textId: Int, onClickListener: OnDialogClickListener? = null): Builder {
            return this.setNegativeButton(this.mContext.getString(textId), onClickListener)
        }

        fun setNegativeButton(text: String?, onClickListener: OnDialogClickListener? = null): Builder {
            var text = text
            this.showNegativeBtn = true
            if (text != null) text = text.toUpperCase()
            this.negativeBtnText = text
            this.negativeOnClickListener = onClickListener

            return this
        }

        fun setNeutralButton(@StringRes textId: Int, onClickListener: OnDialogClickListener? = null): Builder {
            return this.setNeutralButton(this.mContext.getString(textId), onClickListener)
        }

        fun setNeutralButton(text: String?, onClickListener: OnDialogClickListener? = null): Builder {
            var text = text
            this.showNeutralBtn = true
            if (text != null) text = text.toUpperCase()
            this.neutralBtnText = text
            this.neutralOnClickListener = onClickListener

            return this
        }

        fun setImmediateDismiss(immediateDismiss: Boolean): Builder {
            this.immediateDismiss = immediateDismiss
            return this
        }

        fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener): Builder {
            this.onDismissListener = onDismissListener
            return this
        }

        fun setCancelOnTouchOutside(cancelOnTouchOutside: Boolean): Builder {
            this.cancelOnTouchOutside = cancelOnTouchOutside
            return this
        }

        fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener): Builder {
            this.onCancelListener = onCancelListener
            return this
        }

        fun create(): MaterialDialog {
            return MaterialDialog(
                this.mContext,
                this
            )
        }
    }

//    interface OnClickListener {
//        /**
//         *
//         * @param dialog
//         * @param id
//         * @return whether or not to dismiss the dialog after click
//         */
//        fun onClick(dialog: MaterialDialog, id: Int): Boolean
//    }

    interface OnDialogCreatedListener {
        fun onDialogCreated(dialog: MaterialDialog)
    }
}
