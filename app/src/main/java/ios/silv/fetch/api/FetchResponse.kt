package ios.silv.fetch.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias FetchResponse = List<FetchResponseItem>

@Serializable
data class FetchResponseItem(
    @SerialName("id")
    val id: Int,
    @SerialName("listId")
    val listId: Int,
    @SerialName("name")
    val name: String?
)