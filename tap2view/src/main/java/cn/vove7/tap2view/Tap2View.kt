package cn.vove7.tap2view

import android.os.Handler
import android.view.MotionEvent
import android.view.View
import cn.vove7.vog.Vog
import java.lang.Math.abs

/**
 * Tap2View
 */
class Tap2View(
        val targetView: View,
        private var tapEvent: OnTapEvent? = null,

        private var itemTapEvent: OnItemTapEvent? = null,
        /**
         * item position of list
         */
        private var position: Int = 0,
        /**
         * time of trigger long click
         */
        private var delayTime: Long = DEFAULT_DELAY_TIME,
        /**
         * 取消后是否响应至父级View
         * clickParentIfCanceled 为true,则不执行tapEvent/itemTapEvent 的onClick事件
         */
        private var clickParentIfCanceled: Boolean = false) {

//    private var performClick = true

    private var isLongClick = false

    companion object {
        const val MOVE_PRECISION = 30
        const val DEFAULT_DELAY_TIME = 500.toLong()
        /*fun with(context: Context): Tap2View {
            return Tap2View(context)
        }*/
    }

    fun register(): Tap2View {
        targetView.setOnClickListener {
            if (isLongClick || clickParentIfCanceled) {//触发长按|点击父级，不再响应此View点击事件
                return@setOnClickListener
            }
            Vog.d(this, "点击")
            tapEvent?.onClick(it, this)
            itemTapEvent?.onClick(position, it, this)
        }
        registerEvent()
        return this
    }


    /**
     * 暂停父级拦截事件
     */
    private fun pauseParentIntercept(targetView: View) {
        requestDisIntercept(targetView, true)
    }

    /**
     * 恢复父级拦截事件
     */
    private fun resumeParentIntercept(targetView: View) {
        requestDisIntercept(targetView, false)
    }

    /**
     *
     */
    private fun requestDisIntercept(targetView: View, disAllow: Boolean) {
        var par = targetView.parent
        while (par != null) {
            par.requestDisallowInterceptTouchEvent(disAllow)
            par = par.parent
        }
    }

    /**
     * 注册事件
     */
    private fun registerEvent() {

        var downX = 0.0
        var downY = 0.0
        var lastX = 0.0
        var lastY = 0.0
        val longHandler = Handler()
        val longClickRun = Runnable {
            isLongClick = true
            Vog.d(this, "长按")
            pauseParentIntercept(targetView)
            tapEvent?.onTapSuccess(this, lastX.toInt(), lastY.toInt())
            itemTapEvent?.onTapSuccess(position, this, lastX.toInt(), lastY.toInt())
        }
        var cancel = false

        targetView.setOnTouchListener({ _, event ->
            // Vog.d(this, "x: ${event.x}/${event.rawX} \t y:${event.y}/${event.rawY}")
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    Vog.v(this, "ACTION_UP")
                    when {
                        cancel -> {//已取消- don't consume (不会进入)
                            Vog.d(this, "取消-抬手事件")
                            return@setOnTouchListener false
                        }
                        isLongClick -> { //触发长按-抬手事件
                            Vog.d(this, "长按-抬手事件")
                            resumeParentIntercept(targetView)
                            tapEvent?.onTapRelease(this)
                            itemTapEvent?.onTapRelease(position, this)

                            return@setOnTouchListener false
                            //false->调用点击事件
                            //true->拦截
                        }
                        else -> {//未触发长按-抬手事件- don't consume
                            longHandler.removeCallbacks(longClickRun)
                            Vog.d(this, "未长按-抬手事件")
                            if (clickParentIfCanceled) {
                                Vog.d(this, "点击父级")
                                (targetView.parent as View).performClick()
                            }
                            return@setOnTouchListener false
                        }
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    Vog.d(this, "ACTION_CANCEL")
                    if (!isLongClick) {
                        cancel = true
                        tapEvent?.onTapCancel(this)
                        itemTapEvent?.onTapCancel(position, this)
                        longHandler.removeCallbacks(longClickRun)
                        return@setOnTouchListener false
                    }
                    //else

                }
                MotionEvent.ACTION_MOVE -> {
                    Vog.d(this, "ACTION_MOVE")
                    if (cancel) {
                        Vog.d(this, "give up")
                        return@setOnTouchListener false
                    } else if (isLongClick) {
//                            Vog.d(this, "长按-Move")
                        tapEvent?.onMove(this, event, event.rawX - downX, event.rawY - downY)
                        itemTapEvent?.onMove(position, this, event, event.rawX - downX, event.rawY - downY)
                        return@setOnTouchListener true
                    }
                    lastX = event.rawX.toDouble()
                    lastY = event.rawY.toDouble()
                    if (!isLongClick && !isBigMove(lastX, lastY, downX, downY)) {//未触发长按，超出范围
                        cancel = true
                        tapEvent?.onTapCancel(this)
                        itemTapEvent?.onTapCancel(position, this)
                        longHandler.removeCallbacks(longClickRun)
                        return@setOnTouchListener false
                    }
                }
                MotionEvent.ACTION_DOWN -> {
//                    performClick = true
                    cancel = false
                    isLongClick = false
                    lastX = event.rawX.toDouble()
                    lastY = event.rawY.toDouble()
                    downX = lastX
                    downY = lastY
                    targetView.onTouchEvent(event)
                    longHandler.postDelayed(longClickRun, delayTime)
                    Vog.d(this, "ACTION_DOWN")
                }
                else -> {
                    Vog.d(this, "else ${event.action}")
                }
            }
            true
        })
    }

    private fun isBigMove(a: Double, b: Double, c: Double, d: Double): Boolean {
        return abs(a - c) <= MOVE_PRECISION && abs(b - d) <= MOVE_PRECISION
    }

}

interface OnItemTapEvent {

    fun onTapSuccess(position: Int, tap2View: Tap2View, triggerX: Int, triggerY: Int)

    fun onClick(position: Int, v: View, tap2View: Tap2View)

    fun onTapCancel(position: Int, tap2View: Tap2View)

    fun onTapRelease(position: Int, tap2View: Tap2View)

    fun onMove(position: Int, tap2View: Tap2View, event: MotionEvent, dx: Double, dy: Double)
}

interface OnTapEvent {
    /**
     * 长按-触发成功
     */
    fun onTapSuccess(tap2View: Tap2View, triggerX: Int, triggerY: Int)

    /**
     * clickParentIfCanceled 为true,则不执行
     * 点击
     */
    fun onClick(v: View, tap2View: Tap2View)

    /**
     * 触发失败
     */
    fun onTapCancel(tap2View: Tap2View) {}

    /**
     * 触发成功，释放后的操作
     */
    fun onTapRelease(tap2View: Tap2View)

    /**
     * 触发成功，移动操作
     */
    fun onMove(tap2View: Tap2View, event: MotionEvent, dx: Double, dy: Double) {}

}