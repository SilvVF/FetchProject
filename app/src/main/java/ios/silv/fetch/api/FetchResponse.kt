package ios.silv.fetch.api


import ios.silv.fetch.api.FetchResponse.FetchResponseItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class FetchResponse : ArrayList<FetchResponseItem>() {
    @Serializable
    data class FetchResponseItem(
        @SerialName("id")
        val id: Int,
        @SerialName("listId")
        val listId: Int,
        @SerialName("name")
        val name: String?
    )
}