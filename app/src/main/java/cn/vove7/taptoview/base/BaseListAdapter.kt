package cn.vove7.taptoview.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

@Suppress("UNCHECKED_CAST")
abstract class BaseListAdapter<T : BaseListAdapter.ViewHolder, DataType>
(context: Context, private val dataSet: List<DataType>?) : BaseAdapter() {
    var inflater: LayoutInflater = LayoutInflater.from(context)

    protected abstract fun onCreateViewHolder(parent: ViewGroup): T

    override fun getCount(): Int {
        return dataSet?.size ?: 0
    }

    protected abstract fun onBindView(holder: T, pos: Int, item: DataType)

    override fun getItem(position: Int): DataType {
        return dataSet!![position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: T
        if (view == null) {
            holder = onCreateViewHolder(parent)
            view = holder.itemView
            view.tag = holder
        } else {
            holder = view.tag as T
        }
        onBindView(holder, position, getItem(position))
        return view
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    abstract class ViewHolder(var itemView: View)
}
