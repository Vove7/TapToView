package cn.vove7.tap2view

import android.util.Log

object Vog {
    const val v = 0
    const val d = 1
    const val i = 2
    const val w = 3
    const val e = 4
    const val a = 5
    var output_level = 0
    fun init(l: Int) {
        output_level = l
    }

    fun d(o: Any, msg: String) {
        if (output_level <= d) {
            Log.d(o.javaClass.simpleName, "  ----> $msg")
        }
    }

    fun v(o: Any, msg: Any) {
        if (output_level <= v) {
            Log.v(o.javaClass.simpleName, "  ----> $msg")
        }
    }

    fun i(o: Any, msg: Any) {
        if (output_level <= i) {
            Log.i(o.javaClass.simpleName, "  ----> " + msg)
        }
    }

    fun w(o: Any, msg: Any) {
        if (output_level <= w) {
            Log.w(o.javaClass.simpleName, "  ---->" + msg)
        }
    }

    fun e(o: Any, msg: Any) {
        if (output_level <= e) {
            Log.e(o.javaClass.simpleName, "  ----> " + msg)
        }
    }

    fun a(o: Any, msg: Any) {
        if (output_level <= a)
            Log.wtf(o.javaClass.simpleName, "  ----> " + msg)
    }
}
