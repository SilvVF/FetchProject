package ios.silv.fetch

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.coroutines.cancellation.CancellationException

/**
 * Attempts [block], returning a successful [Result] if it succeeds, otherwise a [Result.Failure]
 * taking care not to break structured concurrency
 */
suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (cancellationException: CancellationException) {
        throw cancellationException
    } catch (exception: Exception) {
        Result.failure(exception)
    }


@Composable
@Suppress("UNCHECKED_CAST")
public inline fun <reified T : ViewModel> createViewModel(
    crossinline provider: () -> T,
): T {
    return viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T = provider() as T
        }
    )
}

//@Composable
//@Suppress("UNCHECKED_CAST")
//public inline fun <reified T : ViewModel> NavBackStackEntry.createViewModel(
//    crossinline provider: (handle: SavedStateHandle) -> T,
//): T = viewModel(
//    factory = object : AbstractSavedStateViewModelFactory(this, arguments) {
//        override fun <T : ViewModel> create(
//            key: String,
//            modelClass: Class<T>,
//            handle: SavedStateHandle,
//        ): T = provider(handle) as T
//    }
//)