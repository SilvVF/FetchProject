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
                    // 5MB
                    val cacheSizeBytes = 5_000_000L
                    cache(Cache(File(context.cacheDir, "net_cache"), cacheSizeBytes) )
                }
            }
            install(ContentNegotiation) {
                json(json)
            }
        }
    }

    val fetchApi by lazy { FetchApi(client) }
}