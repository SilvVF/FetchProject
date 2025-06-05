package ios.silv.fetch.list

import androidx.compose.runtime.Stable
import ios.silv.fetch.api.FetchResponseItem

@Stable
sealed interface ListViewState {
    data object Loading : ListViewState
    data class Error(val message: String) : ListViewState
    data class Done(
        val data: Map<Int, List<ListItem>>
    ) : ListViewState
}

@Stable
data class ListItem(
    val id: Int,
    val listId: Int,
    val name: String,
) {

    val key = "${id}_${listId}"

    constructor(item: FetchResponseItem): this(
        id = item.id,
        listId = item.listId,
        name = item.name.orEmpty()
    )

    companion object {
        const val CONTENT_TYPE = "LIST_ITEM"
    }
}