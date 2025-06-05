package ios.silv.fetch

import android.app.Application
import android.content.Context
import ios.silv.fetch.di.AppGraph
import ios.silv.fetch.di.appGraph

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        appGraph = object : AppGraph() {
            override val context: Context
                get() = this@App
        }
    }
}