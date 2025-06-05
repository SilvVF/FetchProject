package ios.silv.fetch.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ios.silv.fetch.R
import ios.silv.fetch.createViewModel

@Composable
fun ListViewScreen() {

    val viewModel = createViewModel { ListViewModel() }

    val state by viewModel.state.collectAsStateWithLifecycle()

    ListViewContent(
        onRefresh = viewModel::refresh,
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListViewContent(
    onRefresh: () -> Unit,
    state: ListViewState,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.fetch_topbar_label)) },
                actions = {
                    IconButton(
                        onClick = onRefresh
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        PullToRefreshBox(
            onRefresh = onRefresh,
            isRefreshing = state is ListViewState.Loading,
            modifier = Modifier.padding(paddingValues)
        ) {
            when (state) {
                is ListViewState.Done -> {
                    ListViewDone(
                        state = state,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                is ListViewState.Error -> {
                    ListViewError(
                        modifier = Modifier.fillMaxSize(),
                        message = state.message
                    )
                }

                ListViewState.Loading -> {
                    ListViewLoading(Modifier.fillMaxSize())
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ListViewDone(
    state: ListViewState.Done,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        state.data.forEach { (listId, items) ->
            stickyHeader(key = "STICKY_HEADER_$listId") {
                Surface(Modifier.fillMaxSize()) {
                    Text(
                        modifier = Modifier.padding(6.dp),
                        text = stringResource(R.string.list_header_label, listId),
                        style = MaterialTheme.typography.headlineMedium,
                    )
                }
            }
            items.fastForEach { item ->
                item(
                    contentType = { ListItem.CONTENT_TYPE },
                    key = item.key
                ) {
                    ListItemView(
                        item,
                        Modifier
                            .padding(vertical = 6.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun ListItemView(item: ListItem, modifier: Modifier) {
    ElevatedCard(modifier) {
        Text(stringResource(R.string.list_item_id, item.id), Modifier.padding(4.dp))
        Text(stringResource(R.string.list_item_name, item.name), Modifier.padding(4.dp))
    }
}

@Composable
private fun ListViewError(
    modifier: Modifier = Modifier,
    message: String
) {
    Column(
        modifier = modifier.padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.fetch_error_message),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun ListViewLoading(
    modifier: Modifier = Modifier
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}