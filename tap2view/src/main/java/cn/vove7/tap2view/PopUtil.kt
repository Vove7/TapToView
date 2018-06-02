package cn.vove7.tap2view

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import jp.wasabeef.glide.transformations.BlurTransformation


object PopUtil {
    /**
     * Create a PopupWindow with a layoutRes
     */
    fun createPop(context: Context, @LayoutRes resId: Int, w: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
                  h: Int = ViewGroup.LayoutParams.WRAP_CONTENT): PopupWindow {
        val contentView = LayoutInflater.from(context).inflate(resId, null)
        return createPop(contentView, w, h)
    }

    /**
     * Create a PopupWindow with a View
     */
    fun createPop(contentView: View, w: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
                  h: Int = ViewGroup.LayoutParams.WRAP_CONTENT): PopupWindow {
        val popW = PopupWindow(contentView, w, h, false)
        popW.contentView = contentView
        return popW
    }

    /**
     * Create a PopupWindow
     * which like @Toast with @CardView
     */
    fun createPopCard(context: Context, content: String): PopupWindow {
        val contentView = LayoutInflater.from(context).inflate(R.layout.pop_card, null)
        contentView.findViewById<TextView>(R.id.message).text = content
        return createPop(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    /**
     * gasBlur : 背景使用高斯模糊
     * background 背景 Activity==null时启用
     */
    fun createPreViewImage(context: Context, previewDrawable: Drawable,
                           gasBlur:Boolean = true, background: Drawable? = null): PopupWindow {
        val contentView = LayoutInflater.from(context).inflate(R.layout.pop_preview_image, null)

        val previewHolder = contentView.findViewById<ImageView>(R.id.preview_holder)

        if (gasBlur) {
            val simpleTarget = object : SimpleTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    contentView.background = resource
                }
            }
            Glide.with(context).load(AppUtil.activityShot(context as Activity))
                    .apply(bitmapTransform(BlurTransformation(45, 3)))
                    .into(simpleTarget)
        } else if (background != null) {
            contentView.background = background
        }

        previewHolder.setImageDrawable(previewDrawable)
        val p = createPop(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        p.animationStyle = R.style.pop_anim
        return p
    }

}