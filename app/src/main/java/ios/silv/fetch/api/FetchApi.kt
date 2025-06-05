package ios.silv.fetch.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ios.silv.fetch.suspendRunCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchApi(
    val client: HttpClient,
) {

    suspend fun get(): Result<FetchResponse> = suspendRunCatching {
        withContext(Dispatchers.IO) {
            client
                .get(urlString = "https://hiring.fetch.com/hiring.json")
                .body<FetchResponse>()
        }
    }
}