package ios.silv.fetch.di

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import ios.silv.fetch.api.FetchApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import java.io.File

lateinit var appGraph: AppGraph

abstract class AppGraph {

    abstract val context: Context

    val json = Json {
        prettyPrint = true
    }

    val client by lazy {
        HttpClient(OkHttp) {
            engine {
                config {
                    cache(Cache(File(context.cacheDir, "net_cache"), 5_000L))
                }
            }
            install(ContentNegotiation) {
                json(json)
            }
        }
    }

    val fetchApi by lazy { FetchApi(client) }
}