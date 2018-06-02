package cn.vove7.taptoview.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import cn.vove7.tap2view.OnItemTapEvent
import cn.vove7.tap2view.Tap2View
import cn.vove7.taptoview.R
import cn.vove7.taptoview.base.BaseListAdapter

class DemoListAdapter(private val context: Context, dataSet: List<Data>,
                      private val itemTapEvent: OnItemTapEvent, val listView: ListView) :
        BaseListAdapter<DemoHolder, Data>(context, dataSet) {

    override fun onCreateViewHolder(parent: ViewGroup): DemoHolder {
        return DemoHolder(inflater.inflate(R.layout.demo_list_item, null))
    }

    override fun onBindView(holder: DemoHolder, pos: Int, item: Data) {
        holder.textView.text = item.text
        holder.imgView.setImageResource(item.imgId)
        holder.imgView1.setImageResource(item.imgId)

        Tap2View(
                targetView = holder.imgView,
                position = pos,
                itemTapEvent = itemTapEvent,
                delayTime = 400
        ).register()
        Tap2View(
                targetView = holder.imgView1,
                position = pos,
                itemTapEvent = itemTapEvent,
                delayTime = 400
        ).register()


    }
}

class DemoHolder(itemView: View) : BaseListAdapter.ViewHolder(itemView) {
    var textView: TextView = itemView.findViewById(R.id.text)
    var imgView: ImageView = itemView.findViewById(R.id.img)
    var imgView1: ImageView = itemView.findViewById(R.id.img1)
}

class Data(var text: String, var imgId: Int)