package ios.silv.fetch.list

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ios.silv.fetch.api.FetchApi
import ios.silv.fetch.api.FetchResponse
import ios.silv.fetch.api.FetchResponseItem
import ios.silv.fetch.di.appGraph
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListViewModel(
    private val fetchApi: FetchApi = appGraph.fetchApi
) : ViewModel() {

    private val _state = MutableStateFlow<ListViewState>(ListViewState.Loading)
    val state = _state.asStateFlow()

    private var job: Job? = null

    init {
        refresh()
    }

    private fun List<FetchResponseItem>.groupDataByListIdSortByName() =
        this
            .groupBy { item -> item.listId }
            .mapValues { (_, list) ->
                list.sortedBy { item -> item.name }
            }
            .toSortedMap()

    fun refresh() {
        if (job?.isActive == true) {
            return
        }

        job = viewModelScope.launch {
            _state.update { ListViewState.Loading }

            val result = fetchApi.get().fold(
                onSuccess = { res ->
                    ListViewState.Done(
                        data = res
                            .filterNot { it.name.isNullOrBlank() }
                            .groupDataByListIdSortByName()
                            .mapValues { (_, items) ->
                                items.map { item ->
                                    ListItem(item)
                                }
                            }
                    )
                },
                onFailure = {
                    ListViewState.Error(it.message ?: "Unknown error")
                }
            )

            _state.update { result }
        }
    }
}