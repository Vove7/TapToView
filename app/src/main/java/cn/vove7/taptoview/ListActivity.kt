package cn.vove7.taptoview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.Toast
import cn.vove7.tap2view.OnItemTapEvent
import cn.vove7.tap2view.PopUtil
import cn.vove7.tap2view.Tap2View
import cn.vove7.taptoview.adapter.Data
import cn.vove7.taptoview.adapter.DemoListAdapter
import cn.vove7.vog.Vog


class ListActivity : AppCompatActivity() {

    lateinit var listView: ListView
    lateinit var popW: PopupWindow
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        listView = findViewById(R.id.listView)
        testData()
    }


    fun testData() {
        val list = ArrayList<Data>()
        for (i in 0..20) {
            list.add(Data("123", R.mipmap.ic_launcher))
        }

        listView.adapter = DemoListAdapter(this, list, itemTapEvent = object : OnItemTapEvent {
            override fun onClick(position: Int, v: View, tap2View: Tap2View) {
                Toast.makeText(this@ListActivity, "点击$position", Toast.LENGTH_SHORT).show()
            }

            override fun onTapSuccess(position: Int, tap2View: Tap2View, triggerX: Int, triggerY: Int) {
                when (tap2View.targetView.id) {
                    R.id.img -> {
                        popW = PopUtil.createPopCard(this@ListActivity, "Hello ~ No.$position")
                        popW.animationStyle = R.style.pop_anim
                        popW.showAtLocation(listView, Gravity.TOP or Gravity.START,
                                triggerX - (popW.contentView.measuredWidth / 2),
                                triggerY - popW.contentView.measuredHeight - 50)
                    }
                    R.id.img1 -> {
                        popW = PopUtil.createPreViewImage(this@ListActivity, getDrawable(R.drawable.a))
                        popW.showAtLocation(listView, Gravity.CENTER, 0, 0)
                    }
                }
//                Toast.makeText(this@ListActivity, "触发", Toast.LENGTH_SHORT).show()
            }

            override fun onTapCancel(position: Int, tap2View: Tap2View) {
                Toast.makeText(this@ListActivity, "let it go", Toast.LENGTH_SHORT).show()
            }

            override fun onTapRelease(position: Int, tap2View: Tap2View) {
                popW.dismiss()
//                Toast.makeText(this@ListActivity, "释放", Toast.LENGTH_SHORT).show()
            }

            override fun onMove(position: Int, tap2View: Tap2View, event: MotionEvent, dx: Double, dy: Double) {
                when (tap2View.targetView.id) {
                    R.id.img -> {
                        Vog.d(this, "dx: $dx dy:$dy")
//                popW.contentView.alpha = abs(dx / dy).toFloat()
                        popW.update(event.rawX.toInt() - (popW.contentView.width / 2),
                                event.rawY.toInt() - popW.contentView.height - 20,
                                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                        )

                    }
                    R.id.img1 -> {

                    }
                }
            }
        }, listView = listView)
    }
}
