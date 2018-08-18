# TapToView

> 一个用于手指短按触发，松开释放的Library。有效解决与父级View的事件冲突
- 效果图

v1.0.1 加入揭露效果

![preview0](screenshot/0.gif)

![preview1](screenshot/1.gif)

![preview2](screenshot/2.gif)

![preview3](screenshot/3.gif)

## 使用步骤
### 引用
Step 1.Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
Step 2. Add the dependency

```groovy
dependencies {
    implementation 'com.github.Vove7:TapToView:1.0.1'
}
```
### 使用
**这里均使用Kotlin**

***就不客气了，直接上代码***
- Custom View
```kotlin

class MainActivity : AppCompatActivity(), OnTapEvent {
    lateinit var btn: Button

    lateinit var popW: PopupWindow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn = findViewById(R.id.button)

        //注意tapEvent
        Tap2View(targetView = btn, tapEvent = this).register()

    }

    override fun onTapSuccess(tap2View: Tap2View, triggerX: Int, triggerY: Int) {
        popW = PopUtil.createPopCard(this, "I am here - 😄")
        popW.animationStyle = R.style.pop_anim
        popW.contentView.measure(100, 100)
        popW.showAtLocation(btn2, Gravity.TOP or Gravity.START, 0, 0)
        //...
        Thread.sleep(300)
        updatePop(triggerX, triggerY)
    }

    override fun onClick(v: View, tap2View: Tap2View) {
        //Vog.d(this, "点击")
    }

    override fun onMove(tap2View: Tap2View, event: MotionEvent, dx: Double, dy: Double) {
        updatePop(event.rawX.toInt(), event.rawY.toInt())
     
    }

    override fun onTapRelease(tap2View: Tap2View) {
        popW.dismiss()
    }

    fun updatePop(x: Int, y: Int) {
        popW.update(x - (popW.contentView.width / 2),
                y - popW.contentView.height - 20,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
```
- ListView
ListAdapter
```kotlin
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
                        popW!!.animationStyle = R.style.pop_anim
                        popW!!.showAtLocation(listView, Gravity.TOP or Gravity.START,
                                triggerX - (popW!!.contentView.measuredWidth / 2),
                                triggerY - popW!!.contentView.measuredHeight - 50)
                    }
                    R.id.img1 -> {
                        popW = PopUtil.createPreViewImageAndShow(this@ListActivity, listView, getDrawable(R.drawable.a)
                                , fingerPoint = Point(triggerX, triggerY))
                    }
                }
//                Toast.makeText(this@ListActivity, "触发", Toast.LENGTH_SHORT).show()
            }

            override fun onTapCancel(position: Int, tap2View: Tap2View) {
                Toast.makeText(this@ListActivity, "let it go", Toast.LENGTH_SHORT).show()
            }

            override fun onTapRelease(position: Int, tap2View: Tap2View) {
                popW?.dismiss()
//                Toast.makeText(this@ListActivity, "释放", Toast.LENGTH_SHORT).show()
            }

            override fun onMove(position: Int, tap2View: Tap2View, event: MotionEvent, dx: Double, dy: Double) {
                when (tap2View.targetView.id) {
                    R.id.img -> {
                        Vog.d(this, "dx: $dx dy:$dy")
//                popW.contentView.alpha = abs(dx / dy).toFloat()
                        val p = popW!!
                        p.update(event.rawX.toInt() - (p.contentView.width / 2),
                                event.rawY.toInt() - p.contentView.height - 20,
                                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    }
                    R.id.img1 -> {

                    }
                }
            }
        }, listView = listView)
    }
```

```kotlin
class DemoListAdapter(private val context: Context, dataSet: List<Data>,
                      private val itemTapEvent: OnItemTapEvent, val listView: ListView) :
        BaseListAdapter<DemoHolder, Data>(context, dataSet) {

    override fun onCreateViewHolder(parent: ViewGroup): DemoHolder {
        return DemoHolder(inflater.inflate(R.layout.demo_list_item, null))
    }

    override fun onBindView(holder: DemoHolder, pos: Int, item: Data) {
        holder.textView.text = item.text
        holder.imgView1.setImageResource(item.imgId)

        Tap2View(
                targetView = holder.imgView,
                position = pos,
                itemTapEvent = itemTapEvent,
                delayTime = 150
        ).register()
        Tap2View(
                targetView = holder.imgView1,
                position = pos,
                itemTapEvent = itemTapEvent,
                delayTime = 150
        ).register()


    }
    }
}

class DemoHolder(itemView: View) : BaseListAdapter.ViewHolder(itemView) {
    var textView: TextView = itemView.findViewById(R.id.text)
    var imgView: ImageView = itemView.findViewById(R.id.img)
    var imgView1: ImageView = itemView.findViewById(R.id.img1)
}

class Data(var text: String, var imgId: Int)
```
# PopUtil
```kotlin
package cn.vove7.tap2view

object PopUtil {
    /**
     * Create a PopupWindow with a layoutRes
     */
    fun createPop(context: Context, @LayoutRes resId: Int, w: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
                  h: Int = ViewGroup.LayoutParams.WRAP_CONTENT): PopupWindow 
   
    /**
     * Create a PopupWindow with a View
     */
    fun createPop(contentView: View, w: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
                  h: Int = ViewGroup.LayoutParams.WRAP_CONTENT): PopupWindow 
   
    /**
     * Create a PopupWindow
     * which like @Toast with @CardView
     */
    fun createPopCard(context: Context, content: String): PopupWindow 
   
    /**
     * gasBlur : 背景使用高斯模糊
     * background 背景 Activity==null时启用
     */
    fun createPreViewImageAndShow(context: Context, parent: View, previewDrawable: Drawable,
                                  gasBlur: Boolean = true, background: Drawable? = null,
                                  cirReveal: Boolean = true, fingerPoint: Point? = null): PopupWindow
}
```
# 已知 'Bug'
- 1、同时触摸注册Tap2View事件的多个View就会挂挂。如有需求，各位可自行加'锁'控制。😄
