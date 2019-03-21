package cn.vove7.taptoview

import android.app.Application
import cn.vove7.tap2view.Vog

class VApplication:Application {
    constructor() : super()

    override fun onCreate() {
        super.onCreate()
        Vog.init(Vog.v)
    }
}