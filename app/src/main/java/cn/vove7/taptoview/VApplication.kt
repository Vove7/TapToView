package cn.vove7.taptoview

import android.app.Application
import cn.vove7.vog.Vog

class VApplication:Application {
    constructor() : super()

    override fun onCreate() {
        super.onCreate()
        Vog.init(Vog.v)
    }
}