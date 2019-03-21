package cn.vove7.taptoview

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import cn.vove7.tap2view.OnTapEvent
import cn.vove7.tap2view.PopUtil
import cn.vove7.tap2view.Tap2View
import cn.vove7.tap2view.Vog

class MainActivity : AppCompatActivity(), OnTapEvent {
    lateinit var textView: TextView
    lateinit var btn: Button
    lateinit var btn2: Button

    lateinit var popW: PopupWindow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView2)
        btn = findViewById(R.id.button)
        btn2 = findViewById(R.id.button2)

        Tap2View(targetView = textView, delayTime = 100).register()
        Tap2View(targetView = btn, tapEvent = this).register()
        Tap2View(targetView = btn2, tapEvent = this, delayTime = 100).register()

    }

    override fun onTapSuccess(tap2View: Tap2View, triggerX: Int, triggerY: Int) {
        when (tap2View.targetView) {
            btn -> {
                startActivity(Intent(this@MainActivity, ListActivity::class.java))
            }
            btn2 -> {
                popW = PopUtil.createPopCard(this, "出来啦 - 😄")
                popW.animationStyle = R.style.pop_anim
                popW.contentView.measure(100, 100)
                popW.showAtLocation(btn2, Gravity.TOP or Gravity.START, 0, 0)
                Thread.sleep(300)
                updatePop(triggerX, triggerY)
            }
        }
    }

    override fun onClick(v: View, tap2View: Tap2View) {
        Vog.d(this, "点击")
    }

    override fun onMove(tap2View: Tap2View, event: MotionEvent, dx: Double, dy: Double) {

        when (tap2View.targetView) {
            btn2 -> {
                updatePop(event.rawX.toInt(), event.rawY.toInt())
            }
        }
    }

    override fun onTapRelease(tap2View: Tap2View) {
        when (tap2View.targetView) {
            btn2 -> {
                popW.dismiss()
            }
        }
    }

    fun updatePop(x: Int, y: Int) {
        popW.update(x - (popW.contentView.width / 2),
                y - popW.contentView.height - 20,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
